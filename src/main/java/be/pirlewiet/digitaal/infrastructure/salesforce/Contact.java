package be.pirlewiet.digitaal.infrastructure.salesforce;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contact {

    @JsonProperty
    protected String id;
    @JsonProperty("FirstName")
    protected String firstName;
    @JsonProperty("LastName")
    protected String lastName;
    @JsonProperty("Birthdate")
    protected String birthDate;
    @JsonProperty("MobilePhone")
    protected String mobilePhone;
    //@JsonProperty("Geslacht")
    protected String geslacht;

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

    public String geslacht() {
        return geslacht;
    }

    public Contact geslacht(String geslacht) {
        this.geslacht = geslacht;
        return this;
    }
}
