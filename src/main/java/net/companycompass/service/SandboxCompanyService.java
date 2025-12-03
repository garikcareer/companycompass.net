package net.companycompass.service;

import net.companycompass.model.Company;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@SessionScope
@Profile("demo")
public class SandboxCompanyService implements CompanyService {
    private List<Company> companies;
    private long nextId = 1;
    private static final int MAX_ROWS = 12;

    @PostConstruct
    public void init() {
        this.companies = new ArrayList<>();
        saveInternal("TechNova Solutions", "San Francisco, CA");
        saveInternal("BlueFin Capital", "New York, NY");
        saveInternal("GreenLeaf Energy", "Denver, CO");
        saveInternal("Summit Health Systems", "Nashville, TN");
        saveInternal("Apex Logistics Global", "Miami, FL");
        saveInternal("Quantum Dynamics", "Boston, MA");
        saveInternal("SilverLine Architecture", "Chicago, IL");
        saveInternal("RedRock Consulting", "Phoenix, AZ");
        saveInternal("Orbit Media Group", "Los Angeles, CA");
        saveInternal("Cascade Engineering", "Seattle, WA");
    }

    @Override
    public List<Company> getCompanies() {
        return new ArrayList<>(companies);
    }

    @Override
    public Company getCompanyById(Long id) {
        return companies.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    @Override
    public void addCompany(Company company) {
        if (companies.size() >= MAX_ROWS) return;
        saveInternal(company.getName(), company.getLocation());
    }

    @Override
    public void updateCompany(Company updatedInfo) {
        Optional<Company> existingOpt = companies.stream()
                .filter(c -> c.getId().equals(updatedInfo.getId()))
                .findFirst();

        if (existingOpt.isPresent()) {
            Company existing = existingOpt.get();
            existing.setName(updatedInfo.getName());
            existing.setLocation(updatedInfo.getLocation());
        } else {
            throw new RuntimeException("Company not found with id: " + updatedInfo.getId());
        }
    }

    @Override
    public void deleteCompany(Long id) {
        companies.removeIf(c -> c.getId().equals(id));
    }

    private void saveInternal(String name, String location) {
        Company c = new Company();
        c.setId(nextId++);
        c.setName(name);
        c.setLocation(location);
        companies.add(c);
    }
}