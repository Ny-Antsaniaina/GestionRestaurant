package org.example;

import org.example.DAO.CrudOperation.DishCrudOperation;
import org.example.DataBase.ConnectionDataBase;
import org.example.Entity.Dish;
import org.example.Entity.DishIngredient;
import org.example.Entity.Ingredient;
import org.example.Entity.Unity;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Main {
    static Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {

        try(ConnectionDataBase dataBase = new ConnectionDataBase();
            Connection connection = dataBase.getConnection()) {

            logger.info("Connected to database" + connection.toString());
            // Création des ingrédients
            Ingredient saucisse = new Ingredient(1, "Saucisse", LocalDateTime.now(), 20, Unity.G);
            Ingredient huile = new Ingredient(2, "Huile", LocalDateTime.now(), 10000, Unity.L);
            Ingredient oeuf = new Ingredient(3, "Oeuf", LocalDateTime.now(), 1000, Unity.U);
            Ingredient pain = new Ingredient(4, "Pain", LocalDateTime.now(), 1000, Unity.U);

            // Création du plat "Hot Dog"
            Dish hotDog = new Dish(1, "Hot dog", 15000);

            // Création des relations Dish-Ingredient
            List<DishIngredient> ingredients = new ArrayList<>();
            ingredients.add(new DishIngredient(hotDog, saucisse, 100, Unity.G));
            ingredients.add(new DishIngredient(hotDog, huile, 0.15, Unity.L));
            ingredients.add(new DishIngredient(hotDog, oeuf, 1, Unity.U));
            ingredients.add(new DishIngredient(hotDog, pain, 1, Unity.U));


            hotDog.setIngredients(ingredients);


            double totalCOst = hotDog.getIngredientsCost();
            logger.info("Cost total: " + totalCOst);


            DishCrudOperation dishCrudOperation = new DishCrudOperation();
            List<Dish> dishes = dishCrudOperation.findAll(1,10);
            for (Dish dish : dishes) {
                logger.info(dish.toString());
            }
            logger.info("Found " + dishes.size() + " dishes");

            Dish dish = dishCrudOperation.findById(1);
            logger.info("Found id : " + dish);

//            Dish newDish = new Dish();
//            newDish.setName("oeuf dur");
//            newDish.setUnitPrice(1200);
//
//            List<Dish> createdDishes = dishDAO.saveAndUpdate(List.of(newDish));
//            logger.info("Dishes created: " + createdDishes.size() + " dishes" + createdDishes);
//
//            newDish.setName("akoho");
//            List<Dish> updatedDishes = dishDAO.saveAndUpdate(List.of(newDish));
//            logger.info("Dishes updated: " + updatedDishes.size() + " dishes" + updatedDishes);

            dishCrudOperation.deleteById(3);
           logger.info("Deleted id : 3");


        }catch (SQLException e){
            logger.severe("SQL Exception: " + e.getMessage());
        }
    }
}
