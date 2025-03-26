package org.exemple;


import org.example.DAO.CrudOperation.DishCrudOperation;
import org.example.Entity.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DishTest {
    DishCrudOperation subject = new DishCrudOperation();
    @Test
    public void get_price_total_ingredients_in_dish_without_date() {
        double expected = 5500;
        Dish dish = subject.findById("1");

        double actual = dish.getIngredientsPriceTotal();

        System.out.println(expected);
        System.out.println(actual);

        assertEquals(expected, actual);
    }

    @Test void get_price_total_ingredient_in_dish_with_date(){
        double expected = 0;
        Dish dish = subject.findById("1");

        double actual = dish.getIngredientsPriceTotal(LocalDateTime.of(1999,01,01,0,0,0));

        System.out.println(expected);
        System.out.println(actual);
        assertEquals(expected, actual);

    }

    @Test
    public void get_gross_margin_test_without_date(){
        double expected = 9500;
        Dish dish = subject.findById("1");

        double actual = dish.getGrossMargin();

        assertEquals(expected, actual);;
    }

    @Test
    public void get_gross_margin_test_with_date(){
        double expected = 9500;
        Dish dish = subject.findById("1");

        double actual = dish.getGrossMargin(LocalDateTime.of(2026,01,01,0,0,0));

        System.out.println(actual);
        System.out.println(expected);
        assertEquals(expected, actual);;
    }

    @Test
    public void get_dish_by_id_test(){
        Dish expected = DishHotDog();
        Dish actual = subject.findById("1");
        System.out.println(expected);
        System.out.println(actual);
        assertEquals(expected, actual);
    }

//    @Test
//    public void get_all_dish_test(){
//        List<Dish> expected = List.of(DishHotDog());
//        List<Dish> dishes = subject.findAll(1,2);
//        System.out.println(expected);
//        System.out.println(dishes);
//        assertEquals(expected, dishes);
//    }

    @Test
    public void create_update_test(){
        List<Dish> expected = List.of(DishHotDog());
        List<Dish> actual = subject.saveAll(expected);
        System.out.println(actual);
        assertEquals(actual.containsAll(expected) ,true);
    }
    private Dish DishHotDog(){
        Dish dish = new Dish();
        dish.setId("1");
        dish.setName("Hot Dog");
        dish.setUnitPrice(15000);

        dish.setIngredients(List.of(
                SaucisseIngredientWithQuantity(),
                HuileIngredientWithQuantity(),
                OeufIngredientWithQuantity(),
                PainIngredientWithQuantity()
        ));

        return dish;
    }


    public Ingredient SaucisseIngredient(){
        return createIngredient("1","Saucisse", LocalDateTime.of(2025,1,1,0,0), 20.0, Unity.G);
    }

    public Ingredient HuileIngredient(){
        return createIngredient("2","Huile", LocalDateTime.of(2025,1,1,0,0), 10000.0, Unity.L);
    }

    public Ingredient OeufIngredient(){
        return createIngredient("3","Oeuf", LocalDateTime.of(2025,1,1,0,0), 1000.0, Unity.U);
    }

    public Ingredient PainIngredient(){
        return createIngredient("4","Pain", LocalDateTime.of(2025,1,1,0,0), 1000.0, Unity.U);
    }


    public DishIngredient SaucisseIngredientWithQuantity(){
        return createIngredientWithQuantity(SaucisseIngredient(), 100, Unity.G);
    }

    public DishIngredient HuileIngredientWithQuantity(){
        return createIngredientWithQuantity(HuileIngredient(), 0.15, Unity.L);
    }

    public DishIngredient OeufIngredientWithQuantity(){  // Correction du nom
        return createIngredientWithQuantity(OeufIngredient(), 1, Unity.U);
    }

    public DishIngredient PainIngredientWithQuantity(){
        return createIngredientWithQuantity(PainIngredient(), 1, Unity.U);
    }


    public Ingredient createIngredient(String id, String name, LocalDateTime updateDatetime, Double unitPrice, Unity unity) {
        return new Ingredient(id, name, updateDatetime, unitPrice, unity);
    }


    public DishIngredient createIngredientWithQuantity(Ingredient ingredient, double quantity, Unity unity) {
        return new DishIngredient(ingredient, quantity, unity);
    }

    public QuantityStock SaucisseStock(){
        return createQuantityStock(10000.0, Unity.G);
    }

    public QuantityStock HuileStock(){
        return createQuantityStock(20.0, Unity.L);
    }

    public QuantityStock OeufStock(){
        return createQuantityStock(80.0, Unity.U);
    }

    public QuantityStock PainStock(){
        return createQuantityStock(30.0, Unity.U);
    }

    public QuantityStock createQuantityStock(Double quantity, Unity unity) {
        return new QuantityStock(quantity,unity);
    }
}
