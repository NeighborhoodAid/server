package de.wirvsvirus.neighborhoodaid.db.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Address {

    private final String street;
    private final String houseNumber;
    private final String postcode;
    private final String city;
    private final String longitude;
    private final String latitude;

    @JsonCreator
    public Address(@JsonProperty("street") String street, @JsonProperty("houseNumber") String houseNumber,
                   @JsonProperty("postcode") String postcode, @JsonProperty("city") String city,
                   @JsonProperty("longitude") String longitude, @JsonProperty("latitude") String latitude) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public Address withGeoLocation(final String longitude, final String latitude) {
        return new Address(this.street, this.houseNumber, this.postcode, this.city, longitude, latitude);
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", postcode='" + postcode + '\'' +
                ", city='" + city + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                '}';
    }
}
