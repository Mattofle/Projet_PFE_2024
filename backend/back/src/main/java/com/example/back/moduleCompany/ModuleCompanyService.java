package com.example.back.moduleCompany;

import com.example.back.module.Module;
import com.example.back.module.ModuleService;
import java.util.List;
import org.springframework.stereotype.Service;

import java.sql.Date;

/**
 * Service class for managing module-company relationships.
 * This class provides methods to perform CRUD operations and additional business logic
 * related to module-company relationships, using the ModuleCompanyRepository for database interactions.
 */
@Service
public class ModuleCompanyService {

  private final ModuleCompanyRepository moduleCompanyRepository;
  private final ModuleService moduleService;

  public ModuleCompanyService(ModuleCompanyRepository moduleCompanyRepository,
      ModuleService moduleService) {
    this.moduleCompanyRepository = moduleCompanyRepository;
    this.moduleService = moduleService;
  }

  /**
   * Get a module-company relationship by company id and module title.
   * @param companyId the company id
   * @param moduleTitle the module title
   * @return the module-company relationship
   */
  public ModuleCompany getModuleCompany(int companyId, String moduleTitle) {
      Module module = moduleService.getModule(moduleTitle);
      return moduleCompanyRepository.findByCompanyIdAndModuleId(companyId, module.getModuleId());
  }

  /**
   * Get a module-company relationship by company id and module id.
   * @param companyId the company id
   * @param moduleId the module id
   * @return the module-company relationship
   */
  public ModuleCompany getModuleCompany(int companyId, int moduleId) {
    return moduleCompanyRepository.findByCompanyIdAndModuleId(companyId, moduleId);
  }


    /**
     * Save a module-company relationship.
     * @param companyId the company id
     * @param moduleId the module id
     * @return the saved module-company relationship
     */
    public ModuleCompany saveModuleCompany(int companyId, int moduleId ) {
        ModuleCompany moduleCompany = new ModuleCompany();
        moduleCompany.setCompanyId(companyId);
        moduleCompany.setModuleId(moduleId);
        moduleCompany.setDate_form(null);
        moduleCompany.setScoremax(0.0);
        Module module =  moduleService.getModuleById(moduleId);
        if (module.getParentModule() != null) {
            saveModuleCompany(companyId, module.getParentModule());
        }

    return moduleCompanyRepository.save(moduleCompany);
  }

  /**
   * Get all module-company relationships for a given company.
   * @param companyId the company id
   * @return an iterable of module-company relationships
   */
  public Iterable<ModuleCompany> getCompanyModules(int companyId) {
    return moduleCompanyRepository.findByCompanyId(companyId);
  }


  /**
   * Get the module tree for a company.
   * @param companyId the company
   * @param moduleTitle the title of the root module
   * @return the module tree
   */
  public ModuleCompanyWithSubmodules getModuleTreeForCompany(int companyId, String moduleTitle) {
    Module moduleODD = moduleService.getModule(moduleTitle);

    ModuleCompany moduleRoot = moduleCompanyRepository.findByCompanyIdAndModuleId(companyId,
        moduleODD.getModuleId());

    if(moduleRoot == null) {
      return null;
    }
    // Construire l'arborescence à partir du module racine
    return buildModuleTree(companyId, moduleRoot);
  }


  /**
   * Build the module tree for company.
   * @param companyId the company id
   * @param current the current module
   * @return the module tree
   */
    private ModuleCompanyWithSubmodules buildModuleTree(int companyId, ModuleCompany current) {
        // Crée un objet ModuleWithSubModules avec le module courant
        ModuleCompanyWithSubmodules moduleWithSubModules = new ModuleCompanyWithSubmodules(current);
      moduleWithSubModules.setTitle(moduleService.getModuleById(current.getModuleId()).getTitle());

    // Récupère les sous-modules pour le module courant
    List<ModuleCompany> subModules = getSubModulesForCompany(companyId, current);

    // Pour chaque sous-module, on les ajoute récursivement à l'arbre
    for (ModuleCompany subModule : subModules) {
      moduleWithSubModules.addSubModule(buildModuleTree(companyId, subModule));
    }

    return moduleWithSubModules;
  }

  /**
   * Get the sub-modules for a company.
   * @param companyId the company
   * @param current the current module
   * @return the list of sub-modules
   */
  private List<ModuleCompany> getSubModulesForCompany(int companyId, ModuleCompany current) {
    Module moduleAssociated = moduleService.getModuleById(current.getModuleId());

    return moduleCompanyRepository.findSubModulesForCompany(companyId,
        moduleAssociated.getModuleId());
  }

  /**
   * Update the score for a module-company relationship.
   * @param moduleCompany the module-company relationship
   */
  public void updateModuleCompanyScore(ModuleCompany moduleCompany) {
    moduleCompanyRepository.save(moduleCompany);
  }


  /**
   * add the date for a module-company relationship.
   * @param companyId the company id
   * @param moduleTitle the module title
   */
  public void addDates(int companyId, String moduleTitle) {
    ModuleCompanyWithSubmodules moduleTree = getModuleTreeForCompany(companyId, moduleTitle);
    addDates(moduleTree, companyId);

    }


    /**
     * add the date for a moduletree-company relationship.
     * @param companyId the company id
     * @param moduleTree the module tree
     */
  public void addDates(ModuleCompanyWithSubmodules moduleTree, int companyId) {
    if (!moduleTree.getSubModules().isEmpty()) {
      for (ModuleCompanyWithSubmodules subModule : moduleTree.getSubModules()) {
        addDates(subModule, companyId);
      }
    }

    ModuleCompany moduleCompany = moduleCompanyRepository.findByCompanyIdAndModuleId(companyId, moduleTree.getModule().getModuleId());
    moduleCompany.setDate_form(new Date(System.currentTimeMillis()));
    moduleCompanyRepository.save(moduleCompany);
  }


    /**
     * Validate a module for a company.
     * @param companyId the company
     * @param moduleId the module id
     */
    public void ValidateModuleCompany(int companyId, int moduleId) {
        String moduleTitle = moduleService.getModuleById(moduleId).getTitle();
        ModuleCompanyWithSubmodules moduleTree = getModuleTreeForCompany(companyId, moduleTitle);
        ValidateModuleCompany(companyId, moduleTree);


    }

    /**
     * Validate a module for a company.
     * @param companyId the company
     * @param modules the module tree
     */
    public void ValidateModuleCompany(int companyId,ModuleCompanyWithSubmodules modules) {
        if (!modules.getSubModules().isEmpty()) {
            for (ModuleCompanyWithSubmodules module : modules.getSubModules()) {
                ValidateModuleCompany(companyId, module);
            }

        }

        Module module = moduleService.getModuleById(modules.getModule().getModuleId());
        ModuleCompany moduleCompany = moduleCompanyRepository.findByCompanyIdAndModuleId(companyId, module.getModuleId());
        moduleCompany.setIs_validated(true);
        moduleCompanyRepository.save(moduleCompany);
    }

}
