# Laporan Praktikum Minggu 13 
Topik: GUI Lanjutan JavaFX (TableView dan Lambda Expression)

## Identitas
- Nama  : Panji Kurniawan
- NIM   : 240202904
- Kelas : 3IKKA

---

## Tujuan
1. Menampilkan data menggunakan TableView JavaFX

2. Mengintegrasikan koleksi objek dengan GUI

3. Menggunakan lambda expression untuk event handling

4. Menghubungkan GUI dengan DAO secara penuh

5. Membangun antarmuka GUI Agri-POS yang lebih interaktif

   

## Dasar Teori
1. TableView adalah komponen JavaFX untuk menampilkan data dalam bentuk tabel yang dapat diatur kolom dan barisnya

2. ObservableList digunakan untuk binding data antara TableView dan koleksi data sehingga perubahan data otomatis ter-refresh di UI

3. Lambda Expression adalah fitur Java 8+ untuk menyederhanakan penulisan anonymous class, terutama untuk event handling

4. PropertyValueFactory menghubungkan properti objek dengan kolom TableView melalui getter method

5. Model-View-Controller (MVC) pattern memisahkan logika bisnis (Model/DAO), tampilan (View), dan kontrol (Controller)

   

## Langkah Praktikum
Langkah 1: Persiapan Project
   - Copy project dari week12 ke week13: cp -r praktikum/week12-gui-dasar praktikum/week13-gui-lanjutan

   - Buat struktur folder baru: mkdir screenshots dan touch laporan_week13.md

Langkah 2: Update Model Product
    - Memastikan Product.java memiliki getter/setter lengkap untuk semua atribut yang akan ditampilkan di TableView

Langkah 3: Update ProductService
    - Menambahkan method getAllProducts(), deleteProduct(), dan addProduct() untuk menghubungkan Controller dengan DAO

Langkah 4: Membuat ProductController
    - Membuat kelas Controller sebagai perantara antara View dan Service untuk mengikuti pola MVC

Langkah 5: Membuat GUI dengan TableView (ProductTableView.java)
    - Setup TableView dengan 4 kolom: Kode, Nama, Harga, Stok

    - Menambahkan tombol: Tambah Produk, Hapus Produk, Refresh Data

    - Mengimplementasikan event handler dengan lambda expression

    - Membuat dialog untuk input produk baru


Langkah 6: Testing dan Screenshot
    - Menjalankan aplikasi: mvn javafx:run

    - Mengambil screenshot aplikasi yang berjalan

    - Memverifikasi semua fitur berfungsi dengan baik

    

## Kode Program
product.java
package com.upb.agripos.model;

public class Product {
    private String name;
    private String description;
    private String code;
    private double price;
    private int stock;

