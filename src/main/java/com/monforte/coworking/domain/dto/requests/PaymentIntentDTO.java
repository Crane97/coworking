package com.monforte.coworking.domain.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentIntentDTO {
    public enum Currency{
        USD,EUR;
    }

    private String description;
    private int amount;
    private Currency currency;
    private String token;
    private String payment_method;
}
