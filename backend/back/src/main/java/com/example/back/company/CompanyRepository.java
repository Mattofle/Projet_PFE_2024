package com.example.back.company;

import com.example.back.company.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for managing companies.
 * This interface provides methods to perform CRUD operations on the database.
 */
public interface CompanyRepository extends JpaRepository<Company, Integer> {



}