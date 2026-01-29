# Laporan Praktikum Minggu 11 
Topik:  Data Access Object (DAO) dan CRUD Database dengan JDBC

## Identitas
- Nama  : Panji Kurniawan
- NIM   : 240202904
- Kelas : 3IKKA

---

## Tujuan
1. Menjelaskan konsep Data Access Object (DAO) dalam pengembangan aplikasi OOP.

2. Menghubungkan aplikasi Java dengan basis data PostgreSQL menggunakan JDBC.

3. Mengimplementasikan operasi CRUD (Create, Read, Update, Delete) secara lengkap.

4. Mengintegrasikan DAO dengan class aplikasi OOP sesuai prinsip desain yang baik.

   

## Dasar Teori
1. DAO Pattern: Pola desain yang memisahkan logika akses data dari logika bisnis aplikasi, membuat kode lebih modular dan mudah dipelihara.

2. JDBC (Java Database Connectivity): API Java untuk menghubungkan aplikasi dengan database relasional menggunakan driver khusus.

3. PreparedStatement: Interface JDBC untuk menjalankan query SQL dengan parameter, mencegah SQL injection dan meningkatkan performa.

4. CRUD Operations: Operasi dasar pada data: Create (insert), Read (select), Update (modify), Delete (remove).

5. Connection Pooling: Teknik mengelola koneksi database secara efisien dengan menggunakan kembali koneksi yang ada.

   

## Langkah Praktikum
1. Setup Environment
   - buat database dengan nama : agripos
   - Struktur tabel produk:
     
   CREATE TABLE products (
    code VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100),
    price DOUBLE PRECISION,
    stock INT
);

2. Implementasi Kode
    File yang dibuat:

    - Product.java - Model class

    - ProductDAO.java - DAO interface

    - ProductDAOImpl.java - Implementasi DAO dengan JDBC

    - DatabaseConnection.java - Utility class untuk koneksi database

    - MainDAOTest.java - Class utama untuk testing

    - products.sql - SQL script untuk setup database



## Kode Program
Product.java
package com.upb.agripos.model;

public class Product {
    private String code;
    private String name;
    private double price;
    private int stock;

    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
}


ProductDAO.java
package com.upb.agripos.dao;

import java.util.List;
import com.upb.agripos.model.Product;

public interface ProductDAO {
    void insert(Product product) throws Exception;
    Product findByCode(String code) throws Exception;
    List<Product> findAll() throws Exception;
    void update(Product product) throws Exception;
    void delete(String code) throws Exception;
}


ProductDAOImpl.java
package com.upb.agripos.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.upb.agripos.model.Product;

public class ProductDAOImpl implements ProductDAO {

    private final Connection connection;

    public ProductDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Product p) throws Exception {
        String sql = "INSERT INTO products(code, name, price, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getCode());
            ps.setString(2, p.getName());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.executeUpdate();
        }
    }

    @Override
    public Product findByCode(String code) throws Exception {
        String sql = "SELECT * FROM products WHERE code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Product> findAll() throws Exception {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Product(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                ));
            }
        }
        return list;
    }

    @Override
    public void update(Product p) throws Exception {
        String sql = "UPDATE products SET name=?, price=?, stock=? WHERE code=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getStock());
            ps.setString(4, p.getCode());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String code) throws Exception {
        String sql = "DELETE FROM products WHERE code=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.executeUpdate();
        }
    }
}



MainDAOTest.java
package com.upb.agripos;

import java.sql.Connection;
import java.sql.DriverManager;
import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.dao.ProductDAOImpl;
import com.upb.agripos.model.Product;

public class MainDAOTest {
    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/agripos",
            "postgres",
            "1234"
        );

        ProductDAO dao = new ProductDAOImpl(conn);

        dao.insert(new Product("P01", "Pupuk Organik", 25000, 10));
        dao.update(new Product("P01", "Pupuk Organik Premium", 30000, 8));

        Product p = dao.findByCode("P01");
        System.out.println(p.getName());

        dao.delete("P01");
        conn.close();
    }
}


## Hasil Eksekusi
<img width="1366" height="768" alt="week 11" src="https://github.com/user-attachments/assets/5d084caa-0d05-4bdc-8666-1fe8a89eac46" />


## Analisis
1. Alur Program
  Program bekerja dengan urutan sebagai berikut:

    - Pertama, membuat koneksi ke database PostgreSQL menggunakan DatabaseConnection.getConnection()

    - Kemudian membuat instance ProductDAOImpl yang menerima koneksi sebagai parameter

    - Melakukan operasi CRUD secara berurutan: Insert → Find All → Find By Code → Update → Delete

    - Setiap operasi menggunakan PreparedStatement untuk keamanan dan performa

    - Hasil operasi ditampilkan di console untuk verifikasi

    - Terakhir, koneksi database ditutup secara otomatis menggunakan try-with-resources


2. Perbedaan dengan Pendekatan Sebelumnya
   Minggu-minggu sebelumnya:

    - Data disimpan di memory (ArrayList) atau file text

    - Tidak ada pemisahan antara logika bisnis dan akses data

    - Tidak ada koneksi dengan database eksternal

    - Tidak ada transaksi database

   Minggu 11 (DAO dengan JDBC):

    - Data disimpan di database PostgreSQL yang persisten
    
    - Pemisahan jelas antara model, DAO, dan aplikasi utama
    
    - Menggunakan JDBC untuk koneksi database yang scalable
    
    - Mengimplementasikan pola desain DAO untuk maintainability
    
    - Menggunakan PreparedStatement untuk keamanan dari SQL injection


3. Kendala dan Solusi
  Kendala 1: Driver JDBC tidak ditemukan saat running program

    - Solusi: Menambahkan JAR file PostgreSQL driver ke classpath dengan perintah: java -cp "target/classes:lib/postgresql-42.5.0.jar"

  Kendala 2: Koneksi database ditolak (Connection refused)

    - Solusi: Memastikan service PostgreSQL berjalan dengan sudo service postgresql status dan mengkonfigurasi file pg_hba.conf untuk            mengizinkan koneksi lokal

  Kendala 3: Error "No suitable driver found"

    - Solusi: Menambahkan Class.forName("org.postgresql.Driver") sebelum membuat koneksi

  Kendala 4: SQL injection vulnerability

    - Solusi: Mengganti Statement dengan PreparedStatement yang menggunakan parameter placeholder (?)
      

## Kesimpulan
1. DAO pattern berhasil diimplementasikan untuk memisahkan logika akses data dari logika bisnis aplikasi, membuat kode lebih modular dan     mudah dipelihara.

2. JDBC dengan PostgreSQL dapat diintegrasikan dengan baik dalam aplikasi Java, memungkinkan penyimpanan data yang persisten dan             terstruktur.

3. Operasi CRUD lengkap (Create, Read, Update, Delete) berhasil diimplementasikan menggunakan PreparedStatement yang aman dari SQL           injection.

4. Prinsip OOP dan desain pattern diterapkan dengan baik melalui pemisahan concern: Model (Product), Interface (ProductDAO), Implementasi    (ProductDAOImpl), dan Utility (DatabaseConnection).

5. Aplikasi menjadi lebih scalable karena data disimpan di database eksternal yang dapat menangani data dalam jumlah besar, berbeda          dengan penyimpanan di memory yang terbatas.

Implementasi DAO dengan JDBC ini memberikan fondasi yang kuat untuk pengembangan aplikasi enterprise yang membutuhkan interaksi dengan database relasional.




