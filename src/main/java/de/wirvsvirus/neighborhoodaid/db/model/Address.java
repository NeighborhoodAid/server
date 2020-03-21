package de.wirvsvirus.neighborhoodaid.db.model;

public class Address {

    private final Integer postcode;
    private final String number;
    private final String street;

    public Address(Integer postcode, String number, String street) {
        this.postcode = postcode;
        this.number = number;
        this.street = street;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public String getNumber(){
        return number;
    }

    public String getStreet(){
        return street;
    }

}
