package com.example.back.answerODD;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing answers.
 * This interface provides methods to perform CRUD operations on the database.
 */
@Repository
public interface AnswerODDepository extends CrudRepository<AnswerODD, AnswersODDId> {

  /**
   * Find all answers for a given question.
   * @param questionId the id of the question
   * @return an iterable of answers
   */
  Iterable<AnswerODD> findByQuestion(int questionId);

}
