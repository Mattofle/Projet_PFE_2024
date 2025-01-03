package com.example.back.answerCompany.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class representing an answer related to a company in the database.
 * This class is a JPA entity linked to the "answerscompany" table in the "bd_pfe" schema.
 * It includes a unique constraint on the combination of module, answer, question, and company.
 */
@Entity
@Table(name = "answerscompany",
        schema = "bd_pfe",
        uniqueConstraints = {
                @UniqueConstraint(name = "answerscompany_unique",
                        columnNames = {"module", "answer", "question", "company"})
        }
)
@Getter
@Setter
@AllArgsConstructor

public class AnswersCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_company_id")
    private int answerCompany_id;

    @Column(nullable = false)
    boolean is_engagement;

    @Column(name = "module" ,nullable = false)
    private int moduleId;

    @Column(name = "answer" )
    private int answerId;

    @Column(name = "question" ,nullable = false)
    private int questionId;

    @Column(name = "company" ,nullable = false)
    private int companyId;

    /**
     * Gets the company answer ID.
     *
     * @return the company answer ID
     */
    public int getAnswerCompany_id() {
        return answerCompany_id;
    }

    /**
     * Sets the company answer ID.
     *
     * @param answerCompany_id the ID to set
     */
    public void setAnswerCompany_id(int answerCompany_id) {
        this.answerCompany_id = answerCompany_id;
    }

    /**
     * Checks if the answer is an engagement.
     *
     * @return true if the answer is an engagement, false otherwise
     */
    public boolean isIs_engagement() {
        return is_engagement;
    }

    /**
     * Sets whether the answer is an engagement.
     *
     * @param is_engament the engagement status to set
     */
    public void setIs_engagement(boolean is_engament) {
        this.is_engagement = is_engament;
    }


    /**
     * Gets the module ID.
     *
     * @return the module ID
     */
    public int getModule_id() {
        return moduleId;
    }

    /**
     * Sets the module ID.
     *
     * @param module_id the module ID to set
     */
    public void setModule_id(int module_id) {
        this.moduleId = module_id;
    }

    /**
     * Gets the answer ID.
     *
     * @return the answer ID
     */
    public int getAnswer_id() {
        return answerId;
    }

    /**
     * Sets the answer ID.
     *
     * @param answer_id the answer ID to set
     */
    public void setAnswer_id(int answer_id) {
        this.answerId = answer_id;
    }

    /**
     * Gets the question ID.
     *
     * @return the question ID
     */
    public int getQuestion_id() {
        return questionId;
    }

    /**
     * Sets the question ID.
     *
     * @param question_id the question ID to set
     */
    public void setQuestion_id(int question_id) {
        this.questionId = question_id;
    }

    /**
     * Gets the company ID.
     *
     * @return the company ID
     */
    public int getCompany_id() {
        return companyId;
    }

    /**
     * Sets the company ID.
     *
     * @param company_id the company ID to set
     */
    public void setCompany_id(int company_id) {
        this.companyId = company_id;
    }

    public AnswersCompany(boolean is_engagement, int module_id, int answer_id, int question_id, int company_id) {
        this.is_engagement = is_engagement;
        this.moduleId = module_id;
        this.answerId = answer_id;
        this.questionId = question_id;
        this.companyId = company_id;
    }

    public AnswersCompany() {
    }

    @Override
    public String toString() {
        return "AnswersCompany{" +
            "answerCompany_id=" + answerCompany_id +
            ", is_engagement=" + is_engagement +
            ", moduleId=" + moduleId +
            ", answerId=" + answerId +
            ", questionId=" + questionId +
            ", companyId=" + companyId +
            '}';
    }
}
