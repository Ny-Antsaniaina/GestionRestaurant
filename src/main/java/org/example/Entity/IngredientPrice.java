package org.example.Entity;

import lombok.*;

import java.time.LocalDateTime;

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

    public int getUnitPrice() {
        return unitPrice;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
