package org.example.DAO.CrudOperation;

import org.example.Entity.*;
import org.example.db.ConnectionDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishOrderCrudOperation implements CrudOperation<DishOrder> {
    private ConnectionDataBase dataSource = new ConnectionDataBase();
    private CommonCrudOperations commonCrudOperations = new CommonCrudOperations();
    @Override
    public List<DishOrder> findAll(int page, int pageSize) throws SQLException {
        if(page > 0){
            throw new IllegalArgumentException("page must be greater than 0 but is "+page);
        }else{
            List<DishOrder> dishOrders = new ArrayList<>();
            String sql = "SELECT id, dish_id, quantity FROM dish_order LIMIT ? OFFSET ?";
            try(Connection con = dataSource.getConnection();
                PreparedStatement statement = con.prepareStatement(sql)){
                statement.setInt(1, pageSize);
                statement.setInt(2, pageSize * (page - 1));
                try(ResultSet resultSet = statement.executeQuery()){
                    while(resultSet.next()){
                        DishOrder dishOrder = new DishOrder(
                                resultSet.getString("id"),
                                commonCrudOperations.findDishById(resultSet.getString("dish_id")),
                                resultSet.getInt("quantity"),
                               commonCrudOperations.findByDishOrderId(resultSet.getString("id"))

                        );
                        dishOrders.add(dishOrder);
                    }
                    return dishOrders;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public DishOrder findById(String id) {
        DishOrder dishOrder = null;
        String sql = "SELECT id, dish_id, quantity FROM dish_order WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    dishOrder = new DishOrder(
                            resultSet.getString("id"),
                            commonCrudOperations.findDishById(resultSet.getString("dish_id")),
                            resultSet.getInt("quantity"),
                            commonCrudOperations.findByDishOrderId(resultSet.getString("id"))
                    );
                }
            }
            return dishOrder;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DishOrder> saveAll(List<DishOrder> list) {throw new RuntimeException("not implemented");
    }

    @Override
    public void deleteById(String id) {

    }
}
