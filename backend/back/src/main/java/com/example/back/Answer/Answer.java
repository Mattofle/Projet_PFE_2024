package com.example.back.Answer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class representing an answer in the database.
 * This class is a JPA entity linked to the "answers" table in the "bd_pfe" schema.
 */
@Entity
@Table(name = "answers", schema = "bd_pfe")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "answerId", nullable = false)
  private int answerId;

  @Column(nullable = false)
  private String answer;

  @Column()
  private Double score;

  /**
   * Gets the answer ID.
   *
   * @return the answer ID
   */
  public int getAnswerId() {
    return this.answerId;
  }

  /**
   * Gets the content of the answer.
   *
   * @return the content of the answer
   */
  public String getAnswer() {
    return this.answer;
  }

  /**
   * Sets the content of the answer.
   *
   * @param answer the content to set
   */
  public void setAnswer(String answer) {
    this.answer = answer;
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
   * Gets the score of the answer.
   *
   * @return the score of the answer
   */
  public Double getScore() {
    return this.score;
  }

  /**
   * Sets the score of the answer.
   *
   * @param score the score to set
   */
  public void setScore(Double score) {
    this.score = score;
  }

  @Override
  public String toString() {
    return "Answer{" +
        "answerId=" + answerId +
        ", answer='" + answer + '\'' +
        ", score=" + score +
        '}';
  }
}
