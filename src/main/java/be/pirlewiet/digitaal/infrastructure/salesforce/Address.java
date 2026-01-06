package be.pirlewiet.digitaal.infrastructure.salesforce;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 "MailingAddress": {
 "city": "Geel",
 "country": "Belgium",
 "geocodeAccuracy": null,
 "latitude": null,
 "longitude": null,
 "postalCode": "2440",
 "state": "Antwerpen",
 "street": "Dorpsplein 5"
 }

 */

public class Address {

    @JsonProperty("city")
    protected String city;
    @JsonProperty("postalCode")
    protected String postalCode;
    @JsonProperty("street")
    protected String street;

    public String city() {
        return city;
    }

    public void city(String city) {
        this.city = city;
    }

    public String postalCode() {
        return postalCode;
    }

    public void postalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String street() {
        return street;
    }

    public void street(String street) {
        this.street = street;
    }
}
