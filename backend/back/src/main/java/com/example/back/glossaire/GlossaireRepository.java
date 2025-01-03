package com.example.back.glossaire;

import org.springframework.data.repository.CrudRepository;

/**
 * Repository for managing glossary entries.
 * This interface provides methods to perform CRUD operations on the database.
 */
public interface GlossaireRepository extends CrudRepository<Glossaire, Integer> {

}
