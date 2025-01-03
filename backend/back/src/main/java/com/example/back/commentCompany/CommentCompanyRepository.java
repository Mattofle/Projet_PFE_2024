package com.example.back.commentCompany;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing comments.
 * This interface provides methods to perform CRUD operations on the database.
 */
@Repository
public interface CommentCompanyRepository extends JpaRepository<CommentCompany, Integer> {

  /**
   * Find all comments for a given company.
   * @param companyId the id of the company
   * @return an iterable of comments
   */
  Iterable<CommentCompany> findAllByCompanyId(int companyId);

  /**
   * Find all comments for a given question.
   * @param questionId the id of the question
   * @param companyId the id of the company
   * @return a CommentCompany object
   */
   CommentCompany getCommentByQuestionIdAndCompanyId(int questionId, int companyId);

    void deleteByCompanyId(int companyId);
}
