package com.example.back.answerCompany;

import com.example.back.Answer.Answer;
import com.example.back.answerCompany.models.AnswersCompany;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing answers.
 * This interface provides methods to perform CRUD operations on the database.
 */
@Repository
public interface AnswerCompanyRepository extends CrudRepository<AnswersCompany, Integer> {

  /**
   * Find all answers for a given question.
   * @param questionId the id of the question
   * @return an iterable of answers
   */
  AnswersCompany findByModuleIdAndQuestionIdAndCompanyIdAndAnswerId(
      int moduleId, int questionId, int companyId, int answerId);

  @Query("SELECT a FROM AnswersCompany ac, Answer a WHERE ac.answerId = a.answerId AND ac.questionId = ?1 AND ac.companyId = ?2")
  Answer findByQuestionIdAndCompanyId(int questionId, int companyId);


        /**
         * Find all answers for a given question.
         * @param questionId the id of the question
         * @return an iterable of answers
         */
  Iterable<AnswersCompany> findAllByQuestionIdAndCompanyIdAndAnswerId(int questionId, int companyId,
      int answerId);

  //trouve toutes les answersCompany par company id et module id
  @Query("SELECT ac FROM AnswersCompany ac WHERE ac.companyId = ?1 AND ac.moduleId = ?2")
  Iterable<AnswersCompany> findByCompanyIdAndModuleId(int companyId, int moduleId);

  void deleteByCompanyId(int companyId);

}
