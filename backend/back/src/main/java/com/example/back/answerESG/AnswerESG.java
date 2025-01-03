package com.example.back.answerESG;

import com.example.back.question.Type;
import jakarta.persistence.*;
import lombok.*;

/**
 * Class representing an answer related to ESG in the database.
 * This class is a JPA entity linked to the "answersESG" table in the "bd_pfe" schema.
 * It includes a unique constraint on the combination of answer and question.
 */
@Entity
@Table(name = "answersESG", schema = "bd_pfe")
@IdClass(AnswerESGId.class)  // Référence à la classe d'ID composite
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AnswerESG {

    @Id  // Partie de la clé primaire composite
    @Column(name = "answer", nullable = false)
    private int answerId;

    @Id  // Partie de la clé primaire composite
    @Column(nullable = false)
    private int question;

    @Column()
    private boolean default_engagement;

    @Column(name="engagementscore")
    private Double engagementScore;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

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

  /**
   * Sets the engagement score.
   *
   * @param engagementScore the score to set
   */
  public void setEngagementScore(Double engagementScore) {
    this.engagementScore = engagementScore;
  }

  /**
   * Sets the type.
   *
   * @param type the type to set
   */
  public void setType(Type type) {
    this.type = type;
  }


    /**
     * Gets the answer ID.
     *
     * @return the answer ID
     */
    public int getAnswerId() {
        return this.answerId;
    }

    /**
     * Gets the question ID.
     *
     * @return the question ID
     */
    public int getQuestion() {
        return this.question;
    }


    /**
     * Gets the engagement score.
     *
     * @return the engagement score
     */
    public Double getEngagementScore() {
        return this.engagementScore;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public Type getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "AnswerESG{" +
            "answerId=" + answerId +
            ", question=" + question +
            ", engagementScore=" + engagementScore +
            ", type=" + type +
            '}';
    }
}
