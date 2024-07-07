package com.pratik.major.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingDetails {
    private String firstName;
    private String lastName;
    private String country;
    private String houseNumberAndStreetName;
    private String apartment;
    private Integer postalCode;
    private String city;
    private String phoneNumber;
    private String email;
    private String additionalInfo;

    @Override
    public String toString() {
        return "ShippingDetails{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", houseNumberAndStreetName='" + houseNumberAndStreetName + '\'' +
                ", apartment='" + apartment + '\'' +
                ", postalCode=" + postalCode +
                ", city='" + city + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                '}';
    }
}
