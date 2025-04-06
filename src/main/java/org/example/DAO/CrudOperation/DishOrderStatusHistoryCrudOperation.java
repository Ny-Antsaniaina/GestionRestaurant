package org.example.DAO.CrudOperation;

import org.example.Entity.DishOrderStatusHistory;
import org.example.Entity.StatusEnum;
import org.example.db.ConnectionDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DishOrderStatusHistoryCrudOperation implements CrudOperation<DishOrderStatusHistory> {
    private ConnectionDataBase dataSource = new ConnectionDataBase();



    @Override
    public List<DishOrderStatusHistory> findAll(int page, int pageSize) throws SQLException {
        if (page < 1) {
            throw new IllegalArgumentException("page must be greater than 0 but is " + page);
        }else {
            List<DishOrderStatusHistory> dishOrderStatusHistoryList = new ArrayList<>();
            String sql = "SELECT id, status, date FROM dish_order_status_history ORDER BY id ASC LIMIT ? OFFSET ?";
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setInt(1, pageSize);
                statement.setInt(2, pageSize * (page - 1));
                try(ResultSet resultSet = statement.executeQuery()){
                    while (resultSet.next()) {
                        DishOrderStatusHistory dishOrderStatusHistory = new DishOrderStatusHistory(
                                resultSet.getString("id"),
                                StatusEnum.valueOf(resultSet.getString("status")),
                                resultSet.getTimestamp("date").toLocalDateTime()
                        );
                        dishOrderStatusHistoryList.add(dishOrderStatusHistory);
                    }
                }
                return dishOrderStatusHistoryList;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public DishOrderStatusHistory findById(String id) {
        DishOrderStatusHistory dishOrderStatusHistory = null;
        String sql = "SELECT id, status, date FROM dish_order_status_history WHERE id = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    dishOrderStatusHistory = new DishOrderStatusHistory(
                            resultSet.getString("id"),
                            StatusEnum.valueOf(resultSet.getString("status")),
                            resultSet.getTimestamp("date").toLocalDateTime()
                    );
                }
            }
            return dishOrderStatusHistory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DishOrderStatusHistory> saveAll(List<DishOrderStatusHistory> list) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteById(String id) {

    }

    public List<DishOrderStatusHistory> findByDishOrderId(String dishOrderId) {
        List<DishOrderStatusHistory> dishOrderStatusHistoryList = new ArrayList<>();
        String sql = "SELECT id, status, date FROM dish_order_status_history WHERE id_dish_order = ?";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, dishOrderId);
            try(ResultSet resultSet = statement.executeQuery()){
                DishOrderStatusHistory dishOrderStatusHistory = new DishOrderStatusHistory(
                        resultSet.getString("id"),
                        StatusEnum.valueOf(resultSet.getString("status")),
                        resultSet.getTimestamp("date").toLocalDateTime()
                );
                dishOrderStatusHistoryList.add(dishOrderStatusHistory);
            }
            return dishOrderStatusHistoryList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
