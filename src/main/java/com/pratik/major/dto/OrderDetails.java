package com.pratik.major.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDetails {
    private ShippingDetails shippingDetails;
    private BillingDetails billingDetails;


}
