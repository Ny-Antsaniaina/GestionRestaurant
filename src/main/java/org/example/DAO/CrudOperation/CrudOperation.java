package org.example.DAO.CrudOperation;

import java.sql.SQLException;
import java.util.List;

public interface CrudOperation<E>{
    List<E> findAll(int page, int pageSize) throws SQLException;
    E findById(String id);
    List<E> saveAll(List<E> list);
    void deleteById(String id);
}