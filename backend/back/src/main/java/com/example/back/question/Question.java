package com.example.back.question;

import jakarta.persistence.*;
import lombok.*;

/**
 * Class representing a question in the database.
 * This class is a JPA entity linked to the "questions" table in the "bd_pfe" schema.
 */
@Entity
@Table(name = "questions", schema = "bd_pfe")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Question implements Comparable<Question> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;

    @Column(nullable = false)
    private String title;

    @Column(name = "scoremax", nullable = false)
    private Double scoreMax;

    @Column( nullable = false)
    private int module;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Display display;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
        private Type type;

    /**
     * Gets the question ID.
     *
     * @return the question ID
     */
    public Integer getQuestionId() {
        return questionId;
    }

    /**
     * Sets the question ID.
     *
     * @param questionId the ID to set
     */
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the maximum score.
     *
     * @return the maximum score
     */
    public Double getScoreMax() {
        return scoreMax;
    }

    /**
     * Sets the maximum score.
     *
     * @param scoreMax the maximum score to set
     */
    public void setScoreMax(Double scoreMax) {
        this.scoreMax = scoreMax;
    }

    /**
     * Gets the module.
     *
     * @return the module
     */
    public int getModule() {
        return module;
    }

    /**
     * Sets the module.
     *
     * @param module the module to set
     */
    public void setModule(int module) {
        this.module = module;
    }

    /**
     * Gets the display.
     *
     * @return the display
     */
    public Display getDisplay() {
        return display;
    }

    /**
     * Sets the display.
     *
     * @param display the display to set
     */
    public void setDisplay(Display display) {
        this.display = display;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the type to set
     */
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Question{" +
            "questionId=" + questionId +
            ", title='" + title + '\'' +
            ", scoreMax=" + scoreMax +
            ", module=" + module +
            ", display=" + display +
            ", type=" + type +
            '}';
    }

    @Override
    public int compareTo(Question q) {
        return questionId - q.getQuestionId();
    }


    public enum Display {
        MULTIPLE("MULTIPLE"),
        ONE("ONE"),
        OPEN("OPEN");

        private final String display;

        Display(String display) {
            this.display = display;
        }
    }

}
