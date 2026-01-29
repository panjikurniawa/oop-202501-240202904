// ProductController.java
package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.service.ProductService;
import java.util.List;

public class ProductController {
    private ProductService productService = new ProductService();

    // Method 1: Dengan 5 parameter (yang Anda panggil)
    public boolean addProduct(String name, String description, String code, double price, int stock) {
        Product product = new Product(name, description, code, price, stock);
        return productService.addProduct(product);
    }

    // Method 2: Dengan object Product (jika sudah ada)
    public boolean addProduct(Product product) {
        return productService.addProduct(product);
    }

    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    public boolean deleteProduct(String code) {
        return productService.deleteProduct(code);
    }

    // Optional: Method untuk update
    public boolean updateProduct(Product product) {
        return productService.updateProduct(product);
    }
}
