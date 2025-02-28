package org.example.DAO.CrudOperation;

import org.example.DAO.Mapper.EnumMapper;
import org.example.DataBase.ConnectionDataBase;
import org.example.Entity.Dish;
import org.example.Entity.DishIngredient;
import org.example.Entity.Ingredient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DishCrudOperation implements CrudOperation<Dish> {
    private ConnectionDataBase dataSource = new ConnectionDataBase();
    Logger logger = Logger.getLogger(DishCrudOperation.class.getName());

    @Override
    public List<Dish> findAll(int page, int pageSize) {
        if(page < 1){
            logger.warning("page is less than 1");
        }
        String sql = "select d.id , d.name , d.sale_price from dish d order by d.id limit  ? offset ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ){
            logger.info("Executing query" + sql);
            statement.setInt(1,pageSize);
            statement.setInt(2,pageSize * (page - 1));

            ResultSet resultSet = statement.executeQuery();
            List<Dish> list = new ArrayList<>();
            while(resultSet.next()){
                Dish d = new Dish();
                d.setId(resultSet.getInt("id"));
                d.setName(resultSet.getString("name"));
                d.setUnitPrice(resultSet.getInt("sale_price"));
                list.add(d);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Dish findById(int id) {
        String sql = "select d.id , d.name , d.sale_price from dish d where d.id=?";

        Dish d = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            logger.info("Executing query" + sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                d = new Dish();
                d.setId(resultSet.getInt("id"));
                d.setName(resultSet.getString("name"));
                d.setUnitPrice(resultSet.getInt("sale_price"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return d;
    }

    @Override
    public List<Dish> saveAll(List<Dish> dishes) {
        List<Dish> list = new ArrayList<>();

        String sql = "insert into dish (name,sale_price) values (?,?) on conflict (name) do update set name = EXCLUDED.name,sale_price = EXCLUDED.sale_price";

        try(Connection connection = dataSource.getConnection()){
            logger.info("Executing query" + sql);
            for (Dish d : dishes) {
                try(PreparedStatement statement = connection.prepareStatement(sql)){
                    statement.setString(1,d.getName());
                    statement.setInt(2,d.getUnitPrice());
                    statement.executeUpdate();
                    list.add(d);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            logger.info( list.size() + " dishes have been saved");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public Dish getListIngredientInDish(int id) {
        Dish dish = null;
        List<DishIngredient> ingredients = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            String dishSQL = "SELECT id, name, sale_price FROM dish WHERE id=?";
            PreparedStatement dishStatement = connection.prepareStatement(dishSQL);
            dishStatement.setInt(1, id);
            ResultSet resultSet = dishStatement.executeQuery();

            if (resultSet.next()) {
                dish = new Dish();
                dish.setId(resultSet.getInt("id"));
                dish.setName(resultSet.getString("name"));
                dish.setUnitPrice(resultSet.getInt("sale_price"));
            } else {
                return null;
            }

            String dishIngredientSQL = "SELECT di.ingredient_id, di.quantity, di.unit, " +
                    "i.name, i.unit_price " +
                    "FROM dish_ingredient di " +
                    "JOIN ingredient i ON di.ingredient_id = i.id " +
                    "WHERE di.dish_id = ?";
            PreparedStatement ingredientStatement = connection.prepareStatement(dishIngredientSQL);
            ingredientStatement.setInt(1, id);
            ResultSet ingredientResultSet = ingredientStatement.executeQuery();

            while (ingredientResultSet.next()) {
                Ingredient ingredient = new Ingredient(
                        ingredientResultSet.getInt("ingredient_id"),
                        ingredientResultSet.getString("name"),
                        null,
                        ingredientResultSet.getDouble("unit_price"),
                        EnumMapper.topicmaper(ingredientResultSet.getString("unit"))
                );

                DishIngredient dishIngredient = new DishIngredient(
                        dish,
                        ingredient,
                        ingredientResultSet.getDouble("quantity"),
                        EnumMapper.topicmaper(ingredientResultSet.getString("unit"))
                );

                ingredients.add(dishIngredient);
            }

            dish.setIngredients(ingredients);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching dish with ingredients", e);
        }

        return dish;
    }

    @Override
     public void deleteById(int id) {
         String sql = "delete from dish where id=?";

         try (Connection connection = dataSource.getConnection();
              PreparedStatement statement = connection.prepareStatement(sql)) {
             logger.info("Executing query: " + sql);
             statement.setInt(1, id);
             int rowsAffected = statement.executeUpdate();

             if (rowsAffected > 0) {
                 logger.info("Dish with id " + id + " has been deleted.");
             } else {
                 logger.warning("No dish found with id " + id + ".");
             }
         } catch (SQLException e) {
             throw new RuntimeException("Error deleting dish with id " + id, e);
         }
     }

}
