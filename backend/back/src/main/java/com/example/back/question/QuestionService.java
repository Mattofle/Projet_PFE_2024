package com.example.back.question;

import com.example.back.company.models.Company;
import com.example.back.module.Module;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service class for managing questions.
 * This class provides methods to perform CRUD operations and additional business logic
 * related to questions, using the QuestionRepository for database interactions.
 */
@Service
public class QuestionService {

  private QuestionRepository questionRepository;

  public QuestionService(QuestionRepository questionRepository) {
    this.questionRepository = questionRepository;
  }

  /**
   * Get all questions.
   * @return an iterable of questions
   */
  public Iterable<Question> getQuestions() {
    return questionRepository.findAll();
  }

  /**
   * Get all questions by module.
   * @param moduleId the module id
   * @return an iterable of questions
   */
  public Iterable<Question> getQuestionsByModule(int moduleId) {
    return questionRepository.findByModule(moduleId);
  }

  /**
   * Get all questions by modules.
   * @param modules the modules
   * @return a list of questions
   */
  public List<Question> getQuestionsByModules(Iterable<Module> modules, Company company) {
    List<Question> list = new ArrayList<>();

    for (Module module : modules) {
      for (Question question : getQuestionsByModule(module.getModuleId())) {
        question.setTitle(question.getTitle().replace("XXX", company.getName()));
        list.add(question);
      }
    }
    return list;
  }

  /**
   * Get a question by question id.
   * @param questionId the question id
   * @return the question
   */
  public Question findByQuestionId(int questionId) {
    return questionRepository.findByQuestionId(questionId);
  }




}
