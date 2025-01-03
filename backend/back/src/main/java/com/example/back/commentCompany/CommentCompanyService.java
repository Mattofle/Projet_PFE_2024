package com.example.back.commentCompany;

import org.springframework.stereotype.Service;

/**
 * Service class for managing comments.
 * This class provides methods to perform CRUD operations and additional business logic
 * related to comments, using the CommentCompanyRepository for database interactions.
 */
@Service
public class CommentCompanyService {

  private final CommentCompanyRepository commentCompanyRepository;

  public CommentCompanyService(CommentCompanyRepository commentCompanyRepository) {
    this.commentCompanyRepository = commentCompanyRepository;
  }

  /**
   * Get all comments for a given company.
   * @param companyId the id of the company
   * @return an iterable of comments
   */
  public Iterable<CommentCompany> getAllByCompany(int companyId) {
    return commentCompanyRepository.findAllByCompanyId(companyId);
  }




    /**
     * Save a comment.
     * @param companyId the id of the company
     * @param questionId the id of the question
     * @param comment the comment
     * @return the saved comment
     */
    public CommentCompany save(int companyId, int questionId,String comment) {
        CommentCompany commentCompany = new CommentCompany();
        commentCompany.setCompanyId(companyId);
        commentCompany.setQuestionId(questionId);
        commentCompany.setComment(comment);
        return commentCompanyRepository.save(commentCompany);
    }

    /**
     * Delete all comments for a given company.
     * @param companyId the id of the company
     */
    public CommentCompany getCommentByQuestionAndCompany(int questionId, int companyId) {
        return commentCompanyRepository.getCommentByQuestionIdAndCompanyId(questionId, companyId);
    }

    /**
     * Delete all comments for a given company.
     * @param companyId the id of the company
     */
    public void updateComment(int companyId, int questionId, String comment) {
        CommentCompany commentCompany = getCommentByQuestionAndCompany(questionId, companyId);
        commentCompany.setComment(comment);
        commentCompanyRepository.save(commentCompany);
    }



}
