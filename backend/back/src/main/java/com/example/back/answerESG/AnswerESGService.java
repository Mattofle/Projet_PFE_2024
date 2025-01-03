package com.example.back.answerESG;

import com.example.back.Answer.AnswerRepository;
import com.example.back.Answer.AnswerService;
import com.example.back.company.models.Company;
import com.example.back.question.Question;
import com.example.back.question.Type;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service class for managing ESG answers.
 * This class provides methods to perform CRUD operations and additional business logic
 * related to ESG answers, using the AnswerESGRepository for database interactions.
 */
@Service
public class AnswerESGService {

  private AnswerESGRepository answerESGRepository;
  private AnswerRepository answerRepository;

  public AnswerESGService(AnswerESGRepository answerESGRepository, AnswerRepository answerRepository) {
    this.answerESGRepository = answerESGRepository;
    this.answerRepository = answerRepository;
  }

  /**
   * Get all answers for a given question in ESG.
   * @param id the id of the question
   * @return an iterable of answers
   */
  public Iterable<AnswerESG> getAnswersESGByQuestion(int id) {
    return answerESGRepository.findByQuestion(id);
  }


  /**
   * Get all answers for a given question in ESG.
   * @param questions the list of questions
   * @param company the company
   * @return an iterable of answers
   */
  public Iterable<AnswerESG> getAnswersESGByQuestions(Iterable<Question> questions,
      Company company) {
    List<AnswerESG> list = new ArrayList<>();

    for (Question question : questions) {
      for (AnswerESG answerESG : getAnswersESGByQuestion(question.getQuestionId())) {
        if (validAnswerESGForCompany(question, answerESG, company)) {
          list.add(answerESG);
        }
      }
    }
    return list;
  }

  /**
   * validate an ESG answer for a company.
   * @param question the question
   * @param answerESG the answer
   * @param company the company
   * @return true if the answer is valid for the company, false otherwise
   */
  private boolean validAnswerESGForCompany(Question question, AnswerESG answerESG,
      Company company) {

    if (answerESG.getType().equals(Type.ALL)) {
      return true;
    }
    if (answerESG.getType().equals(Type.OWNED_FACILITY) && company.getOwnsInstalation()) {
      return true;
    }
    if (answerESG.getType().equals(Type.FACILITY) && company.getHasInstalation()) {
      return true;
    }
    if (answerESG.getType().equals(Type.WORKERS) && company.getWorkers() > 0) {
      return true;
    }
    if (answerESG.getType().equals(Type.PRODUITS) && company.getHasProduct()) {
      return true;
    }
    if (answerRepository.findById(answerESG.getAnswerId()).getAnswer().contains("N/A")) {
      return true;
    }
    return false;
  }


  /**
   * Save a new ESG answer.
   * @param answer the answer to save
   * @return the saved answer
   */
  public AnswerESG saveNewESGAnswer(AnswerESG answer) {

    return answerESGRepository.save(answer);
  }

  /**
   * Get the engagement score of an answer.
   * @param answerId the id of the answer
   * @param questionId the id of the question
   * @return the engagement score
   */
  public double getScoreEngagement(int answerId, int questionId) {
    AnswerESG answer = answerESGRepository.findById(answerId, questionId);

    if (answer == null || answer.getEngagementScore() == null){
      return (double) 0;
    }
    return answer.getEngagementScore();
  }


}
