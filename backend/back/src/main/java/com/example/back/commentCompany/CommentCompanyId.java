package com.example.back.commentCompany;

import com.example.back.moduleCompany.ModuleCompanyid;

import java.io.Serializable;
import java.util.Objects;


/**
 * Class representing the ID of a comment related to a company in the database.
 * This class is used as a composite primary key for the CommentCompany class.
 */
public class CommentCompanyId implements Serializable {

  private int questionId;
  private int companyId;

    public CommentCompanyId(int questionId, int companyId) {
        this.questionId = questionId;
        this.companyId = companyId;
    }

    public CommentCompanyId() {
    }


  /**
   * Gets the question ID.
   * @return the question ID
   */
  public int getQuestionId() {
    return questionId;
  }

  /**
   * Sets the question ID.
   * @param question the ID to set
   */
  public void setQuestionId(int question) {
    this.questionId = question;
  }

  /**
   * Gets the company ID.
   * @return the company ID
   */
  public int getCompanyId() {
    return companyId;
  }

  /**
   * Sets the company ID.
   * @param company the ID to set
   */
  public void setCompanyId(int company) {
    this.companyId = company;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {return true;
    }
    if (o instanceof CommentCompanyId) {
      return false;
    }
    CommentCompanyId that = (CommentCompanyId) o;
    return getQuestionId() == that.getQuestionId() && getCompanyId() == that.getCompanyId();
  }


  @Override
  public int hashCode() {
    return Objects.hash(questionId, companyId);
  }


  @Override
  public String toString() {
    return "CommentCompanyId{" +
        "question=" + questionId +
        ", company=" + companyId +
        '}';
  }
}
