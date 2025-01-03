package com.example.back.company.models;

import jakarta.persistence.*;
import lombok.*;

/**
 * Class representing a company in the database.
 * This class is a JPA entity linked to the "companies" table in the "bd_pfe" schema.
 */
@Entity
@Table(name = "companies", schema = "bd_pfe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @Column(nullable = false)
    private int company_id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String address;

    @Column(unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private Boolean hasInstalation;

    @Column(nullable = false)
    private Boolean ownsInstalation;

    @Column(nullable = false)
    private Integer workers;

    @Column(nullable = false)
    private Boolean hasProduct;

    /**
     * Gets the company ID.
     *
     * @return the company ID
     */
    public int getCompany_id() {
        return company_id;
    }

    /**
     * Sets the company ID.
     *
     * @param compagny_id the ID to set
     */
    public void setCompany_id(int compagny_id) {
        this.company_id = compagny_id;
    }

    /**
     * Gets the company name.
     *
     * @return the company name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the company name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the company address.
     *
     * @return the company address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the company address.
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the company phone number.
     *
     * @return the company phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the company phone number.
     *
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /**
     * Gets if company has installations.
     *
     * @return boolean if company has installations
     */
    public Boolean getHasInstalation() {
        return hasInstalation;
    }

    /**
     * Sets if company has installations.
     *
     * @param hasInstalation boolean if company has installations
     */
    public void setHasInstalation(Boolean hasInstalation) {
        this.hasInstalation = hasInstalation;
    }



    /**
     * Gets the number of workers.
     *
     * @return the number of workers
     */
    public Integer getWorkers() {
        return workers;
    }

    /**
     * Sets the number of workers.
     *
     * @param workers the number of workers to set
     */
    public void setWorkers(Integer workers) {
        this.workers = workers;
    }

    /**
     * Gets if company has a product.
     *
     * @return boolean if company has a product
     */
    public Boolean getHasProduct() {
        return hasProduct;
    }

    /**
     * Sets if company has a product.
     *
     * @param hasProduct boolean if company has a product
     */
    public void setHasProduct(Boolean hasProduct) {
        this.hasProduct = hasProduct;
    }

    /**
     * Gets if company owns installations.
     *
     * @return boolean if company owns installations
     */
    public Boolean getOwnsInstalation() {
        return ownsInstalation;
    }

    /**
     * Sets if company owns installations.
     *
     * @param ownsInstalation boolean if company owns installations
     */
    public void setOwnsInstalation(Boolean ownsInstalation) {
        this.ownsInstalation = ownsInstalation;
    }
}