package com.example.back.scoring;

import com.example.back.Answer.AnswerService;
import com.example.back.answerCompany.AnswerCompanyService;
import com.example.back.answerCompany.models.AnswersCompany;
import com.example.back.answerESG.AnswerESGService;
import com.example.back.module.ModuleService;
import com.example.back.moduleCompany.ModuleCompany;
import com.example.back.moduleCompany.ModuleCompanyService;
import com.example.back.moduleCompany.ModuleCompanyWithSubmodules;
import com.example.back.question.Question;
import com.example.back.question.QuestionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service class for managing ESG scoring.
 * This class provides methods to calculate ESG scores for companies and modules.
 */
@Service
public class ScoringEsgService {

    private final AnswerCompanyService answerCompanyService;
    private final ModuleService moduleService;
    private final ModuleCompanyService moduleCompanyService;
    private final AnswerService answerService;

    private final ScoringService scoringService;

    private final AnswerESGService answerESGService;

    private final QuestionService questionService;

    public ScoringEsgService(AnswerCompanyService answerCompanyService, ModuleService moduleService,
                             ModuleCompanyService moduleCompanyService, AnswerService answerService, ScoringService scoringService, AnswerESGService answerESGService, QuestionService questionService) {
        this.answerCompanyService = answerCompanyService;
        this.moduleService = moduleService;
        this.moduleCompanyService = moduleCompanyService;
        this.answerService = answerService;
        this.scoringService = scoringService;
        this.answerESGService = answerESGService;
        this.questionService = questionService;
    }


    /**
     * Calculate ESG scores for a company.
     * @param companyId the company id
     * @return a map of ESG scores for each module
     */
    public Map<String, List<Double>> calculateModulesESGScoresForCompany(int companyId) {
        // Get the tree of modules starting from the "ODD" root
        ModuleCompanyWithSubmodules moduleTreeE = moduleCompanyService.getModuleTreeForCompany(companyId, "Environnementale");
        ModuleCompanyWithSubmodules moduleTreeS = moduleCompanyService.getModuleTreeForCompany(companyId, "Sociale");
        ModuleCompanyWithSubmodules moduleTreeG = moduleCompanyService.getModuleTreeForCompany(companyId, "Gouvernance");

        // Calculate the total score
        ScoreESG scoreE = calculateModuleTreeScoresESG(moduleTreeE, companyId);
        ScoreESG scoreS = calculateModuleTreeScoresESG(moduleTreeS, companyId);
        ScoreESG scoreG = calculateModuleTreeScoresESG(moduleTreeG, companyId);
        return Map.of(
                "Environnementale", List.of(scoreE.getScore(), scoreE.getScoreEngament(), scoreE.getScoreMax()),
                "Sociale", List.of(scoreS.getScore(), scoreS.getScoreEngament(), scoreS.getScoreMax()),
                "Gouvernance", List.of(scoreG.getScore(), scoreG.getScoreEngament(), scoreG.getScoreMax())
        );
    }

    /**
     * Calculate ESG scores for a module tree.
     * @param moduleTree the module tree
     * @param companyId the company id
     * @return the total score
     */
    public ScoreESG calculateModuleTreeScoresESG(ModuleCompanyWithSubmodules moduleTree, int companyId) {
        ScoreESG score;

        if (moduleTree.getSubModules().isEmpty()) {
            // Sous-sous-root: Feuilles sans sous-modules, calculer les questions
            score = calculateScoreForQuestionsESG(moduleTree.getModule(), companyId);
        } else {
            // Root: Calculer avec la logique spécifique
            score = calculateRootScoreESG(moduleTree, companyId);
        }

        updateModuleCompanyScoreESG(moduleTree.getModule(), companyId, score);
        return score;
    }

    /**
     * check if the module is a sub root
     * @param moduleTree the module tree
     * @return true if the module is a sub root, false otherwise
     */
    public boolean isSubRoot(ModuleCompanyWithSubmodules moduleTree) {
        return !moduleTree.getSubModules().isEmpty() &&
                moduleTree.getSubModules().get(0).getSubModules().isEmpty();
    }

    /**
     * Calculate the score for a module's questions.
     * @param module the module
     * @param companyId the company id
     * @return the total score
     */
    private ScoreESG calculateScoreForQuestionsESG(ModuleCompany module, int companyId) {
        Iterable<AnswersCompany> answers = answerCompanyService.getAnswersCompanyByCompanyIdAndModuleId(companyId, module.getModuleId());
        ScoreESG scoreESG = new ScoreESG();
        double score = 0;
        double scoreEngagement = 0;

        for (AnswersCompany answer : answers) {
            if(answer.isIs_engagement()){
                scoreEngagement += answerESGService.getScoreEngagement(answer.getAnswer_id(),answer.getQuestion_id());
            }else {
                score += answerService.getScore(answer.getAnswer_id());
            }
        }
        scoreESG.setScoreEngament(scoreEngagement);
        scoreESG.setScore(score);
        for (Question question : questionService.getQuestionsByModule(module.getModuleId())) {
            scoreESG.setScoreMax(scoreESG.getScoreMax() + question.getScoreMax());
        }

        return scoreESG;
    }


    /**
     * Calculate the total score for a module tree.
     * @param root the root of the module tree
     * @param companyId the company id
     * @return the total score
     */
    private ScoreESG calculateRootScoreESG(ModuleCompanyWithSubmodules root, int companyId) {
        ScoreESG sum = new ScoreESG();

        List<ModuleCompanyWithSubmodules> subModules = root.getSubModules();


        for (ModuleCompanyWithSubmodules subModule : root.getSubModules()) {
            ScoreESG subModuleScore = calculateModuleTreeScoresESG(subModule, companyId);
            sum.setScore(sum.getScore() + subModuleScore.getScore());
            sum.setScoreEngament(sum.getScoreEngament() + subModuleScore.getScoreEngament());
            sum.setScoreMax(sum.getScoreMax() + subModuleScore.getScoreMax());
        }

        if(sum.getScoreMax() < sum.getScore()){
            sum.setScoreMax(sum.getScore());
        }

        return sum;
    }

    /**
     * Update the score for a module-company relationship.
     * @param module the module
     * @param companyId the company
     * @param score the score
     */
    public void updateModuleCompanyScoreESG(ModuleCompany module, int companyId, ScoreESG score) {
        ModuleCompany moduleCompany = moduleCompanyService.getModuleCompany(companyId, module.getModuleId());
        moduleCompany.setScore(Math.round( score.getScore() * 100.0) / 100.0);

        moduleCompany.setScore_engagement(Math.round(score.getScoreEngament() * 100.0) / 100.0 );
        moduleCompany.setScoremax( Math.round(score.getScoreMax() * 100.0) / 100.0 );
        moduleCompanyService.updateModuleCompanyScore(moduleCompany);
    }

}
