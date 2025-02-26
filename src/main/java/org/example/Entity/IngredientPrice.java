package org.example.Entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class IngredientPrice {
    private int unitPrice;
    private LocalDateTime date;

    public IngredientPrice(int unitPrice, LocalDateTime date) {
        this.unitPrice = unitPrice;
        this.date = date;
    }

    public IngredientPrice() {

    }
}
