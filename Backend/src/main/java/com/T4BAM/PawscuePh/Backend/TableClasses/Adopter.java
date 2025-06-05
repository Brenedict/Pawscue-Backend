package com.T4BAM.PawscuePh.Backend.TableClasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;

@Entity
@Table(name = "adopter")
public class Adopter {
    @Id
    @Column(name = "adopterid")
    private String adopterId;

    @Column(name = "adoptername")
    private String adopterName;

    @Column(name = "contactnum")
    private String contactNum;

    @Column(name = "emailaddress")
    private String emailAddress;

    // @OneToOne
    // @JoinColumn(name = "adopteraddressid", referencedColumnName = "adopteraddressid")
    @Column(name = "adopteraddressid")
    private String adopterAddressId;

    // @OneToOne
    // @JoinColumn(name = "spouseid")
    @Column(name = "spouseid")
    private String spouseId;

    @Column(name = "employmentstatus")
    private String employmentStatus = "Working";

    @Column(name = "workinghrs")
    private int workingHrs;

    @Column(name = "workcontactnum")
    private String workContactNum;

    @Column(name = "employername")
    private String employerName;

    @Column(name = "workaddress")
    private String workAddress;

    @Column(name = "petalonehours")
    private int petAloneHours;

    @Column(name = "petcaretaker")
    private String petCareTaker = "Self";

    public Adopter() {

    }

    // === Getters and Setters ===

    public String getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(String adopterId) {
        this.adopterId = adopterId;
    }

    public String getAdopterName() {
        return adopterName;
    }

    public void setAdopterName(String adopterName) {
        this.adopterName = adopterName;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAdopterAddress() {
        return adopterAddressId;
    }

    public void setAdopterAddress(String adopterAddress) {
        this.adopterAddressId = adopterAddressId;
    }

    public String getSpouseId() {
        return spouseId;
    }

    public void setSpouseId(String spouseId) {
        this.spouseId = spouseId;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public int getWorkingHrs() {
        return workingHrs;
    }

    public void setWorkingHrs(int workingHrs) {
        this.workingHrs = workingHrs;
    }

    public String getWorkContactNum() {
        return workContactNum;
    }

    public void setWorkContactNum(String workContactNum) {
        this.workContactNum = workContactNum;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public int getPetAloneHours() {
        return petAloneHours;
    }

    public void setPetAloneHours(int petAloneHours) {
        this.petAloneHours = petAloneHours;
    }

    public String getPetCareTaker() {
        return petCareTaker;
    }

    public void setPetCareTaker(String petCareTaker) {
        this.petCareTaker = petCareTaker;
    }
}
