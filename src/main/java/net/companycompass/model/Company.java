package net.companycompass.model;

import jakarta.persistence.*;

/**
 * Represents a company entity mapped to the "companies" table in the database.
 * This class serves as a model for storing and retrieving company-related information.
 * It includes fields for the company's unique identifier, name, and location.
 */
@Entity
@Table(name = "companies")
public class Company {
    /**
     * Represents the unique identifier for the Company entity.
     * This field is annotated with @Id and @GeneratedValue to indicate that it serves
     * as the primary key, and its values are generated automatically using the
     * IDENTITY strategy by the persistence provider.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents the name of the company.
     * This field is mapped to the "name" column in the "companies" table and cannot be null.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Represents the location of the company.
     * This field is mapped to the "location" column in the "companies" table.
     */
    @Column(name = "location")
    private String location;

    /**
     * Default constructor for the Company class.
     * Initializes a new instance of the Company class with no properties set.
     * This constructor is typically used by frameworks or libraries
     * (e.g., JPA) that require a no-argument constructor.
     */
    public Company() {
    }

    /**
     * Constructs a new Company object with the specified name and location.
     *
     * @param name     the name of the company
     * @param location the location of the company
     */
    public Company(String name, String location) {
        this.name = name;
        this.location = location;
    }

    /**
     * Retrieves the unique identifier of the company.
     *
     * @return the unique identifier of the company as a Long
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the company.
     *
     * @param id the unique identifier to be set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the company.
     *
     * @return the name of the company as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the company.
     *
     * @param name the name of the company to be set
     */
    public void setName(String name) {
        this.name = sanitize(name);
    }

    /**
     * Retrieves the location of the company.
     *
     * @return the location of the company as a String
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the company.
     *
     * @param location the location of the company to be set
     */
    public void setLocation(String location) {
        this.location = sanitize(location);
    }

    private String sanitize(String input) {
        if (input == null) {
            return null;
        }
        String clean = input.trim();
        clean = clean.replaceAll("[^a-zA-Z0-9\\s.,'\\-&()@]", "");
        if (clean.length() > 50) {
            clean = clean.substring(0, 50);
        }
        return clean;
    }
}
