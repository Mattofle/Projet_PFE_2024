package com.example.back.commentCompany;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Class representing a comment related to a company in the database.
 * This class is a JPA entity linked to the "commentscompany" table in the "bd_pfe" schema.
 * It includes a composite primary key on the combination of question and company.
 */
@Entity
@Table(name = "commentscompany", schema = "bd_pfe")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CommentCompanyId.class)
public class CommentCompany {

  @Id
  @Column(name = "question", nullable = false)
  private int questionId;

  @Id
  @Column(name = "company" ,nullable = false)
  private int companyId;

  @Column(nullable = false)
  private String comment;

  /**
   * Gets the question ID.
   *
   * @return the question ID
   */
  public int getQuestionId() {
    return questionId;
  }

  /**
   * Sets the question ID.
   *
   * @param question the ID to set
   */
  public void setQuestionId(int question) {
    this.questionId = question;
  }

  /**
   * Gets the company ID.
   *
   * @return the company ID
   */
  public int getCompanyId() {
    return companyId;
  }

  /**
   * Sets the company ID.
   *
   * @param company the ID to set
   */
  public void setCompanyId(int company) {
    this.companyId = company;
  }

  /**
   * Gets the comment.
   *
   * @return the comment
   */
  public String getComment() {
    return comment;
  }

  /**
   * Sets the comment.
   *
   * @param comment the comment to set
   */
  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  public String toString() {
    return "CommentCompany{" +
        "question=" + questionId +
        ", company=" + companyId +
        ", comment='" + comment + '\'' +
        '}';
  }
}
