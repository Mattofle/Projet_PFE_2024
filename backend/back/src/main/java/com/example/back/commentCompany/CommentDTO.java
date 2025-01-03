package com.example.back.commentCompany;

import lombok.AllArgsConstructor;

/**
 * Class representing a comment related to a company in the database.
 * This class is used as a data transfer object for the CommentCompany class.
 */
@AllArgsConstructor
public class CommentDTO {
    private String comment;
    private int question;

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

    /**
     * Gets the question ID.
     *
     * @return the question ID
     */
    public int getQuestion() {
        return question;
    }

    /**
     * Sets the question ID.
     *
     * @param question the ID to set
     */
    public void setQuestion(int question) {
        this.question = question;
    }


}
