package com.example.back.answerCompany.models;

/**
 * Class representing a company answer.
 * This class holds information related to module, question, answer, comments, and engagement status.
 * DTO used to store the answers of a company to a question.
 * */
public class CompanyAnswer
{
    private int moduleId;
    private int questionId;
    private int answerId;
    private String comment;

    private  String openAnswer;
    private boolean isEngamement;

    /**
     * Constructor for CompanyAnswer.
     * @param moduleId the module ID
     * @param questionId the question ID
     * @param answerId the answer ID
     * @param openAnswer the open answer
     * @param isEngamement the engagement status
     */
    public CompanyAnswer(int moduleId, int questionId, int answerId, String openAnswer, boolean isEngamement) {
        this.moduleId = moduleId;
        this.questionId = questionId;
        this.answerId = answerId;
        this.isEngamement = isEngamement;
        this.openAnswer = openAnswer;
    }

    /**
     * get module ID.
     * @return the module ID
     */
    public int getModuleId() {
        return moduleId;
    }

    /**
     * set module ID.
     * @param moduleId the module ID
     */
    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * get question ID.
     * @return the question ID
     */
    public int getQuestionId() {
        return questionId;
    }

    /**
     * set question ID.
     * @param questionId the question ID
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    /**
     * get aswere ID.
     * @return the answer ID
     */
    public int getAnswerId() {
        return answerId;
    }

    /**
     * get oper answere.
     * @return the open answer
     */
    public String getOpenAnswer() {
        return openAnswer;
    }

    /**
     * set open answer.
     * @param openAnswer the open answer
     */
    public void setOpenAnswer(String openAnswer) {
        this.openAnswer = openAnswer;
    }

    /**
     * set answer ID.
     * @param answerId the answer ID
     */
    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }


    /**
     * check if an engagement was taken on this answere.
     * @return true if an engagement was taken, false otherwise.
     */
    public boolean isEngamement() {
        return isEngamement;
    }

    /**
     * set the engagement status.
     * @param engamement the engagement status
     */
    public void setEngamement(boolean engamement) {
        isEngamement = engamement;
    }

    @Override
    public String toString() {
        return "CompanyAnswer{" +
            "moduleId=" + moduleId +
            ", questionId=" + questionId +
            ", answerId=" + answerId +
            ", comment='" + comment + '\'' +
            ", openAnswer='" + openAnswer + '\'' +
            ", isEngamement=" + isEngamement +
            '}';
    }
}
