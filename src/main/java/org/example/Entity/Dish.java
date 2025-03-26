package org.example.Entity;

import lombok.*;
import org.example.DAO.CrudOperation.IngredientCrudOperation;

import javax.management.ConstructorParameters;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Dish {
    private String id;
    private String name;
    private int unitPrice;
    private List<DishIngredient> ingredients = new ArrayList<>();

    public Dish(String id, String name, int unitPrice , List<DishIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.unitPrice = unitPrice;
        this.ingredients = ingredients;
    }

    public double getIngredientsPriceTotal() {
        return getIngredients().stream()
                .mapToDouble(ingredient -> ingredient.getIngredient().getUnitePrice() * ingredient.getRequiredQuantity())
                .sum();
    }

    public double getIngredientsPriceTotal(LocalDateTime date) {
        return getIngredients().stream()
                .mapToDouble(ingredient -> ingredient.getIngredient().getUnitPrice(date) * ingredient.getRequiredQuantity())
                .sum();
    }

    public double getGrossMargin(){
        return getUnitPrice() - getIngredientsPriceTotal(LocalDateTime.now());
    }

    public double getGrossMargin(LocalDateTime date){
        return getUnitPrice() - getIngredientsPriceTotal(date);
    }


    public double getAvailableQuantity() {
        List<Double> resultList = new ArrayList<>();

        for (DishIngredient ingredient : getIngredients()) {
            resultList.add(
                    ingredient.getIngredient().getAvalaibleQuantity().getQuantity() / ingredient.getRequiredQuantity()
            );
        }
        Collections.sort(resultList);
        if(resultList.isEmpty()) return 0.0;
        return resultList.get(0);
    }

    public double getAvailableQuantity(LocalDateTime date) {
        List<Double> resultList = new ArrayList<>();

        for (DishIngredient ingredient : getIngredients()) {
            resultList.add(
                    ingredient.getIngredient().getAvalaibleQuantity(date).getQuantity() / ingredient.getRequiredQuantity()
            );
        }
        Collections.sort(resultList);
        if(resultList.isEmpty()) return 0.0;
        return resultList.get(0);
    }
}