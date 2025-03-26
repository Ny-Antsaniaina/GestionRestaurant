package org.example.Entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Stock {
    private String id;
    private Movement movement;
    private Double quantity;
    private Unity unity;
    private LocalDateTime date;
    private Ingredient ingredient;

    public Stock(String id, Movement movement, Double quantity, Unity unity, LocalDateTime date, Ingredient ingredient) {
        this.id = id;
        this.movement = movement;
        this.quantity = quantity;
        this.unity = unity;
        this.date = date;
        this.ingredient = ingredient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Unity getUnity() {
        return unity;
    }

    public void setUnity(Unity unity) {
        this.unity = unity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(id, stock.id) && movement == stock.movement && Objects.equals(quantity, stock.quantity) && unity == stock.unity && Objects.equals(date, stock.date) && Objects.equals(ingredient, stock.ingredient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, movement, quantity, unity, date, ingredient);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id='" + id + '\'' +
                ", movement=" + movement +
                ", quantity=" + quantity +
                ", unity=" + unity +
                ", date=" + date +
                ", ingredient=" + ingredient +
                '}';
    }
}
