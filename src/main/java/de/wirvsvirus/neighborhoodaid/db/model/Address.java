package de.wirvsvirus.neighborhoodaid.db.model;

public class Address {

    private final String street;
    private final String number;
    private final Integer postcode;

    public Address(String street, String number, Integer postcode) {
        this.street = street;
        this.number = number;
        this.postcode = postcode;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public Integer getPostcode() {
        return postcode;
    }


}
