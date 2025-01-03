package com.example.back.scoring;

import com.example.back.Answer.AnswerService;
import com.example.back.answerCompany.AnswerCompanyService;
import com.example.back.answerCompany.models.AnswersCompany;
import com.example.back.module.ModuleService;
import com.example.back.moduleCompany.ModuleCompany;
import com.example.back.moduleCompany.ModuleCompanyService;
import com.example.back.moduleCompany.ModuleCompanyWithSubmodules;
import org.springframework.stereotype.Service;

/**
 * Service class for managing scoring.
 * This class provides methods to calculate scores for companies and modules.
 */
@Service
public class ScoringService {
  private final AnswerCompanyService answerCompanyService;
  private final ModuleService moduleService;
  private final ModuleCompanyService moduleCompanyService;
  private final AnswerService answerService;

  /**
   * Constructor.
   * @param answerCompanyService the answer company service
   * @param moduleService the module service
   * @param moduleCompanyService the module company service
   * @param answerService the answer service
   */
  public ScoringService(AnswerCompanyService answerCompanyService, ModuleService moduleService,
      ModuleCompanyService moduleCompanyService, AnswerService answerService) {
    this.answerCompanyService = answerCompanyService;
    this.moduleService = moduleService;
    this.moduleCompanyService = moduleCompanyService;
    this.answerService = answerService;
  }

  /**
   * Calculate ODD scores for a company.
   * @param companyId the company
   * @return the ODD score
   */
  public Integer calculateModulesODDScoresForCompany(int companyId) {
    // Get the tree of modules starting from the "ODD" root
    ModuleCompanyWithSubmodules moduleTree = moduleCompanyService.getModuleTreeForCompany(companyId, "ODD");

    // Calculate the total score
    double totalScore = calculateModuleTreeScores(moduleTree, companyId);
    int roundedValue = (int) Math.round(totalScore);

    return roundedValue;
  }

  /**
   * Calculate ESG scores for a company.
   * @param companyId the company
   * @param moduleTree the module tree
   * @param companyId the company id
   * @return the ESG score
   */
  public double calculateModuleTreeScores(ModuleCompanyWithSubmodules moduleTree, int companyId) {
    double score;

    if (moduleTree.getSubModules().isEmpty()) {
      // Sous-sous-root: Feuilles sans sous-modules, calculer les questions
      score = calculateScoreForQuestions(moduleTree.getModule(), companyId);
    } else if (isSubRoot(moduleTree)) {
      // Sous-root: Calculer selon la formule spécifique
      score = calculateSubRootScore(moduleTree, companyId);
    } else {
      // Root: Calculer avec la logique spécifique
      score = calculateRootScore(moduleTree, companyId);
    }

    updateModuleCompanyScore(moduleTree.getModule(), companyId, score);
    return score;
  }

  /**
   * Check if the module tree is a sub-root.
   * @param moduleTree the module tree
   * @return true if the module tree is a sub-root, false otherwise
   */
  public boolean isSubRoot(ModuleCompanyWithSubmodules moduleTree) {
    return !moduleTree.getSubModules().isEmpty() &&
        moduleTree.getSubModules().get(0).getSubModules().isEmpty();
  }

  /**
   * Calculate the score for a sub-root.
   * @param subRoot the sub-root
   * @param companyId the company id
   * @return the sub-root score
   */
  private double calculateSubRootScore(ModuleCompanyWithSubmodules subRoot, int companyId) {
    double sum = 0;
    int count = subRoot.getSubModules().size();

    for (ModuleCompanyWithSubmodules subModule : subRoot.getSubModules()) {
      double subModuleScore = calculateModuleTreeScores(subModule, companyId);
      sum += subModuleScore;
    }

    double average = sum / count;
    average = average / 100;

    return (1 - Math.pow(1 - average, 2)) * 100;
  }

  /**
   * Calculate the score for a root.
   * @param root the root
   * @param companyId the company
   * @return the root score
   */
  private double calculateRootScore(ModuleCompanyWithSubmodules root, int companyId) {
    double maxScore = 0;
    double sumScores = 0;
    int count = root.getSubModules().size();

    // Calculer le score maximum et la somme des scores des sous-modules
    for (ModuleCompanyWithSubmodules subModule : root.getSubModules()) {
      double subModuleScore = calculateModuleTreeScores(subModule, companyId);
      maxScore = Math.max(maxScore, subModuleScore);
      sumScores += subModuleScore;
    }

    // Calculer le score final selon la formule
    double rootScore = maxScore + 0.15 * (sumScores - maxScore);

    // Limiter à 100 (ou 1 si on utilise des pourcentages)
    rootScore = Math.min(rootScore, 100);

    return rootScore;
  }


  /**
   * Calculate the score for a module's questions.
   * @param module the module
   * @param companyId the company id
   * @return the total score
   */
  private double calculateScoreForQuestions(ModuleCompany module, int companyId) {
    Iterable<AnswersCompany> answers = answerCompanyService.getAnswersCompanyByCompanyIdAndModuleId(companyId, module.getModuleId());
    double score = 0;

    for (AnswersCompany answer : answers) {
      score += answerService.getScore(answer.getAnswer_id());
    }

    return score;
  }

  /**
   * Update the score for a module-company relationship.
   * @param module the module
   * @param companyId the company
   * @param score the score
   */
  private void updateModuleCompanyScore(ModuleCompany module, int companyId, double score) {
    ModuleCompany moduleCompany = moduleCompanyService.getModuleCompany(companyId, module.getModuleId());
    moduleCompany.setScore(score);
    moduleCompanyService.updateModuleCompanyScore(moduleCompany);
  }


}
