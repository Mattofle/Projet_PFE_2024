package com.example.back.moduleCompany;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing module-company relationships.
 * This interface provides methods to perform CRUD operations on the database.
 */
@Repository
public interface ModuleCompanyRepository extends CrudRepository<ModuleCompany, Integer> {
    public ModuleCompany save(ModuleCompany moduleCompany) ;

    public ModuleCompany findByCompanyIdAndModuleId(int companyId, int moduleId) ;

    public Iterable<ModuleCompany> findByCompanyId(int companyId) ;

    @Query("SELECT mc FROM ModuleCompany mc JOIN Module m ON mc.moduleId = m.moduleId " +
        "WHERE mc.companyId = :companyId AND m.parentModule = :parentModuleId")
    List<ModuleCompany> findSubModulesForCompany(@Param("companyId") int companyId,
        @Param("parentModuleId") int parentModuleId);

    void deleteByCompanyId(int id);

}
