package org.exemple;

import org.example.DAO.CrudOperation.Filter;
import org.example.DAO.CrudOperation.IngredientCrudOperation;
import org.example.Entity.Ingredient;
import org.example.Entity.Unity;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IngredientsTest {
    IngredientCrudOperation subject = new IngredientCrudOperation();
    @Test
    public void test_get_all_ingredients() {
    List<Ingredient> expected = List.of(
        Saucisse(),
            Huile(),
            Oeuf()
    );
    List<Ingredient> actual = subject.findAll(1,3);
        System.out.println(actual);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void test_find_ingredient_by_id() {
        Ingredient excepted = subject.findById(1);
        Ingredient actual = Saucisse();
        System.out.println(actual);
        System.out.println(excepted);
        assertEquals(excepted, actual);
    }

    @Test
    public void test_filter_ingredients_by_name() {
     List<Filter> filters = new ArrayList<>();
     filters.add(new Filter("name" , "Saucisse","=" ));
     List<Ingredient> expected = List.of(Saucisse());
     List<Ingredient> actual = subject.getFitersIngredient(filters,null,null,1,2);
     System.out.println(actual);
     System.out.println(expected);
     assertEquals(expected, actual);
    }

    @Test
    public void test_find_filter_ingredients_by_name() {
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("name" , "e","ILIKE" ));
        List<Ingredient> expected = List.of(Saucisse(),Huile(),Oeuf());
        List<Ingredient> actual = subject.getFitersIngredient(filters,null,null,1,3);
        System.out.println(actual);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void test_filter_ingredients_by_interval_price() {
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("unit_price",1,1000));
        List<Ingredient> expected = List.of(Saucisse(),Oeuf(),Pain());
        List<Ingredient> actual = subject.getFitersIngredient(filters,"unit_price","ASC",1,3);
        System.out.println(actual);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void test_fiter_ingredients_by_interval_date() {
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("last_modified", LocalDate.of(2024,12,01) , LocalDate.of(2025,02,02)));
        List<Ingredient> expected = List.of(Saucisse(),Huile(),Oeuf(),Pain());
        List<Ingredient> actual = subject.getFitersIngredient(filters,null,null,1,5);
        System.out.println(actual);
        System.out.println(expected);
        assertEquals(expected, actual);
    }

    public Ingredient Saucisse(){
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);
        ingredient.setName("Saucisse");
        ingredient.setLastModifier(LocalDateTime.of(2025,01,01,0,0));
        ingredient.setUnitePrice(20);
        ingredient.setUnity(Unity.G);
        return ingredient;
    }

    public Ingredient Huile(){
        Ingredient ingredient = new Ingredient();
        ingredient.setId(2);
        ingredient.setName("Huile");
        ingredient.setLastModifier(LocalDateTime.of(2025,01,01,0,0));
        ingredient.setUnitePrice(10000);
        ingredient.setUnity(Unity.L);
        return ingredient;
    }

    public Ingredient Oeuf(){
        Ingredient ingredient = new Ingredient();
        ingredient.setId(3);
        ingredient.setName("Oeuf");
        ingredient.setLastModifier(LocalDateTime.of(2025,01,01,0,0));
        ingredient.setUnitePrice(1000);
        ingredient.setUnity(Unity.U);
        return ingredient;
    }

    public Ingredient Pain(){
        Ingredient ingredient = new Ingredient();
        ingredient.setId(4);
        ingredient.setName("Pain");
        ingredient.setLastModifier(LocalDateTime.of(2025,01,01,0,0));
        ingredient.setUnitePrice(1000);
        ingredient.setUnity(Unity.U);
        return ingredient;
    }
}
