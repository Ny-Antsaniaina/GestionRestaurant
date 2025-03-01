package org.example.DAO.CrudOperation;



import org.example.Entity.Stock;

import java.util.List;

public class StockCrudOperation implements CrudOperation<Stock> {

    @Override
    public List<Stock> findAll(int page, int pageSize) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Stock findById(int id) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Stock> saveAll(List<Stock> list) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void deleteById(int id) {
  throw new RuntimeException("Not implemented");
    }


}
