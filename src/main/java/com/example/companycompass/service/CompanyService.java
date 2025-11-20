package com.example.companycompass.service;

import com.example.companycompass.model.Company;

import java.util.List;

/**
 * Service interface for managing company-related operations.
 * This interface defines methods for retrieving, adding, updating, and deleting companies.
 * It serves as a contract for implementing various service layers that handle company data.
 */
public interface CompanyService {
    List<Company> getCompanies();

    Company getCompanyById(Long id);

    void addCompany(Company company);

    void updateCompany(Company company);

    void deleteCompany(Long id);
}
