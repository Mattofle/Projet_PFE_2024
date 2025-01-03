package com.example.back.answerCompany;

import com.example.back.Answer.Answer;
import com.example.back.Answer.AnswerService;
import com.example.back.answerCompany.models.AnswersCompany;
import com.example.back.answerCompany.models.CompanyAnswer;
import com.example.back.answerESG.AnswerESG;
import com.example.back.answerODD.AnswerODD;
import com.example.back.commentCompany.CommentCompanyService;
import com.example.back.company.CompanyService;
import com.example.back.company.models.Company;
import com.example.back.moduleCompany.ModuleCompany;
import com.example.back.moduleCompany.ModuleCompanyService;
import com.example.back.question.Question;
import com.example.back.question.QuestionRepository;
import com.example.back.question.QuestionService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Service class for managing answers.
 * This class provides methods to perform CRUD operations and additional business logic
 * related to answers, using the AnswerRepository for database interactions.
 */
@Service
public class AnswerCompanyService {

  private final AnswerCompanyRepository answerCompanyRepository;
  private final ModuleCompanyService moduleCompanyService;

  private final QuestionRepository questionRepository;

  private final AnswerService answerService;

  private final QuestionService questionService;

  private final CompanyService companyService;

  private final CommentCompanyService commentCompanyService;

  public AnswerCompanyService(AnswerCompanyRepository answerCompanyRepository,
      ModuleCompanyService moduleCompanyService, QuestionRepository questionRepository,
      AnswerService answerService, QuestionService questionService, CompanyService companyService,
      CommentCompanyService commentCompanyService) {
    this.answerCompanyRepository = answerCompanyRepository;
    this.moduleCompanyService = moduleCompanyService;
    this.questionRepository = questionRepository;
    this.answerService = answerService;
    this.questionService = questionService;
    this.companyService = companyService;
    this.commentCompanyService = commentCompanyService;
  }


  /**
   * Save an answer for a company.
   * @param answer the answer to save
   * @param companyId the id of the company
   */
  @Transactional
  public void saveAnswerCompany(CompanyAnswer answer, int companyId) {
    if (answer.getModuleId() == 0) {
      answer.setModuleId(questionRepository.findByQuestionId(answer.getQuestionId()).getModule());
    }
    ModuleCompany moduleCompany = moduleCompanyService.getModuleCompany(companyId,
        answer.getModuleId());

    if (moduleCompany == null) {
      moduleCompany = moduleCompanyService.saveModuleCompany(companyId, answer.getModuleId());
    }

    int answerId = answer.getAnswerId();
    String display = questionRepository.findByQuestionId(answer.getQuestionId()).getDisplay()
        .name();

    if (display.equals("OPEN")) {
      Answer answer1 = answerCompanyRepository.findByQuestionIdAndCompanyId(answer.getQuestionId(),
          companyId);
      if (answer1 == null) {
        answer1 = answerService.save(answer.getOpenAnswer(), answer.getQuestionId());

      } else {
        answerService.modifyAnswer(answer.getOpenAnswer(), answer1.getAnswerId());
        return;
      }
      answerId = answer1.getAnswerId();
    }
    if (display.equals("ONE")) {
      Answer oldAnswer = answerCompanyRepository.findByQuestionIdAndCompanyId(
          answer.getQuestionId(), companyId);
      if (oldAnswer != null) {
        CompanyAnswer oldanswerCompany = new CompanyAnswer(answer.getModuleId(),
            answer.getQuestionId(), oldAnswer.getAnswerId(), null, false);
        if(oldanswerCompany.isEngamement()== answer.isEngamement()){
          modifiyAnswerCompanyAnswerId(oldanswerCompany, companyId, answerId);
          return;
        }
      }
    }

    AnswersCompany answerCompany = new AnswersCompany(answer.isEngamement(),
            answer.getModuleId(), answerId, answer.getQuestionId(), moduleCompany.getCompanyId());
    answerCompanyRepository.save(answerCompany);
  }


