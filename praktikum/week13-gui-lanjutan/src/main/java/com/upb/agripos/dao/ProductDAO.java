package com.upb.agripos.dao;

import com.upb.agripos.model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/agripos",
                    "root",
                    "123"
            );
        } catch (SQLException e) {
            System.err.println("Koneksi database gagal: " + e.getMessage());
        }
    }

    public void insert(Product product) {
        String sql = "INSERT INTO products (code, name, description, price, stock) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, product.getCode());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getDescription());
            pstmt.setDouble(4, product.getPrice());
            pstmt.setInt(5, product.getStock());

            pstmt.executeUpdate();
            System.out.println("[DAO] Produk disimpan ke database: " + product.getCode());
        } catch (SQLException e) {
            System.err.println("[DAO] Error insert product: " + e.getMessage());
            throw new RuntimeException("Gagal menyimpan produk ke database", e);
        }
    }
}
