package com.example.back.answerESG;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class representing the ID of an answer related to ESG in the database.
 * This class is used as a composite primary key for the AnswerESG class.
 */
public class AnswerESGId implements Serializable {
  private int answerId;
  private int question;

  public AnswerESGId() {}

  public AnswerESGId(int answerId, int question) {
    this.answerId = answerId;
    this.question = question;
  }

  /**
   * Gets the answer ID.
   *
   * @return the answer ID
   */
  public int getAnswerId() {
    return answerId;
  }

  /**
   * Gets the question ID.
   *
   * @return the question ID
   */
  public int getQuestion() {
    return question;
  }

  /**
   * Sets the answer ID.
   *
   * @param answerId the ID to set
   */
  public void setAnswerId(int answerId) {
    this.answerId = answerId;
  }

  /**
   * Sets the question ID.
   *
   * @param question the ID to set
   */
  public void setQuestion(int question) {
    this.question = question;
  }

  // equals() et hashCode() basés sur answerId et question
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AnswerESGId that = (AnswerESGId) o;
    return answerId == that.answerId && question == that.question;
  }

  @Override
  public int hashCode() {
    return Objects.hash(answerId, question);
  }
}
