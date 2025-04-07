package org.example.DAO.CrudOperation;

import org.example.Entity.*;
import org.example.db.ConnectionDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommonCrudOperations {
    private ConnectionDataBase dataSource = new ConnectionDataBase();

    public Ingredient findIngredientById(String id) {
        String sql = "SELECT id, name, unity FROM ingredient WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = getIngredientPrice(resultSet.getString("id"));
                    Ingredient ingredient = new Ingredient(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            ingredientPrice.getDate(),
                            ingredientPrice.getUnitPrice(),
                            Unity.valueOf(resultSet.getString("unity"))
                    );
                    return ingredient;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Ingredient findIngredientById(String id, LocalDateTime dateTime) {
        String sql = "SELECT id, name, unity FROM ingredient WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = getIngredientPrice(resultSet.getString("id"), dateTime);
                    Ingredient ingredient = new Ingredient(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            ingredientPrice.getDate(),
                            ingredientPrice.getUnitPrice(),
                            Unity.valueOf(resultSet.getString("unity"))
                    );
                    return ingredient;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public IngredientPrice getIngredientPrice(String ingredient_id) {
        String sql = "SELECT price, date FROM ingredient_price_history WHERE ingredient_id = ? AND date <= CURRENT_DATE ORDER BY date DESC LIMIT 1";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = new IngredientPrice(
                            resultSet.getInt("price"),
                            resultSet.getTimestamp("date").toLocalDateTime()
                    );
                    return ingredientPrice;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public IngredientPrice getIngredientPrice(String ingredient_id, LocalDateTime dateTime) {
        String sql = "SELECT price, date FROM ingredient_price_history WHERE ingredient_id = ? AND date <= ? ORDER BY date DESC LIMIT 1";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, ingredient_id);
            statement.setTimestamp(2, Timestamp.valueOf(dateTime));
            try(ResultSet resultSet = statement.executeQuery();){
                if(resultSet.next()){
                    IngredientPrice ingredientPrice = new IngredientPrice(
                            resultSet.getInt("price"),
                            resultSet.getTimestamp("date").toLocalDateTime()
                    );
                    return ingredientPrice;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DishIngredient> getDishIngredient(String id_dish) {
        List<DishIngredient> dishIngredientList = new ArrayList<>();
        String sql = "SELECT ingredient_id, quantity, unity FROM dish_ingredient WHERE dish_id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id_dish);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    Ingredient ingredient = findIngredientById(resultSet.getString("ingredient_id"));
                    DishIngredient dishIngredient = new DishIngredient(
                            ingredient,
                            resultSet.getDouble("quantity"),
                            Unity.valueOf(resultSet.getString("unity"))
                    );
                    dishIngredientList.add(dishIngredient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishIngredientList;
    }

    public List<DishIngredient> getDishIngredient(String id_dish, LocalDateTime dateTime) {
        List<DishIngredient> dishIngredientList = new ArrayList<>();
        String sql = "SELECT ingredient_id, quantity, unity FROM dish_ingredient WHERE dish_id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id_dish);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    Ingredient ingredient = findIngredientById(resultSet.getString("ingredient_id"), dateTime);
                    DishIngredient dishIngredient = new DishIngredient(
                            ingredient,
                            resultSet.getDouble("quantity"),
                            Unity.valueOf(resultSet.getString("unity"))
                    );
                    dishIngredientList.add(dishIngredient);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishIngredientList;
    }

    public Dish findDishById(String dishId) {
        String sql = "SELECT id, name, unit_price FROM dish WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, dishId);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    Dish dish = new Dish(
                            resultSet.getString("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("unit_price"),
                            getDishIngredient(dishId)
                    );
                    return dish;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<DishOrderStatusHistory> findByDishOrderId(String id) {
        List<DishOrderStatusHistory> dishOrderStatusHistoryList = new ArrayList<>();
        String sql = "SELECT id, status, changed_at FROM dish_order_status_history WHERE dish_order_id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    DishOrderStatusHistory dishOrderStatusHistory = new DishOrderStatusHistory(
                            resultSet.getString("id"),
                            StatusEnum.valueOf(resultSet.getString("status")),
                            resultSet.getTimestamp("changed_at").toLocalDateTime()
                    );
                    dishOrderStatusHistoryList.add(dishOrderStatusHistory);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishOrderStatusHistoryList;
    }
}