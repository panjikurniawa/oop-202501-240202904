package com.upb.agripos.dao;

import com.upb.agripos.model.Product;
import java.util.List;

public interface ProductDAO {
    // CRUD Basic
    void insert(Product p) throws Exception;
    void update(Product p) throws Exception;
    void delete(String code) throws Exception;
    Product findByCode(String code) throws Exception;
    List<Product> findAll() throws Exception;

    // Method baru untuk Week 14
    boolean updateStock(String code, int newStock) throws Exception;
    boolean exists(String code) throws Exception;

    // Method tambahan untuk keranjang belanja
    boolean reduceStock(String code, int quantity) throws Exception;
    boolean increaseStock(String code, int quantity) throws Exception;
    List<Product> searchByName(String name) throws Exception;
    List<Product> findLowStockProducts(int threshold) throws Exception;
}