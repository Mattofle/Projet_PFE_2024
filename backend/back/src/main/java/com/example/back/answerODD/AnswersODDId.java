package com.example.back.answerODD;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class representing the ID of an answer related to ODD in the database.
 * This class is used as a composite primary key for the AnswerODD class.
 */
public class AnswersODDId implements Serializable {
  private int answer; // Doit correspondre à la propriété 'answer' de l'entité AnswerODD
  private int question; // Doit correspondre à la propriété 'question' de l'entité AnswerODD

  public AnswersODDId() {}

  public AnswersODDId(int answer, int question) {
    this.answer = answer;
    this.question = question;
  }

  // Getters
  /**
   * Gets the answer.
   *
   * @return the answer
   */
  public int getAnswer() {
    return answer;
  }

  /**
   * Gets the question.
   *
   * @return the question
   */
  public int getQuestion() {
    return question;
  }

  // Setters
  /**
   * Sets the answer.
   *
   * @param answer the to set
   */
  public void setAnswer(int answer) {
    this.answer = answer;
  }

  /**
   * Sets the question.
   *
   * @param question the to set
   */
  public void setQuestion(int question) {
    this.question = question;
  }

  // equals() et hashCode() basés sur answer et question
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AnswersODDId that = (AnswersODDId) o;
    return answer == that.answer && question == that.question;
  }

  @Override
  public int hashCode() {
    return Objects.hash(answer, question);
  }
}
