package com.example.back.answerODD;

import com.example.back.question.Question;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service class for managing answers.
 * This class provides methods to perform CRUD operations and additional business logic
 * related to answers, using the AnswerODDepository for database interactions.
 */
@Service
public class AnswerODDService {

  AnswerODDepository answerODDepository;

  public AnswerODDService(AnswerODDepository answerODDepository) {
    this.answerODDepository = answerODDepository;
  }

  /**
   * Get all ODD answers for a given question.
   * @param id the id of the question
   * @return an iterable of answers
   */
  public Iterable<AnswerODD> getAnswersODDByQuestion(int id) {
    return answerODDepository.findByQuestion(id);
  }

  /**
   * Get all ODD answers for a given question.
   * @param questions the list of questions
   * @return an iterable of answers
   */
  public Iterable<AnswerODD> getAnswersODDByQuestions(Iterable<Question> questions) {
    List<AnswerODD> list = new ArrayList<>();

    for (Question question : questions) {
      for (AnswerODD answerODD : getAnswersODDByQuestion(question.getQuestionId())) {
        list.add(answerODD);
      }
    }
    return list;
  }

}
