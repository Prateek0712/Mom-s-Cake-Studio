package com.pratik.major.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BillingDetails {
    private String currency;
    private String method;
    private String amount;
    private String description;

    @Override
    public String toString() {
        return "BillingDetails{" +
                "currency='" + currency + '\'' +
                ", method='" + method + '\'' +
                ", amount='" + amount + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
