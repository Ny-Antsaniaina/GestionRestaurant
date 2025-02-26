package org.example.DAO.CrudOperation;

import org.example.DAO.Mapper.EnumMapper;
import org.example.DataBase.ConnectionDataBase;
import org.example.Entity.Ingredient;
import org.example.Entity.IngredientPrice;
import org.example.Entity.Unity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredientCrudOperation implements CrudOperation<Ingredient> {
    ConnectionDataBase dataSource = new ConnectionDataBase();
    @Override
    public List<Ingredient> findAll(int page, int pageSize)  {
        String sql = "SELECT id , name , unit FROM ingredient LIMIT ? OFFSET ?";
        
        if (page < 0){
            throw new IllegalArgumentException("page cannot be less than zero");
        }else {
            List<Ingredient> list = new ArrayList<>();
            try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);){
                statement.setInt(1,pageSize);
                statement.setInt(2,pageSize * (page - 1));
                try(ResultSet resultSet = statement.executeQuery();){
                    while (resultSet.next()){
                        IngredientPrice ingredientPrice = getIngredientPrice(resultSet.getInt("id"));
                        Ingredient ingredient = new Ingredient();
                        ingredient.setId(resultSet.getInt("id"));
                        ingredient.setName(resultSet.getString("name"));
                        ingredient.setLastModifier(ingredientPrice.getDate());
                        ingredient.setUnitePrice(ingredientPrice.getUnitPrice());
                        ingredient.setUnity(Unity.valueOf(resultSet.getString("unit")));
                        list.add(ingredient);
                    }

                    return list;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Ingredient findById(int id) {
        String sql = "SELECT id , name , unit FROM ingredient WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,id);
            try(ResultSet rs = statement.executeQuery()){
                if (rs.next()){
                    IngredientPrice ingredientPrice = getIngredientPrice(rs.getInt("id"));
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(rs.getInt("id"));
                    ingredient.setName(rs.getString("name"));
                    ingredient.setLastModifier(ingredientPrice.getDate());
                    ingredient.setUnitePrice(ingredientPrice.getUnitPrice());
                    ingredient.setUnity(Unity.valueOf(rs.getString("unit")));
                    return ingredient;
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public List<Ingredient> saveAndUpdate(List<Ingredient> list) {
        return List.of();
    }

    @Override
    public void deleteById(int id) {

    }


    public List<Ingredient> getFitersIngredientPrice(List<Filter> filters , String sortBy , String sortOrder , int page, int pageSize) {
        List<Ingredient> listIngredients = new ArrayList<>();
        StringBuilder sql = new StringBuilder("Select id , name , unit from ingredient Where 1=1");

        for (Filter filter : filters) {
            if (filter.getOperator().equals("BETWEEN")) {
                sql.append(" AND ").append(filter.getColumn()).append(" BETWEEN ? AND ?");
            }else {
                sql.append(" AND ").append(filter.getColumn()).append("").append(filter.getOperator()).append("?");
            }

            if (sortBy != null) {
                sql.append("ORDER BY").append(sortBy).append(" ").append((sortOrder != null ? sortOrder : "ASC"));
            }

            sql.append(" LIMIT ? OFFSET ?");

            try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql.toString())){
                int i = 1;
                for (Filter f : filters) {
                    if (filter.getOperator().equals("BETWEEN")) {
                        statement.setObject(i++,f.getValueIntervalMin());
                        statement.setObject(i++,f.getValueIntervalMax());
                    }else {
                        statement.setObject(i++,f.getValueIntervalMin());
                    }

                    statement.setInt(i++,pageSize);
                    statement.setInt(i++,pageSize*(page - 1 ));

                    try(ResultSet resultSet = statement.executeQuery()){
                        while (resultSet.next()){
                            IngredientPrice ingredientPrice = getIngredientPrice(resultSet.getInt("id"));
                            Ingredient ingredient = new Ingredient();
                            ingredient.setId(resultSet.getInt("id"));
                            ingredient.setName(resultSet.getString("name"));
                            ingredient.setLastModifier(ingredientPrice.getDate());
                            ingredient.setUnitePrice(ingredientPrice.getUnitPrice());
                            ingredient.setUnity(Unity.valueOf(resultSet.getString("unit")));
                            listIngredients.add(ingredient);
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return listIngredients;
    }

    public IngredientPrice getIngredientPrice(int id) {
        String sql = "SELECT price , date FROM ingredient_price_history WHERE id = ?";

        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,id);
            try(ResultSet rs = statement.executeQuery()){
                if (rs.next()){
                    IngredientPrice ingredientPrice = new IngredientPrice();
                    ingredientPrice.setUnitPrice(rs.getInt("price"));
                    ingredientPrice.setDate(rs.getTimestamp("date").toLocalDateTime());

                    return ingredientPrice;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public int getPriceID(int id) {
        String sql = "SELECT id FROM ingredient_price WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,id);
            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    id = resultSet.getInt("id");

                }
                return id;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
