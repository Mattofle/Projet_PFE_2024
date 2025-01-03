package com.example.back.answerESG;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing answers.
 * This interface provides methods to perform CRUD operations on the database.
 */
@Repository
public interface AnswerESGRepository extends CrudRepository<AnswerESG, AnswerESGId> {

  /**
   * Find all answers for a given question.
   * @param questionId the id of the question
   * @return an iterable of answers
   */
  Iterable<AnswerESG> findByQuestion(int questionId);

  @Query("SELECT a FROM AnswerESG a WHERE a.answerId = ?1 AND a.question = ?2")
    AnswerESG findById(@Param("answerId") int answerId, @Param("questionId") int questionId);
}
