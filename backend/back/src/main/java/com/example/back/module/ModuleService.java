package com.example.back.module;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Service class for managing modules.
 * This class provides methods to perform CRUD operations and additional business logic
 * related to modules, using the ModuleRepository for database interactions.
 */
@Service
public class ModuleService {


  private ModuleRepository moduleRepository;

  public ModuleService(ModuleRepository moduleRepository) {
    this.moduleRepository = moduleRepository;
  }

  /**
   * Get all modules.
   * @return an iterable of modules
   */
  public Iterable<Module> getModulesEsg(){
    Module module = moduleRepository.findByTitle("ESG");
    return getAllModuls(module);
  }

  /**
   * Get all modules.
   * @return an iterable of modules
   */
  public Iterable<Module> getModulesOdd(){
    Module module = moduleRepository.findByTitle("ODD");
    return getAllModuls(module);
  }

  /**
   * Get all modules.
   * @return an iterable of modules
   */
  private List<Module> getAllModuls(Module module){
    List<Module> list = new ArrayList<>();

    if (module.getTitle().equals("ESG") || module.getTitle().equals("ODD")){
      list.add(module);
    }

    Iterable<Module> iter = moduleRepository.findByParentModule(module.getModuleId());

    for (Module sousModule : iter) {
      list.add(sousModule);
      list.addAll(getAllModuls(sousModule));
    }
    return list;
  }

  /**
   * Get a module by its title.
   * @param title the title of the module
   * @return a Module object
   */
  public Module getModule(String title) {
    return moduleRepository.findByTitle(title);
  }

  /**
   * Get a module by its id.
   * @param moduleId the id of the module
   * @return a Module object
   */
  public Module getModuleById(int moduleId) {
    return moduleRepository.findByModuleId(moduleId);
  }
}
