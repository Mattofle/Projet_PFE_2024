package com.example.back.module;

import com.example.back.Answer.Answer;
import com.example.back.answerCompany.models.AnswersCompany;
import com.example.back.answerCompany.models.CompanyAnswer;
import com.example.back.answerESG.AnswerESG;
import com.example.back.answerODD.AnswerODD;
import com.example.back.commentCompany.CommentCompany;
import com.example.back.question.Question;

/**
 * Class representing all the information related to a module in the database.
 * This class is used as a data transfer object for the Module class.
 * This class holds information related to the module, its questions, answers, company answers, ESG answers, ODD answers, and company comments.
 * used to generate all forms
 */
public class ModuleAllInfos {

  private Iterable<Module> modules;
  private Iterable<Question> questions;
  private Iterable<Answer> answers;
  private Iterable<AnswersCompany> companyAnswers;

  private Iterable<CommentCompany> commentCompanies;

  private Iterable<AnswerESG> answerESGS;
  private Iterable<AnswerODD> answerODDS;

  public ModuleAllInfos() {
  }

  /**
   * Gets the ODD answers.
   *
   * @return the ODD answers
   */
  public Iterable<AnswerODD> getAnswerODDS() {
    return answerODDS;
  }

  /**
   * Sets the ODD answers.
   *
   * @param answerODDS the ODD answers to set
   */
  public void setAnswerODDS(Iterable<AnswerODD> answerODDS) {
    this.answerODDS = answerODDS;
  }

  /**
   * Gets the modules.
   *
   * @return the modules
   */
  public Iterable<Module> getModules() {
    return modules;
  }

  /**
   * Sets the modules.
   *
   * @param modules the modules to set
   */
  public void setModules(Iterable<Module> modules) {
    this.modules = modules;
  }

  /**
   * Gets the questions.
   *
   * @return the questions
   */
  public Iterable<Question> getQuestions() {
    return questions;
  }

  /**
   * Sets the questions.
   *
   * @param questions the questions to set
   */
  public void setQuestions(Iterable<Question> questions) {
    this.questions = questions;
  }

  /**
   * Gets the ESG answers.
   *
   * @return the ESG answers
   */
  public Iterable<AnswerESG> getAnswerESGS() {
    return answerESGS;
  }

  /**
   * Sets the ESG answers.
   *
   * @param answerESGS the ESG answers to set
   */
  public void setAnswerESGS(Iterable<AnswerESG> answerESGS) {
    this.answerESGS = answerESGS;
  }

  /**
   * Gets the answers.
   *
   * @return the answers
   */
  public Iterable<Answer> getAnswers() {
    return answers;
  }

  /**
   * Sets the answers.
   *
   * @param answers the answers to set
   */
  public void setAnswers(Iterable<Answer> answers) {
    this.answers = answers;
  }

  /**
   * Gets the company answers.
   *
   * @return the company answers
   */
  public Iterable<AnswersCompany> getCompanyAnswers() {
    return companyAnswers;
  }

  /**
   * Sets the company answers.
   *
   * @param companyAnswers the company answers to set
   */
  public void setCompanyAnswers(Iterable<AnswersCompany> companyAnswers) {
    this.companyAnswers = companyAnswers;
  }

  /**
   * Gets the company comments.
   *
   * @return the company comments
   */
  public Iterable<CommentCompany> getCommentCompanies() {
    return commentCompanies;
  }

  /**
   * Sets the company comments.
   *
   * @param commentCompanies the company comments to set
   */
  public void setCommentCompanies(
      Iterable<CommentCompany> commentCompanies) {
    this.commentCompanies = commentCompanies;
  }
}
