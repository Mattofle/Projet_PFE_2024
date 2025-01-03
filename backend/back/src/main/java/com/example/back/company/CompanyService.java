package com.example.back.company;

import com.example.back.answerCompany.AnswerCompanyRepository;
import com.example.back.commentCompany.CommentCompanyRepository;
import com.example.back.company.models.Company;
import com.example.back.moduleCompany.ModuleCompanyRepository;
import com.example.back.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing companies.
 * This class provides methods to perform CRUD operations and additional business logic
 * related to companies, using the CompanyRepository for database interactions.
 */
@Service
public class CompanyService {

    private CompanyRepository companyRepository;

    private final ModuleCompanyRepository moduleCompanyRepository;

    private final AnswerCompanyRepository answerCompanyRepository;

    private final CommentCompanyRepository commentCompanyRepository;

    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, ModuleCompanyRepository moduleCompanyRepository, AnswerCompanyRepository answerCompanyRepository, CommentCompanyRepository commentCompanyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.moduleCompanyRepository = moduleCompanyRepository;
        this.answerCompanyRepository = answerCompanyRepository;
        this.commentCompanyRepository = commentCompanyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get a company by id.
     * @param id the company id
     * @return the company
     */
    public Company getCompany(int id) {
        return companyRepository.findById(id).get();
    }

    /**
     * Update a company info.
     * @param companyUp the company updated
     * @param companyId the company
     * @return the updated company
     */
    public Company updateCompany(Company companyUp, int companyId) {

        Company company = getCompany( companyId);

        if (companyUp.getName() != null && !companyUp.getName().isBlank()) {
            company.setName(companyUp.getName());
        }
        if (companyUp.getAddress() != null && !companyUp.getAddress().isBlank()) {
            company.setAddress(companyUp.getAddress());
        }
        if (companyUp.getPhoneNumber() != null) {
            company.setPhoneNumber(companyUp.getPhoneNumber());
        }
        if (companyUp.getHasInstalation() != null) {
            company.setHasInstalation(companyUp.getHasInstalation());
        }
        if (companyUp.getOwnsInstalation() != null) {
            company.setOwnsInstalation(companyUp.getOwnsInstalation());
        }
        if (companyUp.getWorkers() != null) {
            company.setWorkers(companyUp.getWorkers());
        }
        if (companyUp.getHasProduct() != null) {
            company.setHasProduct(companyUp.getHasProduct());
        }
        return companyRepository.save(company);
    }

    /**
     * Save a new company.
     * @return all companies
     */
    public Iterable<Company> getCompanies() {
        return companyRepository.findAll();
    }

    @Transactional
    public void deleteCompany(int id) {
        answerCompanyRepository.deleteByCompanyId(id);
        moduleCompanyRepository.deleteByCompanyId(id);
        commentCompanyRepository.deleteByCompanyId(id);
        companyRepository.deleteById(id);
        userRepository.deleteById(id);
    }


}
