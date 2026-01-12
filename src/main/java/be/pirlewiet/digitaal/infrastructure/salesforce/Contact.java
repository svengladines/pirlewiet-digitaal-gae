package be.pirlewiet.digitaal.infrastructure.salesforce;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contact {

    @JsonProperty
    protected String id;
    @JsonProperty("Type__c")
    protected String type;
    @JsonProperty("FirstName")
    protected String firstName;
    @JsonProperty("LastName")
    protected String lastName;
    @JsonProperty("Birthdate")
    protected String birthDate;
    @JsonProperty("MobilePhone")
    protected String mobilePhone;
    @JsonProperty("Geslacht__c")
    protected String gender;

    // MailingAddress -> salesforce does not support composite field on Rest API
    @JsonProperty("MailingCity")
    protected String city;
    @JsonProperty("MailingPostalCode")
    protected String postalCode;
    @JsonProperty("MailingStreet")
    protected String street;

    public String id() {
        return id;
    }

    public Contact Id(String id) {
        this.id = id;
        return this;
    }

    public String firstName() {
        return firstName;
    }

    public Contact firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String lastName() {
        return lastName;
    }

    public Contact lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String birthDate() {
        return birthDate;
    }

    public Contact birthDate(String birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public String mobilePhone() {
        return mobilePhone;
    }

    public Contact mobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
        return this;
    }

    public String gender() {
        return gender;
    }

    public Contact gender(String gender) {
        this.gender = gender;
        return this;
    }

    public String city() {
        return city;
    }

    public Contact city(String city) {
        this.city = city;
        return this;
    }

    public String postalCode() {
        return postalCode;
    }

    public Contact postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String street() {
        return street;
    }

    public Contact street(String street) {
        this.street = street;
        return this;
    }

    public String type() {
        return type;
    }
    public Contact type(String type) {
        this.type = type;
        return this;
    }

}
