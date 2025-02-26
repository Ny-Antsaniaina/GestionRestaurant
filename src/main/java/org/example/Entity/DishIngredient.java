package org.example.Entity;


import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DishIngredient {
    private Dish dish;
    private Ingredient ingredient;
    private double requiredQuantity;
    private Unity  unity;

    public DishIngredient(Dish dish, Ingredient ingredient, double requiredQuantity, Unity unity) {
        this.dish = dish;
        this.ingredient = ingredient;
        this.requiredQuantity = requiredQuantity;
        this.unity = unity;
    }


}
