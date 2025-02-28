package org.exemple;


import org.example.DAO.CrudOperation.DishCrudOperation;
import org.example.Entity.Dish;
import org.example.Entity.DishIngredient;
import org.example.Entity.Ingredient;
import org.example.Entity.Unity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DishTest {
    DishCrudOperation subject = new DishCrudOperation();

    @Test
    public void get_ingredient_cost_test(){
        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Hot Dog");
        dish.setUnitPrice(15000);

        Ingredient saucisse = new Ingredient(1,"saucisse", LocalDateTime.now(),20, Unity.G);
        Ingredient huile = new Ingredient(2,"huile", LocalDateTime.now(),10000, Unity.L);
        Ingredient oeuf = new Ingredient(3,"oeuf", LocalDateTime.now(),1000, Unity.U);
        Ingredient pain = new Ingredient(4,"pain", LocalDateTime.now(),1000, Unity.U);

        DishIngredient dishIngredient1 = new DishIngredient(dish,saucisse,100,Unity.G);
        DishIngredient dishIngredient2 = new DishIngredient(dish,huile,0.15,Unity.L);
        DishIngredient dishIngredient3 = new DishIngredient(dish,oeuf,1,Unity.U);
        DishIngredient dishIngredient4 = new DishIngredient(dish,pain,1,Unity.U);

        dish.setIngredients(Arrays.asList(dishIngredient1,dishIngredient2,dishIngredient3,dishIngredient4));
        int expected = 5500;
        double actual = dish.getIngredientsCost();
        System.out.println(expected);
        System.out.println(actual);
        assertEquals(expected , actual ,0.01);

    }

    @Test
    public void get_dish_by_id_test(){
        Dish expected = DishHotDog();
        Dish actual = subject.findById(1);
        System.out.println(expected);
        System.out.println(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void get_all_dish_test(){
        List<Dish> expected = List.of(DishHotDog());
        List<Dish> dishes = subject.findAll(1,2);
        System.out.println(expected);
        System.out.println(dishes);
        assertEquals(expected, dishes);
    }

    @Test
    public void create_update_test(){
        List<Dish> expected = List.of(DishHotDog());
        List<Dish> actual = subject.saveAll(expected);
        System.out.println(actual);
        assertEquals(actual.containsAll(expected) ,true);
    }


    private Dish DishHotDog(){
        Dish dish = new Dish();
        dish.setId(1);
        dish.setName("Hot Dog");
        dish.setUnitPrice(15000);
        return dish;
    }


}
