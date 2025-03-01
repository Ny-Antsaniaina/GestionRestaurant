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
@EqualsAndHashCode
public class Ingredient {
    private int id;
    private String name;
    private LocalDateTime lastModifier;
    private double unitePrice;
    private Unity unity;
    private Map<LocalDate, Double> priceHistory = new HashMap<>();
    private List<StockMovement> stockMovements = new ArrayList<>();

    public Ingredient(int id, String name, LocalDateTime lastModifier , double unitePrice, Unity unity) {
        this.id = id;
        this.name = name;
        this.lastModifier = lastModifier;
        this.unitePrice = unitePrice;
        this.unity=unity;
    }

    public Ingredient() {

    }


    public void addPrice(LocalDate date, double price) {
        priceHistory.put(date, price);
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
            if(stock.getMovement() == Movement.EXIT){
                quantity -= stock.getQuantity();
            }else {
                quantity += stock.getQuantity();
            }
        }
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
        return new QuantityStock(quantity, unity);
    }

}
