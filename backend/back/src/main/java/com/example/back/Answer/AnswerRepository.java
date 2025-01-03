package com.example.back.Answer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing answers.
 * This interface provides methods to perform CRUD operations on the database.
 */
@Repository
public interface AnswerRepository extends CrudRepository<Answer, Integer> {
  /**
   * Find all answers for a given question.
   * @param questionId the id of the question
   * @return an iterable of answers
   */
  Iterable<Answer> findByAnswerId(int questionId);

  /**
   * Find an answer by its id.
   * @param answerId the id of the answer
   * @return the answer
   */
  @Query("SELECT a FROM Answer a WHERE  a.answerId = ?1")
    Answer findById(int answerId);

}
