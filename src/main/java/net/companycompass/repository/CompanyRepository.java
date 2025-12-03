package net.companycompass.repository;

import net.companycompass.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Company} entities.
 * This interface provides CRUD operations and additional data access
 * functionalities for the {@link Company} entity through inheritance
 * from {@link JpaRepository}.
 * The main operations supported by this interface include:
 * - Save or update a company
 * - Retrieve companies by their ID
 * - Retrieve all companies
 * - Delete companies by their ID
 * This repository is annotated with {@code @Repository}, enabling Spring to detect
 * and manage it as a bean and to provide exception translation into Spring's
 * data-access exceptions.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
}
