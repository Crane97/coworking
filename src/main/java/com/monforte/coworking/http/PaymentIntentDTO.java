package com.monforte.coworking.http;

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
}
