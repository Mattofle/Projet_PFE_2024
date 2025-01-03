package com.example.back.question;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing questions.
 * This interface provides methods to perform CRUD operations on the database.
 */
@Repository
public interface QuestionRepository extends CrudRepository<Question, Integer> {

  public Iterable<Question> findByModule(int moduleId);

    public Question findByQuestionId(int questionId);


}
