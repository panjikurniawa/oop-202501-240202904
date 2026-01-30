# Laporan Praktikum Minggu 12 
Topik: GUI Dasar JavaFX (Event-Driven Programming)

## Identitas
- Nama  : Panji Kurniawan
- NIM   : 240202904
- Kelas : 3IKKA

---

## Tujuan
Mahasiswa mampu memahami konsep event-driven programming, membangun antarmuka grafis sederhana menggunakan JavaFX, membuat form input data produk, menampilkan daftar produk pada GUI, dan mengintegrasikan GUI dengan modul backend yang telah dibuat (DAO & Service).


## Dasar Teori
1. Event-Driven Programming adalah paradigma pemrograman di mana alur program ditentukan oleh event seperti klik mouse, penekanan tombol,     atau interaksi pengguna lainnya.
2. JavaFX adalah framework GUI untuk Java yang menggantikan Swing, menggunakan arsitektur scene graph dan FXML untuk mendesain UI.
3. Model-View-Controller (MVC) adalah pola desain yang memisahkan aplikasi menjadi tiga komponen: Model (data), View (tampilan), dan          Controller (logika bisnis).
4. Event Handler di JavaFX adalah metode yang merespons event pengguna, biasanya diimplementasikan menggunakan lambda expression atau         inner class.
5. Integrasi Layered Architecture memastikan GUI hanya berkomunikasi dengan service layer, bukan langsung ke DAO, menjaga prinsip             separation of concerns.


## Langkah Praktikum
1. Setup Proyek:
  - Membuat struktur direktori sesuai panduan
  - Menyalin file Product, ProductDAO, ProductService dari praktikum sebelumnya
  - Menyiapkan dependencies JavaFX di Maven/Gradle
    
2. Implementasi GUI:
  - Membuat kelas ProductFormView.java dengan komponen UI (TextField, Button, ListView)
  - Mengimplementasikan event handler untuk tombol "Tambah Produk"
  - Membuat kelas ProductController.java sebagai penghubung antara view dan service
  - Membuat kelas AppJavaFX.java sebagai entry point aplikasi

3. Integrasi Backend:
  - Menggunakan ProductService yang sudah ada dari praktikum sebelumnya
  - Memastikan GUI memanggil metode service, bukan langsung ke DAO
  - Mengimplementasikan pembaruan tampilan setelah operasi CRUD

4. Testing dan Validasi:
  - Menjalankan aplikasi dan menguji semua fungsi
  - Memastikan data tersimpan ke database PostgreSQL
  - Memverifikasi tampilan diperbarui setelah penambahan produk


## Kode Program
ProductController.java
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


ProductDAO.java
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


Product.java
package com.upb.agripos.model;

public class Product {
    private String code;
    private String name;
    private double price;
    private int stock;

    // Constructor
    public Product() {}

    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return code + " - " + name + " | Rp " + price + " | Stok: " + stock;
    }
}


ProductService.java
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


ProductFormView.java
package com.upb.agripos.view;

