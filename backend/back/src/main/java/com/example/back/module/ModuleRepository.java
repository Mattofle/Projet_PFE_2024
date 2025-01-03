package com.example.back.module;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository for managing modules.
 * This interface provides methods to perform CRUD operations on the database.
 */
@Repository
public interface ModuleRepository extends CrudRepository<Module, Integer> {

  /**
   * Find all modules for a given parent module.
   * @param moduleId the id of the parent module
   * @return an iterable of modules
   */
  public Iterable<Module> findByParentModule(int moduleId);

  /**
   * Find a module by its title.
   * @param title the title of the module
   * @return a Module object
   */
  public Module findByTitle(String title);

  /**
   * Find a module by its id.
   * @param moduleId the id of the module
   * @return a Module object
   */
  public Module findByModuleId(int moduleId);

}
