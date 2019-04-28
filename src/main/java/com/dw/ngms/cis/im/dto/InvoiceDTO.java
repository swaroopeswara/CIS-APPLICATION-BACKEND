package com.dw.ngms.cis.im.dto;

import java.io.Serializable;

/**
 * Created by ragava on 27/06/17.
 */

public class InvoiceDTO implements Serializable{


    private static final long serialVersionUID = 4675671622777042755L;

    private String id;

    private String fullName;
    private String orgnization;
    private String telephone;
    private String postalAddress;
    private String email;
    private String moible;
    private String requestNumber;
    private String requestType;
    private String amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOrgnization() {
        return orgnization;
    }

    public void setOrgnization(String orgnization) {
        this.orgnization = orgnization;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMoible() {
        return moible;
    }

    public void setMoible(String moible) {
        this.moible = moible;
    }

    public String getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(String requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