import com.upb.agripos.controller.ProductController;
import com.upb.agripos.model.Product;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ProductFormView extends Application {

    private ProductController controller;
    private ListView<String> productListView;
    private TextField txtCode, txtName, txtPrice, txtStock;
    private Label lblStatus;

    @Override
    public void start(Stage primaryStage) {
        controller = new ProductController();

        // Title
        Label lblTitle = new Label("Kelola Produk - AgriPOS");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Form Input
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(20));

        // Row 0: Kode Produk
        Label lblCode = new Label("Kode Produk:");
        txtCode = new TextField();
        txtCode.setPromptText("PRD001");
        formGrid.add(lblCode, 0, 0);
        formGrid.add(txtCode, 1, 0);

        // Row 1: Nama Produk
        Label lblName = new Label("Nama Produk:");
        txtName = new TextField();
        txtName.setPromptText("Nama Produk");
        formGrid.add(lblName, 0, 1);
        formGrid.add(txtName, 1, 1);

        // Row 2: Harga
        Label lblPrice = new Label("Harga:");
        txtPrice = new TextField();
        txtPrice.setPromptText("50000");
        formGrid.add(lblPrice, 0, 2);
        formGrid.add(txtPrice, 1, 2);

        // Row 3: Stok
        Label lblStock = new Label("Stok:");
        txtStock = new TextField();
        txtStock.setPromptText("100");
        formGrid.add(lblStock, 0, 3);
        formGrid.add(txtStock, 1, 3);

        // Buttons
        Button btnAdd = new Button("Tambah Produk");
        btnAdd.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnAdd.setPrefWidth(150);

        Button btnClear = new Button("Clear Form");
        btnClear.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white;");
        btnClear.setPrefWidth(150);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(btnAdd, btnClear);
        formGrid.add(buttonBox, 1, 4);

        // Status Label
        lblStatus = new Label("Status: Siap menerima input");
        lblStatus.setStyle("-fx-text-fill: #27ae60;");
        formGrid.add(lblStatus, 0, 5, 2, 1);

        // Product List Display
        Label lblListTitle = new Label("Daftar Produk:");
        lblListTitle.setStyle("-fx-font-weight: bold;");

        productListView = new ListView<>();
        productListView.setPrefHeight(250);

        // Update list initially
        updateProductList();

        // Layout
        VBox leftPane = new VBox(15);
        leftPane.getChildren().addAll(lblTitle, formGrid);
        leftPane.setPadding(new Insets(20));
        leftPane.setStyle("-fx-background-color: #ecf0f1;");

        VBox rightPane = new VBox(10);
        rightPane.getChildren().addAll(lblListTitle, productListView);
        rightPane.setPadding(new Insets(20));

        HBox mainLayout = new HBox(20);
        mainLayout.getChildren().addAll(leftPane, rightPane);

        // Event Handlers
        btnAdd.setOnAction(event -> handleAddProduct());
        btnClear.setOnAction(event -> clearForm());

        // Scene
        Scene scene = new Scene(mainLayout, 800, 500);
        primaryStage.setTitle("Week 12 - GUI Dasar JavaFX (AgriPOS)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleAddProduct() {
        try {
            String code = txtCode.getText().trim();
            String name = txtName.getText().trim();
            double price = Double.parseDouble(txtPrice.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());

            // Panggil Controller
            boolean success = controller.handleAddProduct(code, name, price, stock);

            if (success) {
                lblStatus.setText("Status: Produk berhasil ditambahkan!");
                lblStatus.setStyle("-fx-text-fill: #27ae60;");

                // Update list
                updateProductList();

                // Clear form
                clearForm();
            }
        } catch (NumberFormatException e) {
            lblStatus.setText("Status: Error! Harga dan Stok harus angka");
            lblStatus.setStyle("-fx-text-fill: #e74c3c;");
        } catch (IllegalArgumentException e) {
            lblStatus.setText("Status: Error! " + e.getMessage());
            lblStatus.setStyle("-fx-text-fill: #e74c3c;");
        } catch (Exception e) {
            lblStatus.setText("Status: Error tidak diketahui - " + e.getMessage());
            lblStatus.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    private void updateProductList() {
        productListView.setItems(controller.getProductsForDisplay());
    }

    private void clearForm() {
        txtCode.clear();
        txtName.clear();
        txtPrice.clear();
        txtStock.clear();
        lblStatus.setText("Status: Form telah dibersihkan");
        lblStatus.setStyle("-fx-text-fill: #f39c12;");
        txtCode.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


AppJavaFX.java
package com.upb.agripos;

import com.upb.agripos.view.ProductFormView;
import javafx.application.Application;

public class AppJavaFX {
    public static void main(String[] args) {
        // Launch JavaFX application
        Application.launch(ProductFormView.class, args);
    }
}



## Hasil Eksekusi
<img width="1366" height="768" alt="week12" src="https://github.com/user-attachments/assets/669bfa17-79ca-477b-9aee-6208be42b92b" />


## Analisis
1. Cara Kerja Program:
  - Aplikasi dimulai dari AppJavaFX.java yang memuat ProductFormView
  - Pengguna mengisi form produk dan klik tombol "Tambah Produk"
  - Event handler memvalidasi input, membuat objek Product
  - ProductController dipanggil untuk memproses data melalui ProductService
  - ProductService menggunakan ProductDAO untuk menyimpan ke database
  - UI diperbarui dengan menambahkan produk ke ListView

2. Perbedaan dengan Minggu Sebelumnya:
  - Week 11: Hanya backend (DAO + JDBC) dengan aplikasi console
  - Week 12: Menambahkan layer GUI dengan JavaFX, menerapkan event-driven programming
  - Integrasi: GUI mengonsumsi service layer yang sudah ada, bukan membuat CRUD baru

3. Kendala dan Solusi:
  - Kendala: JavaFX dependencies sulit dikonfigurasi di Maven
    Solusi: Menggunakan JavaFX SDK langsung dan menambahkan VM options
  - Kendala: ListView tidak otomatis refresh setelah insert
    Solusi: Memanggil loadExistingProducts() setelah operasi berhasil
  - Kendala: Validasi input kompleks
    Solusi: Menambahkan try-catch untuk NumberFormatException dan validasi bisnis


4. Traceability dengan Bab 6:
  - Semua komponen (View, Controller, Service, DAO) sesuai class diagram Bab 6
  - Urutan eksekusi mengikuti sequence diagram: View → Controller → Service → DAO → DB
  - Prinsip DIP diterapkan: View hanya bergantung pada Controller, tidak langsung ke DAO

    
## Kesimpulan
Dengan implementasi GUI JavaFX pada praktikum ini, dapat disimpulkan bahwa:
  1. Event-driven programming memungkinkan pembuatan aplikasi yang responsif terhadap interaksi pengguna
  2. Arsitektur MVC berhasil memisahkan concern antara tampilan, logika bisnis, dan akses data
  3. Integrasi GUI dengan backend existing (DAO & Service) berjalan dengan baik tanpa mengubah logika bisnis yang sudah ada
  4. Traceability dengan desain Bab 6 terjaga dengan konsistensi nama kelas, metode, dan alur eksekusi
  5. JavaFX menyediakan komponen UI yang lengkap untuk membangun aplikasi desktop modern


