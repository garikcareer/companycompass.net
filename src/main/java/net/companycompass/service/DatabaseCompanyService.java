package net.companycompass.service;

import net.companycompass.model.Company;
import net.companycompass.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Profile("local")
@Transactional
public class DatabaseCompanyService implements CompanyService {
    private final CompanyRepository companyRepository;

    @Autowired
    public DatabaseCompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    @Override
    public void addCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public void updateCompany(Company company) {
        Company existing = getCompanyById(company.getId());
        existing.setName(company.getName());
        existing.setLocation(company.getLocation());
        companyRepository.save(existing);
    }

    @Override
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Company not found with id: " + id);
        }
        companyRepository.deleteById(id);
    }
}