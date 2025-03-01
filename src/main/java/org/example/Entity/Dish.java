package org.example.Entity;

import lombok.*;

import javax.management.ConstructorParameters;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Dish {
    private int id;
    private String name;
    private int unitPrice;
    private List<DishIngredient> ingredients;

    public Dish(int id, String name, int unitPrice) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.ingredients = new ArrayList<>();
    }

    public double getIngredientsPriceTotal() {
         return getIngredients().stream()
                .mapToDouble(ingredient -> ingredient.getIngredient().getUnitePrice() * ingredient.getRequiredQuantity())
                .sum();
    }

    public double getIngredientsCost(LocalDateTime date) {
        return getIngredients().stream()
                .mapToDouble(ingredient -> ingredient.getIngredient().getUnitePrice() * ingredient.getRequiredQuantity())
                .sum();
    }

    public double getGrossMargin(){
        return getUnitPrice() - getIngredientsCost(LocalDateTime.from(LocalDate.from(LocalDateTime.now())));
    }

    public double getGrossMargin(LocalDateTime date){
        return getUnitPrice() - getIngredientsCost(date);
    }

}