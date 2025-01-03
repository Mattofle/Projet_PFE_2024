package com.example.back.glossaire;

import org.springframework.stereotype.Service;

/**
 * Service class for managing glossary entries.
 * This class provides methods to perform CRUD operations and additional business logic
 * related to glossary entries, using the GlossaireRepository for database interactions.
 */
@Service
public class GlossaireService {
  private GlossaireRepository glossaireRepository;

  public GlossaireService(GlossaireRepository glossaireRepository) {
    this.glossaireRepository = glossaireRepository;
  }

  /**
   * Get all glossary entries.
   * @return an iterable of glossary entries
   */
  public Iterable<Glossaire> getGlossaires() {
    return glossaireRepository.findAll();
  }
}