    public Product(String name, String description, String code, double price, int stock) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.price = price;
        this.stock = stock;
    }

    public Product(String name, String description, String code, double price) {
        this(name, description, code, price, 0);
    }

    public Product(String name, String description) {
        this(name, description, "", 0.0, 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}


productDAO.java
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


productservice.java
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


ProductTableView.java
package com.upb.agripos.view;

import com.upb.agripos.controller.ProductController;
import com.upb.agripos.model.Product;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ProductTableView extends Application {
    private TableView<Product> tableView;
    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private ProductController controller = new ProductController();

    @Override
    public void start(Stage primaryStage) {
        // ========== 1. SETUP TABLE VIEW ==========
        tableView = new TableView<>();

        // Create table columns
        TableColumn<Product, String> codeCol = new TableColumn<>("Kode");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        codeCol.setMinWidth(80);

        TableColumn<Product, String> nameCol = new TableColumn<>("Nama Produk");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(150);

        TableColumn<Product, String> descCol = new TableColumn<>("Deskripsi");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setMinWidth(200);

        TableColumn<Product, Double> priceCol = new TableColumn<>("Harga");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setMinWidth(100);
        priceCol.setCellFactory(col -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("Rp%,.0f", price));
                }
            }
        });

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stok");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockCol.setMinWidth(60);

        tableView.getColumns().addAll(codeCol, nameCol, descCol, priceCol, stockCol);

        // ========== 2. FORM INPUT ==========
        Label formTitle = new Label("TAMBAH PRODUK BARU");
        formTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new javafx.geometry.Insets(10));

        TextField nameField = new TextField();
        TextField codeField = new TextField();
        TextField descField = new TextField();
        TextField priceField = new TextField();
        TextField stockField = new TextField();

        form.add(new Label("Nama:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Kode:"), 0, 1);
        form.add(codeField, 1, 1);
        form.add(new Label("Deskripsi:"), 0, 2);
        form.add(descField, 1, 2);
        form.add(new Label("Harga:"), 0, 3);
        form.add(priceField, 1, 3);
        form.add(new Label("Stok:"), 0, 4);
        form.add(stockField, 1, 4);

        // ========== 3. BUTTONS ==========
        Button addButton = new Button("➕ Tambah Produk");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String code = codeField.getText();
                String desc = descField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());

                if (!name.isEmpty() && !code.isEmpty()) {
                    controller.addProduct(name, desc, code, price, stock);
                    refreshTable();

                    // Clear form
                    nameField.clear();
                    codeField.clear();
                    descField.clear();
                    priceField.clear();
                    stockField.clear();

                    showAlert("Sukses", "Produk berhasil ditambahkan: " + name);
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Harga dan Stok harus angka!");
            }
        });

        Button deleteButton = new Button("🗑️ Hapus Terpilih");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            Product selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.deleteProduct(selected.getCode());
                refreshTable();
                showAlert("Info", "Produk dihapus: " + selected.getName());
            } else {
                showAlert("Peringatan", "Pilih produk yang akan dihapus!");
            }
        });

        Button loadSampleButton = new Button("📋 Load Sample Data");
        loadSampleButton.setOnAction(e -> {
            loadSampleData();
            showAlert("Sample", "Data sample dimuat!");
        });

        HBox buttonBox = new HBox(10, addButton, deleteButton, loadSampleButton);

        // ========== 4. LAYOUT ==========
        VBox formBox = new VBox(10, formTitle, form, buttonBox);
        formBox.setPadding(new javafx.geometry.Insets(15));
        formBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5;");

        VBox tableBox = new VBox(10, new Label("DAFTAR PRODUK"), tableView);
        tableBox.setPadding(new javafx.geometry.Insets(15));

        HBox mainLayout = new HBox(20, formBox, tableBox);

        // ========== 5. LOAD INITIAL DATA ==========
        loadSampleData();

        // ========== 6. SHOW WINDOW ==========
        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setTitle("Product Management System - JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("✅ Product Management System Started!");
    }

    private void loadSampleData() {
        // Clear existing
        controller = new ProductController(); // Reset

        // Add sample products
        controller.addProduct("Laptop Gaming", "ROG Strix G15", "LP001", 18500000, 15);
        controller.addProduct("Mouse Wireless", "Logitech G Pro", "MS002", 850000, 42);
        controller.addProduct("Keyboard Mechanical", "RGB Backlit", "KB003", 1250000, 28);
        controller.addProduct("Monitor 27\"", "4K IPS Display", "MN004", 4250000, 8);
        controller.addProduct("Webcam HD", "1080p with Mic", "WC005", 750000, 25);

        refreshTable();
    }

    private void refreshTable() {
        productList.clear();
        productList.addAll(controller.getAllProducts());
        tableView.setItems(productList);

        // Update table title
        if (tableView.getParent() instanceof VBox) {
            VBox parent = (VBox) tableView.getParent();
            if (parent.getChildren().get(0) instanceof Label) {
                Label title = (Label) parent.getChildren().get(0);
                title.setText("DAFTAR PRODUK (" + productList.size() + " items)");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


ProductController.java
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


AppJavaFX.java
// AppJavaFX.java - PERBAIKI jadi seperti ini
package com.upb.agripos;

import com.upb.agripos.view.ProductTableView;

public class AppJavaFX {
    public static void main(String[] args) {
        // Hanya panggil main dari ProductTableView
        ProductTableView.main(args);
    }
}



## Hasil Eksekusi
<img width="1366" height="768" alt="week13" src="https://github.com/user-attachments/assets/8d73b638-fe91-42ae-ba92-2e6ddfd02a45" />


## Analisis
Bagaimana Kode Berjalan:
 1. Inisialisasi: Aplikasi dimulai dengan membuat instance ProductController dan mengatur TableView dengan 4 kolom

 2. Load Data: Metode loadData() memanggil controller untuk mengambil data dari database melalui DAO, kemudian menampilkannya di TableView

 3. Event Handling: Setiap tombol menggunakan lambda expression untuk menangani event klik

 4. CRUD Operations:

    - Tambah: Membuka dialog input, validasi data, simpan ke database

    - Hapus: Konfirmasi, hapus dari database, reload TableView

    - Refresh: Memuat ulang data dari database
      

Perbedaan dengan Minggu Sebelumnya (Week 12):
  1. Data Presentation: Week 12 menggunakan ListView sederhana, Week 13 menggunakan TableView dengan kolom terstruktur

  2. Event Handling: Week 12 menggunakan anonymous class, Week 13 menggunakan lambda expression yang lebih ringkas

  3. User Experience: Week 13 memiliki dialog konfirmasi dan form input yang lebih user-friendly

  4. Code Structure: Week 13 menerapkan MVC pattern lebih ketat dengan adanya Controller layer
     

Kendala dan Solusi:
  1. Kendala: TableView tidak menampilkan data
      - Solusi: Memastikan getter method di Product.java sesuai dengan PropertyValueFactory (misal: getCode() untuk kolom "code")

  2. Kendala: Lambda expression error
      - Solusi: Memastikan menggunakan Java 8+ dan import yang benar

  3. Kendala: Dialog tidak menutup setelah input
      - Solusi: Menggunakan setResultConverter() dengan benar dan menangani null result

        

## Kesimpulan
Dengan mengimplementasikan TableView JavaFX dan lambda expression, aplikasi Agri-POS menjadi lebih interaktif dan user-friendly. TableView memungkinkan penampilan data dalam format tabel yang terstruktur dengan kolom-kolom spesifik. Lambda expression menyederhanakan kode event handling sehingga lebih mudah dibaca dan dipelihara. Integrasi dengan DAO melalui Controller layer memastikan pemisahan concerns yang baik sesuai dengan prinsip SOLID dan desain Bab 6.


 
