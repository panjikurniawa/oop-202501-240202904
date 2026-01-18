package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.ProductService;
import javafx.collections.FXCollections;  //
import javafx.collections.ObservableList;
import java.util.List;

public class ProductController {
    private ProductService productService;

    // Constructor YANG BENAR
    public ProductController() {
        this.productService = new ProductService();
    }

    // Handle tambah produk
    public boolean handleAddProduct(String code, String name, double price, int stock) {
        try {
            return productService.addProduct(code, name, price, stock);
        } catch (IllegalArgumentException e) {
            throw e; // Propagate exception ke View
        }
    }

    // Get all products untuk ditampilkan di ListView
    public ObservableList<String> getProductsForDisplay() {
        ObservableList<String> displayList = FXCollections.observableArrayList();
        for (Product p : productService.getAllProducts()) {
            displayList.add(p.toString());
        }
        return displayList;
    }

    // Get all products sebagai List<Product>
    public ObservableList<Product> getAllProducts() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        productList.addAll(productService.getAllProducts());
        return productList;
    }

    // Get product count
    public int getProductCount() {
        return productService.getProductCount();
    }

    // Get product by code
    public Product getProductByCode(String code) {
        return productService.getProductByCode(code);
    }
}