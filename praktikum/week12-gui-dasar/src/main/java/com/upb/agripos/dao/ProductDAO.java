package com.upb.agripos.dao;

import com.upb.agripos.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private List<Product> products = new ArrayList<>();

    // CREATE
    public void insert(Product product) {
        products.add(product);
        System.out.println("DEBUG: Product inserted - " + product);
    }

    // READ
    public List<Product> getAll() {
        return new ArrayList<>(products);
    }

    // READ by code
    public Product getByCode(String code) {
        return products.stream()
                .filter(p -> p.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    // UPDATE
    public void update(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getCode().equals(product.getCode())) {
                products.set(i, product);
                break;
            }
        }
    }

    // DELETE
    public void delete(String code) {
        products.removeIf(p -> p.getCode().equals(code));
    }

    // Count
    public int count() {
        return products.size();
    }
}