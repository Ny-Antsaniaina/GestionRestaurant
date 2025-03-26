package org.example.Entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.DAO.CrudOperation.IngredientCrudOperation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@ToString
public class Ingredient {
    private String id;
    private String name;
    private LocalDateTime lastModifier;
    private double unitePrice;
    private Unity unity;


    public Ingredient(String id, String name, LocalDateTime lastModifier , double unitePrice, Unity unity) {
        this.id = id;
        this.name = name;
        this.lastModifier = lastModifier;
        this.unitePrice = unitePrice;
        this.unity=unity;
    }

    public Ingredient() {

    }



    public int getUnitPrice(LocalDateTime date) {
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();
        IngredientPrice price =  ingredientCrudOperation.getIngredientPrice(id, date);
        if (price == null) {
            return 0;
        }
        return price.getUnitPrice();
    }

    public QuantityStock getAvalaibleQuantity() {
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();
        List<Stock> stocks = ingredientCrudOperation.getAvailableStocks(id);
        double quantity = 0.0;

        for (Stock stock : stocks) {
            System.out.println("Processing stock: " + stock.getId() + ", Movement: " + stock.getMovement() + ", Quantity: " + stock.getQuantity());
            if (stock.getMovement() == Movement.EXIT) {
                System.out.println("Subtracting: " + stock.getQuantity());
                quantity -= stock.getQuantity();
            } else {
                System.out.println("Adding: " + stock.getQuantity());
                quantity += stock.getQuantity();
            }
        }

        System.out.println("Final available quantity: " + quantity);  // Log the final quantity
        return new QuantityStock(quantity, unity);
    }

    public QuantityStock getAvalaibleQuantity(LocalDateTime date) {
        IngredientCrudOperation ingredientCrudOperation = new IngredientCrudOperation();
        List<Stock> stocks = ingredientCrudOperation.getAvailableStocks(id, date);
        double quantity = 0.0;
        for (Stock stock : stocks) {
            if(stock.getMovement() == Movement.EXIT){
                quantity -= stock.getQuantity();
            }else {
                quantity += stock.getQuantity();
            }
        }
        System.out.println("Stocks for " + id + " at " + date + ": " + quantity);
        return new QuantityStock(quantity, unity);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Double.compare(unitePrice, that.unitePrice) == 0 && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(lastModifier, that.lastModifier) && unity == that.unity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastModifier, unitePrice, unity);
    }
}
