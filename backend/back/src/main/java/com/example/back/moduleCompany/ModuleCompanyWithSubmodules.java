package com.example.back.moduleCompany;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a module company with submodules.
 * This class is used to store a module company and its submodules.
 * Create module three
 */
public class ModuleCompanyWithSubmodules {

  private ModuleCompany module;
  private String title;
  private List<ModuleCompanyWithSubmodules> subModules;

  public ModuleCompanyWithSubmodules(ModuleCompany module) {
    this.module = module;
    this.subModules = new ArrayList<>();
  }

  /**
   * Adds a submodule to the module company.
   *
   * @param subModule the submodule to add
   */
  public void addSubModule(ModuleCompanyWithSubmodules subModule) {
    this.subModules.add(subModule);
  }

  /**
   * Gets the title of the module company.
   *
   * @return the title of the module company
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the module company.
   *
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Gets the module company.
   *
   * @return the module company
   */
  public ModuleCompany getModule() {
    return module;
  }

  /**
   * Gets the submodules of the module company.
   *
   * @return the submodules of the module company
   */
  public List<ModuleCompanyWithSubmodules> getSubModules() {
    return subModules;
  }
}
