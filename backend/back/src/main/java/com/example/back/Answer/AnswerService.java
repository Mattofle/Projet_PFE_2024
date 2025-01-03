package com.example.back.Answer;

import com.example.back.answerESG.AnswerESG;
import com.example.back.answerESG.AnswerESGService;
import com.example.back.answerODD.AnswerODD;
import com.example.back.question.Type;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service class for managing answers.
 * This class provides methods to perform CRUD operations and additional business logic
 * related to answers, using the AnswerRepository for database interactions.
 */
@Service
public class AnswerService {

  private AnswerRepository answerRepository;

  private AnswerESGService answerESGService;

  public AnswerService(AnswerRepository answerRepository, AnswerESGService answerESGService) {
    this.answerRepository = answerRepository;
    this.answerESGService = answerESGService;
  }

  /**
   * Get all answers for a given question.
   * @param questionId the id of the question
   * @return an iterable of answers
   */
  public Iterable<Answer> getAnswersByQuestion(int questionId) {
    return answerRepository.findByAnswerId(questionId);
  }

  /**
   * Get an answer by its id.
   * @param answerId the id of the answer
   * @return the answer
   */
  public Answer getAnswerByAnswerId(int answerId){
    return answerRepository.findById(answerId);
  }

  /**
   * Get all answers of esg for a list of answers.
   * @param answerESGS the list of answers
   * @return an iterable of ESG answers
   */
  public Iterable<Answer> getAnswersByAnswerESG(Iterable<AnswerESG> answerESGS) {
    List<Answer> list = new ArrayList<>();

    for (AnswerESG answerESG : answerESGS) {
      for (Answer answer : getAnswersByQuestion(answerESG.getAnswerId())) {
        list.add(answer);
      }
    }
    return list;
  }

  /**
   * Get all answers of odd for a list of answers.
   * @param answerODDS the list of answers
   * @return an iterable of odd answers
   */
  public Iterable<Answer> getAnswersByAnswerODD(Iterable<AnswerODD> answerODDS) {
    List<Answer> list = new ArrayList<>();

    for (AnswerODD answerODD : answerODDS) {
      for (Answer answer : getAnswersByQuestion(answerODD.getAnswer())) {
        list.add(answer);
      }
    }
    return list;
  }

  /**
   * Save a new answer.
   * @param answer the answer to save
   * @param questionId the id of the question
   * @return the saved answer
   */
    public Answer save(String answer, int questionId) {
        Answer answer1 = new Answer();
        answer1.setAnswer(answer);
        answer1.setAnswerId(0);
        answer1.setScore(0.0);
        Answer newAnswer = answerRepository.save(answer1);

        AnswerESG answerESG = new AnswerESG();
        answerESG.setAnswerId(newAnswer.getAnswerId());
        answerESG.setQuestion(questionId);
        answerESG.setEngagementScore(0.0);
        answerESG.setType(Type.ALL);

        answerESGService.saveNewESGAnswer(answerESG);
        return newAnswer ;
    }

    /**
     * Modify an existing answer.
     * @param answer the new answer
     * @param answerId the id of the answer
     * @return the modified answer
     */
    public Answer modifyAnswer(String answer, int answerId) {
        Answer answer1 = answerRepository.findById(answerId);
        answer1.setAnswer(answer);
        return answerRepository.save(answer1);
    }

    /**
     * Delete an answer.
     * @param answerId the id of the answer
     */
    public void deleteAnswer(int answerId) {
        answerRepository.deleteById(answerId);
    }


    /**
     * Get the score of an answer.
     * @param answerId the id of the answer
     * @return the score of the answer
     */
    public double getScore(int answerId) {
        Answer answer = answerRepository.findById(answerId);
        return answer.getScore();
    }

    /**
     * Get the answer of an answer.
     * @param answerId the id of the answer
     * @return the answer of the answer
     */
    public String getAnswer(int answerId) {
        Answer answer = answerRepository.findById(answerId);
        return answer.getAnswer();
    }



}
