package com.example.back.moduleCompany;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class representing the composite key of the ModuleCompany class.
 * This class is used to store the composite key of the ModuleCompany class.
 */
public class ModuleCompanyid implements Serializable {

    private int moduleId;
    private int companyId;

    public ModuleCompanyid(int moduleId, int companyId) {
        this.moduleId = moduleId;
        this.companyId = companyId;
    }

    public ModuleCompanyid() {
    }

    /**
     * Gets the module ID.
     *
     * @return the module ID
     */
    public int getModuleId() {
        return moduleId;
    }

    /**
     * Sets the module ID.
     *
     * @param moduleId the module ID to set
     */
    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * Gets the company ID.
     *
     * @return the company ID
     */
    public int getCompanyId() {
        return companyId;

    }

    /**
     * Sets the company ID.
     *
     * @param companyId the company ID to set
     */
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModuleCompanyid)) return false;
        ModuleCompanyid that = (ModuleCompanyid) o;
        return getModuleId() == that.getModuleId() && getCompanyId() == that.getCompanyId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getModuleId(), getCompanyId());
    }


}
