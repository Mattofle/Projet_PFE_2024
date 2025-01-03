package com.example.back.company.models;

import lombok.AllArgsConstructor;

/**
 * Class representing a company update.
 * This class holds information related to the company's name, address, phone number, installation, workers, and product.
 * DTO used to store the updates of a company.
 */
@AllArgsConstructor
public class CompanyUpdate {

    private String name;

    private String address;

    private String phoneNumber;

    private Boolean hasInstalation;

    private Boolean ownsInstalation;

    private Integer workers;

    private Boolean hasProduct;


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
     * @return the company installations
     */
    public Boolean getHasInstalation() {
        return hasInstalation;
    }

    /**
     * Sets if company has installations.
     *
     * @param hasInstalation the installations to set
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
     * @return the company product
     */
    public Boolean getHasProduct() {
        return hasProduct;
    }

    /**
     * Sets if company has a product.
     *
     * @param hasProduct the product to set
     */
    public void setHasProduct(Boolean hasProduct) {
        this.hasProduct = hasProduct;
    }

    /**
     * Gets if company owns installations.
     *
     * @return the company installations
     */
    public Boolean getOwnsInstalation() {
        return ownsInstalation;
    }

    /**
     * Sets if company owns installations.
     *
     * @param ownsInstalation the installations to set
     */
    public void setOwnsInstalation(Boolean ownsInstalation) {
        this.ownsInstalation = ownsInstalation;
    }
}
