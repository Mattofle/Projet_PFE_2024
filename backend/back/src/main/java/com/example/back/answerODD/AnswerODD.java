package com.example.back.answerODD;

import com.example.back.answerESG.AnswerESGId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class representing an answer related to ODD in the database.
 * This class is a JPA entity linked to the "answersODD" table in the "bd_pfe" schema.
 * It includes a composite primary key on the combination of answer and question.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "answersODD", schema = "bd_pfe")
@IdClass(AnswersODDId.class)
public class AnswerODD {
  @Id  // Partie de la clé primaire composite
  @Column(nullable = false)
  private int answer;

  @Id  // Partie de la clé primaire composite
  @Column(nullable = false)
  private int question;

  @Override
  public String toString() {
    return "AnswerODD{" +
        "answer=" + answer +
        ", question=" + question +
        '}';
  }

  /**
   * Gets the answer.
   * @return the answer
   */
  public int getAnswer() {
    return this.answer;
  }

  /**
   * Gets the question.
   * @return the question
   */
  public int getQuestion() {
    return this.question;
  }
}
