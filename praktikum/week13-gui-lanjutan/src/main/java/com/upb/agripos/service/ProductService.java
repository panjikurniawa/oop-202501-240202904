package com.upb.agripos.service;

import com.upb.agripos.model.Product;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private List<Product> products = new ArrayList<>();

    // CREATE - Tambah produk baru
    public boolean addProduct(Product product) {
        // Validasi: code tidak boleh null atau kosong
        if (product.getCode() == null || product.getCode().trim().isEmpty()) {
            return false;
        }

        // Cek duplikasi code
        for (Product p : products) {
            if (p.getCode().equalsIgnoreCase(product.getCode())) {
                return false; // Code sudah ada
            }
        }

        // Tambahkan ke list
        products.add(product);
        return true;
    }

    // READ - Ambil semua produk
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    // READ - Cari produk berdasarkan code
    public Product getProductByCode(String code) {
        for (Product p : products) {
            if (p.getCode().equalsIgnoreCase(code)) {
                return p;
            }
        }
        return null;
    }

    // UPDATE - Update produk yang sudah ada
    public boolean updateProduct(Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            Product existingProduct = products.get(i);
            if (existingProduct.getCode().equals(updatedProduct.getCode())) {
                products.set(i, updatedProduct);
                return true;
            }
        }
        return false; // Produk tidak ditemukan
    }

    // DELETE - Hapus produk berdasarkan code
    public boolean deleteProduct(String code) {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.getCode().equals(code)) {
                products.remove(i);
                return true;
            }
        }
        return false; // Produk tidak ditemukan
    }

    // READ - Hitung jumlah produk
    public int getProductCount() {
        return products.size();
    }

    // CLEAR - Hapus semua produk (untuk testing)
    public void clearAllProducts() {
        products.clear();
    }
}
