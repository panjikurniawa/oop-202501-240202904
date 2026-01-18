package com.upb.agripos.service;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.model.Product;
import java.util.List;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    // Business logic untuk tambah produk
    public boolean addProduct(String code, String name, double price, int stock) {
        // Validasi input
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode produk tidak boleh kosong");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama produk tidak boleh kosong");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Harga harus lebih dari 0");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif");
        }

        // Cek duplikasi kode
        if (productDAO.getByCode(code) != null) {
            throw new IllegalArgumentException("Kode produk sudah digunakan");
        }

        // Buat objek Product
        Product product = new Product(code, name, price, stock);

        // Simpan ke DAO
        productDAO.insert(product);
        return true;
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productDAO.getAll();
    }

    // Get product by code
    public Product getProductByCode(String code) {
        return productDAO.getByCode(code);
    }

    // Update product
    public boolean updateProduct(Product product) {
        productDAO.update(product);
        return true;
    }

    // Delete product
    public boolean deleteProduct(String code) {
        productDAO.delete(code);
        return true;
    }

    // Count products
    public int getProductCount() {
        return productDAO.count();
    }
}