package org.example.DAO.CrudOperation;

import java.sql.SQLException;
import java.util.List;

public interface CrudOperation<E>{
    List<E> findAll(int page, int pageSize) throws SQLException;
    E findById(int id);
    List<E> saveAndUpdate(List<E> list);
    void deleteById(int id);
}