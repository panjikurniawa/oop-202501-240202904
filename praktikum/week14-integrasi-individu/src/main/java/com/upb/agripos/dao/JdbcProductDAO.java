package com.upb.agripos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.upb.agripos.model.Product;

/**
 * Implementasi ProductDAO dengan JDBC
 * Mengikuti pattern dari Bab 11 (Data Access Object)
 */
public class JdbcProductDAO implements ProductDAO {
    private final Connection connection;

    public JdbcProductDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Product p) throws Exception {
        if (p.getCode() == null || p.getCode().isEmpty()) {
            throw new IllegalArgumentException("Kode produk tidak boleh kosong");
        }
        if (p.getPrice() < 0) {
            throw new IllegalArgumentException("Harga tidak boleh negatif");
        }
        if (p.getStock() < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif");
        }

        // Cek apakah kode sudah ada
        if (exists(p.getCode())) {
            throw new Exception("Produk dengan kode '" + p.getCode() + "' sudah ada");
        }

        String sql = "INSERT INTO products (code, name, price, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getCode());
            stmt.setString(2, p.getName());
            stmt.setDouble(3, p.getPrice());
            stmt.setInt(4, p.getStock());
            stmt.executeUpdate();
            System.out.println("✅ Produk '" + p.getName() + "' berhasil ditambahkan");
        } catch (SQLException e) {
            throw new Exception("Gagal menambah produk: " + e.getMessage());
        }
    }

    @Override
    public void update(Product p) throws Exception {
        String sql = "UPDATE products SET name = ?, price = ?, stock = ? WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, p.getName());
            stmt.setDouble(2, p.getPrice());
            stmt.setInt(3, p.getStock());
            stmt.setString(4, p.getCode());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new Exception("Produk dengan kode '" + p.getCode() + "' tidak ditemukan");
            }
            System.out.println("✅ Produk '" + p.getName() + "' berhasil diupdate");
        } catch (SQLException e) {
            throw new Exception("Gagal mengubah produk: " + e.getMessage());
        }
    }

    @Override
    public void delete(String code) throws Exception {
        String sql = "DELETE FROM products WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new Exception("Produk dengan kode " + code + " tidak ditemukan");
            }
            System.out.println("✅ Produk dengan kode '" + code + "' berhasil dihapus");
        } catch (SQLException e) {
            throw new Exception("Gagal menghapus produk: " + e.getMessage());
        }
    }

    @Override
    public Product findByCode(String code) throws Exception {
        String sql = "SELECT * FROM products WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                            rs.getString("code"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            throw new Exception("Gagal mencari produk: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Product> findAll() throws Exception {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY code";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Product product = new Product(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                );
                products.add(product);
            }
            System.out.println("✅ Ditemukan " + products.size() + " produk");
        } catch (SQLException e) {
            throw new Exception("Gagal mengambil daftar produk: " + e.getMessage());
        }
        return products;
    }

    // ========== METHOD BARU UNTUK WEEK 14 ==========

    @Override
    public boolean updateStock(String code, int newStock) throws Exception {
        if (newStock < 0) {
            throw new IllegalArgumentException("Stok tidak boleh negatif");
        }

        String sql = "UPDATE products SET stock = ? WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, newStock);
            stmt.setString(2, code);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("✅ Stok produk '" + code + "' diupdate menjadi " + newStock);
            }
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new Exception("Gagal mengupdate stok: " + e.getMessage());
        }
    }

    @Override
    public boolean exists(String code) throws Exception {
        String sql = "SELECT COUNT(*) FROM products WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new Exception("Gagal mengecek keberadaan produk: " + e.getMessage());
        }
        return false;
    }

    // ========== METHOD TAMBAHAN UNTUK KERANJANG ==========

    /**
     * Mengurangi stok produk (untuk keperluan keranjang belanja)
     * @param code Kode produk
     * @param quantity Jumlah yang dikurangi
     * @return true jika berhasil
     * @throws Exception Jika stok tidak cukup atau error
     */
    public boolean reduceStock(String code, int quantity) throws Exception {
        // 1. Cek produk dan stok tersedia
        Product product = findByCode(code);
        if (product == null) {
            throw new Exception("Produk dengan kode '" + code + "' tidak ditemukan");
        }

        if (product.getStock() < quantity) {
            throw new Exception("Stok tidak cukup. Stok tersedia: " + product.getStock() + ", Dibutuhkan: " + quantity);
        }

        // 2. Update stok
        int newStock = product.getStock() - quantity;
        return updateStock(code, newStock);
    }

    /**
     * Menambah stok produk (untuk pengembalian ke keranjang)
     * @param code Kode produk
     * @param quantity Jumlah yang ditambah
     * @return true jika berhasil
     * @throws Exception Jika error
     */
    public boolean increaseStock(String code, int quantity) throws Exception {
        Product product = findByCode(code);
        if (product == null) {
            throw new Exception("Produk dengan kode '" + code + "' tidak ditemukan");
        }

        int newStock = product.getStock() + quantity;
        return updateStock(code, newStock);
    }

    /**
     * Cari produk berdasarkan nama (partial match)
     * @param name Keyword nama produk
     * @return List produk yang cocok
     * @throws Exception Jika error
     */
    public List<Product> searchByName(String name) throws Exception {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getString("code"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock")
                    );
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Gagal mencari produk: " + e.getMessage());
        }

        return products;
    }

    /**
     * Mendapatkan produk dengan stok rendah (untuk alert)
     * @param threshold Batas stok rendah
     * @return List produk dengan stok di bawah threshold
     * @throws Exception Jika error
     */
    public List<Product> findLowStockProducts(int threshold) throws Exception {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE stock < ? ORDER BY stock";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, threshold);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getString("code"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock")
                    );
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Gagal mengambil produk stok rendah: " + e.getMessage());
        }

        return products;
    }
}