  /**
   * Delete an answer for a company.
   * @param answer the answer to delete
   * @param companyId the id of the company
   */
  @Transactional
  public void deleteAnswerCompany(CompanyAnswer answer, int companyId) {
    if (answer.getModuleId() == 0) {
      answer.setModuleId(questionRepository.findByQuestionId(answer.getQuestionId()).getModule());
    }
    AnswersCompany answerCompany = answerCompanyRepository.findByModuleIdAndQuestionIdAndCompanyIdAndAnswerId(
        answer.getModuleId(), answer.getQuestionId(), companyId, answer.getAnswerId());
    if (questionRepository.findByQuestionId(answer.getQuestionId()).getDisplay().name()
        .equals("OPEN")) {
      answerService.deleteAnswer(answer.getAnswerId());
    }

    answerCompanyRepository.delete(answerCompany);
  }

  /**
   * Get all answers for a company.
   * @param companyId the id of the company
   * @return an iterable of answers
   */
  public AnswersCompany getAnswerCompany(CompanyAnswer answer, int companyId) {
    if (answer.getModuleId() == 0) {
      answer.setModuleId(questionRepository.findByQuestionId(answer.getQuestionId()).getModule());
    }
    return answerCompanyRepository.findByModuleIdAndQuestionIdAndCompanyIdAndAnswerId(
        answer.getModuleId(), answer.getQuestionId(), companyId, answer.getAnswerId());
  }

  /**
   * Get all answers for a company.
   * @param companyId the id of the company
   * @return an iterable of answers
   */
  public Iterable<AnswersCompany> getAnswerCompanyByAnswerODD(AnswerODD answerODD, int companyId) {
    return answerCompanyRepository.findAllByQuestionIdAndCompanyIdAndAnswerId(
        answerODD.getQuestion(),
        companyId, answerODD.getAnswer());
  }

  /**
   * Get all answers for a company.
   * @param companyId the id of the company
   * @return an iterable of answers
   */
  public Iterable<AnswersCompany> getAnswersCompanyByAnswerODDS(Iterable<AnswerODD> answerODDS,
      int companyId) {
    List<AnswersCompany> list = new ArrayList<>();
    for (AnswerODD answerODD : answerODDS) {
      for (AnswersCompany answersCompany : getAnswerCompanyByAnswerODD(answerODD, companyId)) {
        list.add(answersCompany);
      }
    }
    return list;
  }

  /**
   * Get all esg answers for a company.
   * @param companyId the id of the company
   * @return an iterable of answers
   */
  public Iterable<AnswersCompany> getAnswerCompanyByAnswerESG(AnswerESG answerESG, int companyId) {
    return answerCompanyRepository.findAllByQuestionIdAndCompanyIdAndAnswerId(
        answerESG.getQuestion(),
        companyId, answerESG.getAnswerId());
  }

  /**
   * Get all esg titled answers for a company based on answers id.
   * @param companyId the id of the company
   * @return an iterable of answers
   */
  public Iterable<AnswersCompany> getAnswersCompanyByAnswerESGS(Iterable<AnswerESG> answerESGS,
      int companyId) {
    List<AnswersCompany> list = new ArrayList<>();
    for (AnswerESG answerESG : answerESGS) {
      for (AnswersCompany answersCompany : getAnswerCompanyByAnswerESG(answerESG, companyId)) {
        list.add(answersCompany);
      }
    }
    return list;
  }

  /**
   * update the comment of an answer.
   * @param answer the answer to updated
   * @param companyId the id of the company
   */
  public void updateComment(CompanyAnswer answer, int companyId) {
    if (answer.getModuleId() == 0) {
      answer.setModuleId(questionRepository.findByQuestionId(answer.getQuestionId()).getModule());
    }
    AnswersCompany answerCompany = answerCompanyRepository.findByModuleIdAndQuestionIdAndCompanyIdAndAnswerId(
        answer.getModuleId(), answer.getQuestionId(), companyId, answer.getAnswerId());
    answerCompanyRepository.save(answerCompany);
  }


  /**
   * Get all company answers.
   * @param companyId the id of the company
   * @return an iterable of answers
   */
  public Iterable<AnswersCompany> getAnswersCompanyByCompanyIdAndModuleId(int companyId,
      int moduleId) {
    return answerCompanyRepository.findByCompanyIdAndModuleId(companyId, moduleId);
  }


  /**
   * Update the engagement of an answer.
   * @param answer the answer updated
   * @param companyId the id of the company
   */
  public void updateEngagement(CompanyAnswer answer, int companyId) {
    if (answer.getModuleId() == 0) {
      answer.setModuleId(questionRepository.findByQuestionId(answer.getQuestionId()).getModule());
    }
    AnswersCompany answerCompany = answerCompanyRepository.findByModuleIdAndQuestionIdAndCompanyIdAndAnswerId(
        answer.getModuleId(), answer.getQuestionId(), companyId, answer.getAnswerId());
    answerCompany.setIs_engagement(answer.isEngamement());

    answerCompanyRepository.save(answerCompany);
  }

  /**
   * Update a company's answer.
   * @param answer the answer updated
   * @param companyId the id of the company
   */
  public void modifiyAnswerCompanyAnswerId(CompanyAnswer answer, int companyId, int answerId) {
    if (answer.getModuleId() == 0) {
      answer.setModuleId(questionRepository.findByQuestionId(answer.getQuestionId()).getModule());
    }
    AnswersCompany answerCompany = answerCompanyRepository.findByModuleIdAndQuestionIdAndCompanyIdAndAnswerId(
        answer.getModuleId(), answer.getQuestionId(), companyId, answer.getAnswerId());
    answerCompany.setAnswer_id(answerId);
    answerCompanyRepository.save(answerCompany);
  }


  /**
   * Get all company engagement.
   * @param companyId the id of the company
   * @return a map of : key = question , value = list of map (key = answer, value = comment)
   */
  public Map<String, List<Map<String, String>>> getEngagement(int companyId) {
    Map<String, List<Map<String, String>>> map = new HashMap<>();

    for (ModuleCompany moduleCompany : moduleCompanyService.getCompanyModules(companyId)) {
      for (AnswersCompany answersCompany : answerCompanyRepository.findByCompanyIdAndModuleId(
          companyId, moduleCompany.getModuleId())) {
        if (answersCompany.isIs_engagement()) {
          int answerId = answersCompany.getAnswer_id();
          Question question = questionService.findByQuestionId(answersCompany.getQuestion_id());
          Company company = companyService.getCompany(companyId);

          question.setTitle(question.getTitle().replace("XXX", company.getName()));
          List<Map<String,String>> list = new ArrayList<>();

          String questionTitle = question.getTitle();
          String comment = null;

          try {
             comment = commentCompanyService.getCommentByQuestionAndCompany(
                question.getQuestionId(), companyId).getComment();
          } catch (NullPointerException ignored) {}

          String answer = answerService.getAnswerByAnswerId(answerId).getAnswer();


          if (map.containsKey(questionTitle)) {
            list = map.get(questionTitle);
          }
          Map<String, String> tempMap = new HashMap<>();
          tempMap.put("answer", answer);
          tempMap.put("comment", comment);

          list.add(tempMap);
          map.put(questionTitle, list);
        }
      }
    }
    return map;
  }

  /**
   * check if a module is validated.
   * @param answerCompany
   * @param companyId
   * @return
   */
  public boolean moduleValidated(CompanyAnswer answerCompany, int companyId) {
        if(answerCompany.getModuleId() == 0){
            answerCompany.setModuleId(questionRepository.findByQuestionId(answerCompany.getQuestionId()).getModule());
        }
        ModuleCompany moduleCompany = moduleCompanyService.getModuleCompany(companyId, answerCompany.getModuleId());
        if(moduleCompany != null){
            return moduleCompany.isIs_validated();
        }
        return false;
    }
}
