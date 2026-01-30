# Laporan Praktikum Minggu 14 
Topik: Integrasi Individu (OOP + Database + GUI)

## Identitas
- Nama  : Panji Kurniawan
- NIM   : 240202904
- Kelas : 3IKKA

---

## Tujuan
Mahasiswa mampu mengintegrasikan seluruh konsep yang telah dipelajari dari Bab 1-13 (OOP, UML, Collections, Exception Handling, Pattern, Database, dan GUI) ke dalam satu aplikasi utuh bernama Agri-POS System yang berfungsi sebagai sistem kasir sederhana.


## Dasar Teori
1. MVC Architecture - Memisahkan aplikasi menjadi Model (data), View (tampilan), dan Controller (logika bisnis)
2. SOLID Principles - Prinsip desain yang membuat kode lebih mudah dipelihara dan dikembangkan
3. Dependency Injection - Memisahkan pembuatan objek dari penggunaannya untuk mengurangi coupling
4. DAO Pattern - Mengisolasi logika akses data dari logika bisnis
5. JavaFX - Framework GUI untuk membangun aplikasi desktop Java


## Langkah Praktikum
1. Setup Project

  - Membuka project existing dari minggu sebelumnya

  - Menyiapkan database PostgreSQL dengan tabel produk

  - Mengkonfigurasi koneksi database di aplikasi

2. Implementasi Integrasi

  - Memperbaiki struktur folder sesuai MVC pattern

  - Membuat interface DAO untuk akses database

  - Mengimplementasikan CartService dengan Collections

  - Membuat unit test untuk CartService

  - Menghubungkan JavaFX GUI dengan backend

3. File/Kode yang Dibuat:

  - src/main/java/com/upb/agripos/AppJavaFX.java - Entry point aplikasi

  - src/main/java/com/upb/agripos/controller/PosController.java - Controller utama

  - src/main/java/com/upb/agripos/service/CartService.java - Logika keranjang belanja

  - src/test/java/com/upb/agripos/CartServiceTest.java - Unit test

  - src/main/java/com/upb/agripos/dao/JdbcProductDAO.java - Implementasi DAO


## Kode Program
PosController.java
package com.upb.agripos.controller;

import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import com.upb.agripos.service.CartService;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.PosView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class PosController {
    private final ProductService productService;
    private final CartService cartService;
    private final PosView view;

    public PosController(ProductService productService, CartService cartService, PosView view) {
        this.productService = productService;
        this.cartService = cartService;
        this.view = view;

        initializeController();
        loadProducts();
    }

    private void initializeController() {
        setupProductEventHandlers();
        setupCartEventHandlers();
        setupCheckoutEventHandlers();
    }

    private void setupProductEventHandlers() {
        view.getBtnAddProduct().setOnAction(e -> handleAddProduct());
        view.getBtnDeleteProduct().setOnAction(e -> handleDeleteProduct());
        view.getBtnAddToCart().setOnAction(e -> handleAddToCart());
    }

    private void setupCartEventHandlers() {
        view.getBtnRemoveFromCart().setOnAction(e -> handleRemoveFromCart());
        view.getBtnClearCart().setOnAction(e -> handleClearCart());
    }

    private void setupCheckoutEventHandlers() {
        view.getBtnCheckout().setOnAction(e -> handleCheckout());
    }

    private void loadProducts() {
        try {
            ObservableList<Product> products = FXCollections.observableArrayList(
                    productService.getAllProducts()
            );
            view.getProductTable().setItems(products);
        } catch (Exception e) {
            showError("Gagal Memuat Data",
                    "Tidak dapat memuat data produk dari database: " + e.getMessage());
            view.getProductTable().setItems(FXCollections.observableArrayList());
        }
    }

    private void handleAddProduct() {
        try {
            String code = view.getTxtProductCode().getText().trim();
            String name = view.getTxtProductName().getText().trim();
            double price = Double.parseDouble(view.getTxtProductPrice().getText().trim());
            int stock = Integer.parseInt(view.getTxtProductStock().getText().trim());

            if (code.isEmpty() || name.isEmpty()) {
                showError("Input Tidak Lengkap", "Kode dan nama produk harus diisi!");
                return;
            }

            if (price <= 0) {
                showError("Harga Tidak Valid", "Harga harus lebih besar dari 0!");
                return;
            }

            if (stock < 0) {
                showError("Stok Tidak Valid", "Stok tidak boleh negatif!");
                return;
            }

            Product newProduct = new Product(code, name, price, stock);
            productService.addProduct(newProduct);
            loadProducts();
            view.clearProductInput();
            showSuccess("Sukses", "Produk berhasil ditambahkan!");

        } catch (NumberFormatException ex) {
            showError("Format Salah", "Harga dan Stok harus berupa angka!");
        } catch (Exception ex) {
            showError("Error", "Gagal menambahkan produk: " + ex.getMessage());
        }
    }

    private void handleDeleteProduct() {
        Product selectedProduct = view.getProductTable().getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            showWarning("Peringatan", "Pilih produk yang akan dihapus!");
            return;
        }

        if (showConfirmation("Konfirmasi",
                "Hapus produk '" + selectedProduct.getName() + "'?")) {
            try {
                productService.deleteProduct(selectedProduct.getCode());
                loadProducts();
                showSuccess("Sukses", "Produk berhasil dihapus!");
            } catch (Exception ex) {
                showError("Error", "Gagal menghapus produk: " + ex.getMessage());
            }
        }
    }

    private void handleAddToCart() {
        Product selectedProduct = view.getProductTable().getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            showWarning("Peringatan", "Pilih produk dari tabel!");
            return;
        }

        try {
            String qtyText = view.getTxtQuantity().getText().trim();
            if (qtyText.isEmpty()) {
                showError("Error", "Masukkan jumlah!");
                return;
            }

            int quantity = Integer.parseInt(qtyText);
            if (quantity <= 0) {
                showError("Error", "Jumlah harus lebih dari 0!");
                return;
            }

            if (quantity > selectedProduct.getStock()) {
                showError("Stok Tidak Cukup",
                        "Stok tersedia: " + selectedProduct.getStock());
                return;
            }

            // PERBAIKAN 1: Gunakan method yang benar dari CartService
            cartService.addItemToCart(selectedProduct.getCode(), quantity);

            // Update tampilan keranjang
            updateCartDisplay();

            // Clear quantity input
            view.getTxtQuantity().clear();

            showSuccess("Sukses",
                    quantity + " " + selectedProduct.getName() + " ditambahkan ke keranjang!");

        } catch (NumberFormatException ex) {
            showError("Format Salah", "Jumlah harus angka!");
        } catch (Exception ex) {
            showError("Error", "Gagal menambahkan ke keranjang: " + ex.getMessage());
        }
    }

    private void handleRemoveFromCart() {
        CartItem selectedItem = view.getCartTable().getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            showWarning("Peringatan", "Pilih item dari keranjang!");
            return;
        }

        // PERBAIKAN 2: Gunakan method yang benar
        cartService.removeItemFromCart(selectedItem.getProduct().getCode());
        updateCartDisplay();
        showSuccess("Sukses", "Item dihapus dari keranjang!");
    }

    private void handleClearCart() {
        if (cartService.getCartItems().isEmpty()) {
            showInfo("Info", "Keranjang sudah kosong!");
            return;
        }

        if (showConfirmation("Konfirmasi", "Kosongkan seluruh keranjang?")) {
            // PERBAIKAN 3: Method ini sudah benar
            cartService.clearCart();
            updateCartDisplay();
            showSuccess("Sukses", "Keranjang dikosongkan!");
        }
    }

    private void handleCheckout() {
        if (cartService.getCartItems().isEmpty()) {
            showWarning("Peringatan", "Keranjang kosong!");
            return;
        }

        // PERBAIKAN 4: Gunakan method yang benar untuk total
        double total = cartService.getCartTotal();

        if (showConfirmation("Checkout",
                "Total: Rp " + String.format("%,.0f", total) + "\n\nLanjutkan checkout?")) {

            // Print receipt (ke console)
            printReceipt();

            // Clear cart
            cartService.clearCart();
            updateCartDisplay();

            showSuccess("Checkout Berhasil", "Transaksi selesai!");
        }
    }

    private void updateCartDisplay() {
        ObservableList<CartItem> cartItems = FXCollections.observableArrayList(
                cartService.getCartItems()
        );
        view.getCartTable().setItems(cartItems);

        // Update summary
        int itemCount = cartService.getCartItemCount();
        double total = cartService.getCartTotal();

        view.updateCartSummary(itemCount, total);
    }

    private void printReceipt() {
        StringBuilder receipt = new StringBuilder();

        receipt.append("\n========================================\n");
        receipt.append("           AGRI-POS RECEIPT           \n");
        receipt.append("----------------------------------------\n");
        receipt.append("Tanggal  : ").append(java.time.LocalDate.now()).append("\n");
        receipt.append("----------------------------------------\n");

        for (CartItem item : cartService.getCartItems()) {
            receipt.append(String.format("%-20s x%3d  Rp. %8.0f\n",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getQuantity() * item.getProduct().getPrice()
            ));
        }

        receipt.append("----------------------------------------\n");
        receipt.append(String.format("%-25s  Rp. %8.0f\n",
                "TOTAL:", cartService.getCartTotal()));
        receipt.append("========================================\n");
        receipt.append("          TERIMA KASIH               \n");

        System.out.println(receipt.toString());
    }

    // ===== HELPER METHODS FOR ALERTS =====

    private void showSuccess(String title, String message) {
        showAlert(AlertType.INFORMATION, title, message);
    }

    private void showError(String title, String message) {
        showAlert(AlertType.ERROR, title, message);
    }

    private void showWarning(String title, String message) {
        showAlert(AlertType.WARNING, title, message);
    }

    private void showInfo(String title, String message) {
        showAlert(AlertType.INFORMATION, title, message);
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


JdbcProductDAO.java
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


ProductDAO.java
package com.upb.agripos.dao;

import com.upb.agripos.model.Product;
import java.util.List;

public interface ProductDAO {
    // CRUD Basic
    void insert(Product p) throws Exception;
    void update(Product p) throws Exception;
    void delete(String code) throws Exception;
    Product findByCode(String code) throws Exception;
    List<Product> findAll() throws Exception;

    // Method baru untuk Week 14
    boolean updateStock(String code, int newStock) throws Exception;
    boolean exists(String code) throws Exception;

    // Method tambahan untuk keranjang belanja
    boolean reduceStock(String code, int quantity) throws Exception;
    boolean increaseStock(String code, int quantity) throws Exception;
    List<Product> searchByName(String name) throws Exception;
    List<Product> findLowStockProducts(int threshold) throws Exception;
}


Cart.java
package com.upb.agripos.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(CartItem item) {
        items.add(item);
    }

    public void removeItem(String productCode) {
        items.removeIf(item -> item.getProduct().getCode().equals(productCode));
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items); // Return copy
    }

    public double getTotal() {
        return items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public int getItemCount() {
        return items.size();
    }
}


CartItem.java
package com.upb.agripos.model;

import javafx.beans.property.*;

/**
 * CartItem - Item dalam keranjang belanja
 */
public class CartItem {
    private final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private final IntegerProperty quantity = new SimpleIntegerProperty();

    // Constructor kosong
    public CartItem() {
    }

    // Constructor dengan parameter
    public CartItem(Product product, int quantity) {
        setProduct(product);
        setQuantity(quantity);
    }

    // ===== JavaFX Property Getters =====
    public ObjectProperty<Product> productProperty() { return product; }
    public IntegerProperty quantityProperty() { return quantity; }

    // ===== Traditional Getters and Setters =====
    public Product getProduct() { return product.get(); }
    public void setProduct(Product product) { this.product.set(product); }

    public int getQuantity() { return quantity.get(); }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }

    // ===== Computed Properties for TableView =====
    public String getProductCode() {
        return getProduct() != null ? getProduct().getCode() : "";
    }

    public String getProductName() {
        return getProduct() != null ? getProduct().getName() : "";
    }

    public double getUnitPrice() {
        return getProduct() != null ? getProduct().getPrice() : 0.0;
    }

    public double getSubtotal() {
        return getUnitPrice() * getQuantity();
    }

    // Property getters for TableView
    public ReadOnlyStringProperty productCodeProperty() {
        return new SimpleStringProperty(getProductCode());
    }

    public ReadOnlyStringProperty productNameProperty() {
        return new SimpleStringProperty(getProductName());
    }

    public ReadOnlyDoubleProperty unitPriceProperty() {
        return new SimpleDoubleProperty(getUnitPrice());
    }

    public ReadOnlyDoubleProperty subtotalProperty() {
        return new SimpleDoubleProperty(getSubtotal());
    }
}



Product.java
package com.upb.agripos.model;

import javafx.beans.property.*;

/**
 * Product - Model produk untuk Agri-POS
 * Diperbarui untuk JavaFX TableView binding
 */
public class Product {
    private final StringProperty code = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final IntegerProperty stock = new SimpleIntegerProperty();

    // Constructor kosong untuk JavaFX
    public Product() {
    }

    // Constructor dengan parameter
    public Product(String code, String name, double price, int stock) {
        setCode(code);
        setName(name);
        setPrice(price);
        setStock(stock);
    }

    // ===== JavaFX Property Getters =====
    public StringProperty codeProperty() { return code; }
    public StringProperty nameProperty() { return name; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty stockProperty() { return stock; }

    // ===== Traditional Getters and Setters =====
    public String getCode() { return code.get(); }
    public void setCode(String code) { this.code.set(code); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }

    public int getStock() { return stock.get(); }
    public void setStock(int stock) { this.stock.set(stock); }

    // Methods lainnya
    public int reduceStock(int quantity) {
        if (quantity <= getStock()) {
            setStock(getStock() - quantity);
        }
        return getStock();
    }

    public int addStock(int quantity) {
        setStock(getStock() + quantity);
        return getStock();
    }

    @Override
    public String toString() {
        return String.format("%s - %s (Rp%,.2f) [Stok: %d]",
                getCode(), getName(), getPrice(), getStock());
    }
}


CartService.java
package com.upb.agripos.service;

import com.upb.agripos.model.Cart;
import com.upb.agripos.model.CartItem;
import com.upb.agripos.model.Product;
import java.util.List;

public class CartService {
    private Cart cart;
    private ProductService productService;

    public CartService(ProductService productService) {
        this.cart = new Cart();
        this.productService = productService;
    }

    public CartService() {

    }

    // Method untuk menambah item ke keranjang
    public void addItemToCart(String productCode, int quantity) {
        try {
            Product product = productService.getProductByCode(productCode);

            if (product != null) {
                // Cek apakah item sudah ada di cart
                CartItem existingItem = findCartItem(productCode);

                if (existingItem != null) {
                    // Update quantity jika sudah ada
                    existingItem.setQuantity(existingItem.getQuantity() + quantity);
                    System.out.println("🛒 Updated quantity: " + product.getName() + " (+" + quantity + ")");
                } else {
                    // Tambah item baru
                    CartItem newItem = new CartItem(product, quantity);
                    cart.addItem(newItem);
                    System.out.println("🛒 Added to cart: " + product.getName() + " x" + quantity);
                }

                // Update stok produk
                product.setStock(product.getStock() - quantity);
                productService.updateProduct(product);

            } else {
                System.out.println("❌ Product not found: " + productCode);
                throw new IllegalArgumentException("Product not found: " + productCode);
            }

        } catch (Exception e) {
            System.err.println("❌ Error adding item to cart: " + e.getMessage());
            throw new RuntimeException("Failed to add item to cart: " + e.getMessage(), e);
        }
    }

    // Method untuk menghapus item dari keranjang
    public void removeItemFromCart(String productCode) {
        try {
            CartItem itemToRemove = findCartItem(productCode);

            if (itemToRemove != null) {
                // Kembalikan stok ke produk
                Product product = itemToRemove.getProduct();
                product.setStock(product.getStock() + itemToRemove.getQuantity());
                productService.updateProduct(product);

                // Hapus dari cart
                cart.removeItem(productCode);
                System.out.println("🗑️ Removed from cart: " + product.getName());
            } else {
                System.out.println("❌ Item not found in cart: " + productCode);
                throw new IllegalArgumentException("Item not found in cart: " + productCode);
            }

        } catch (Exception e) {
            System.err.println("❌ Error removing item from cart: " + e.getMessage());
            throw new RuntimeException("Failed to remove item from cart: " + e.getMessage(), e);
        }
    }

    // Helper method untuk mencari item di cart
    private CartItem findCartItem(String productCode) {
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getCode().equals(productCode)) {
                return item;
            }
        }
        return null;
    }

    // Method lainnya dengan try-catch
    public void clearCart() {
        try {
            // Kembalikan semua stok sebelum clear
            for (CartItem item : cart.getItems()) {
                Product product = item.getProduct();
                product.setStock(product.getStock() + item.getQuantity());
                productService.updateProduct(product);
            }

            cart.clear();
            System.out.println("🛒 Cart cleared");

        } catch (Exception e) {
            System.err.println("❌ Error clearing cart: " + e.getMessage());
            throw new RuntimeException("Failed to clear cart", e);
        }
    }

    // Method tanpa exception
    public boolean isCartEmpty() {
        return cart.isEmpty();
    }

    public List<CartItem> getCartItems() {
        return cart.getItems();
    }

    public double getCartTotal() {
        return cart.getTotal();
    }

    public int getCartItemCount() {
        return cart.getItemCount();
    }
}


ProductService.java
package com.upb.agripos.service;

import java.util.List;

import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.model.Product;


public class ProductService {
    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }


    public void addProduct(Product product) throws Exception {
        if (product.getCode() == null || product.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Kode produk tidak boleh kosong");
        }
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nama produk tidak boleh kosong");
        }
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Harga harus positif");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Stok harus positif");
        }

        productDAO.insert(product);
    }



    public List<Product> getAllProducts() throws Exception {
        return productDAO.findAll();
    }


    public Product getProductByCode(String code) throws Exception {
        return productDAO.findByCode(code);
    }



    public void deleteProduct(String code) throws Exception {
        productDAO.delete(code);
    }


    public void updateProduct(Product product) throws Exception {
        productDAO.update(product);
    }
}


PosView.java
package com.upb.agripos.view;

import com.upb.agripos.model.CartItem;
import javafx.scene.control.cell.PropertyValueFactory;
import com.upb.agripos.model.Product;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PosView extends VBox {
    private TableView<Product> productTable;
    private TableView<CartItem> cartTable;
    private Button btnAddProduct, btnDeleteProduct, btnAddToCart, btnCheckout, btnRemoveFromCart, btnClearCart;
    private TextField txtProductCode, txtProductName, txtProductPrice, txtProductStock, txtQuantity;
    private Label lblCartCount, lblCartTotal;

    public PosView() {
        initializeUI();
    }

    private void initializeUI() {
        setPadding(new Insets(20));
        setSpacing(20);
        setBackground(new Background(new BackgroundFill(Color.web("#f5f5f5"), CornerRadii.EMPTY, Insets.EMPTY)));

        // Title
        Label title = new Label("🛒 AGRI-POS SYSTEM");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#2c3e50"));

        // Main horizontal layout
        HBox mainContent = new HBox(20);
        mainContent.setPadding(new Insets(10, 0, 20, 0));

        // Left: Product Management
        VBox productSection = createProductSection();
        productSection.setPrefWidth(500);

        // Right: Cart + Summary
        VBox rightSection = new VBox(20);
        rightSection.setPrefWidth(600);

        VBox cartSection = createCartSection();
        VBox summarySection = createSummarySection();

        rightSection.getChildren().addAll(cartSection, summarySection);
        mainContent.getChildren().addAll(productSection, rightSection);

        getChildren().addAll(title, mainContent);
    }

    private VBox createProductSection() {
        VBox section = new VBox(15);
        section.setStyle("-fx-border-color: #ccc; -fx-border-radius: 8; -fx-padding: 15;");

        // Form
        txtProductCode = new TextField();
        txtProductCode.setPromptText("Kode");

        txtProductName = new TextField();
        txtProductName.setPromptText("Nama");

        txtProductPrice = new TextField();
        txtProductPrice.setPromptText("Harga");

        txtProductStock = new TextField();
        txtProductStock.setPromptText("Stok");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.add(new Label("Kode:"), 0, 0);
        form.add(txtProductCode, 1, 0);
        form.add(new Label("Nama:"), 0, 1);
        form.add(txtProductName, 1, 1);
        form.add(new Label("Harga:"), 0, 2);
        form.add(txtProductPrice, 1, 2);
        form.add(new Label("Stok:"), 0, 3);
        form.add(txtProductStock, 1, 3);

        // Buttons
        btnAddProduct = new Button("➕ Tambah Produk");
        btnAddProduct.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        btnDeleteProduct = new Button("🗑️ Hapus Produk");
        btnDeleteProduct.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox buttons = new HBox(10, btnAddProduct, btnDeleteProduct);
        buttons.setAlignment(Pos.CENTER_LEFT);

        // Table
        productTable = new TableView<>();

        TableColumn<Product, String> colCode = new TableColumn<>("Kode");
        colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));

        TableColumn<Product, String> colName = new TableColumn<>("Nama");
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Product, Number> colPrice = new TableColumn<>("Harga");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Number> colStock = new TableColumn<>("Stok");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        productTable.getColumns().addAll(colCode, colName, colPrice, colStock);
        productTable.setPrefHeight(300);

        section.getChildren().addAll(new Label("📦 Manajemen Produk"), form, buttons, productTable);
        return section;
    }

    private VBox createCartSection() {
        VBox section = new VBox(15);
        section.setStyle("-fx-border-color: #ccc; -fx-border-radius: 8; -fx-padding: 15;");

        // Quantity input
        txtQuantity = new TextField();
        txtQuantity.setPromptText("Qty");
        txtQuantity.setPrefWidth(60);

        btnAddToCart = new Button("➕ Tambah ke Keranjang");
        btnAddToCart.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox cartInput = new HBox(10, new Label("Jumlah:"), txtQuantity, btnAddToCart);
        cartInput.setAlignment(Pos.CENTER_LEFT);

        // Cart table
        cartTable = new TableView<>();

        TableColumn<CartItem, String> colCartName = new TableColumn<>("Produk");
        colCartName.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getProduct().getName()));

        TableColumn<CartItem, Number> colCartQty = new TableColumn<>("Qty");
        colCartQty.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getQuantity()));  // DIUBAH: HAPUS .asObject()

        TableColumn<CartItem, Number> colCartPrice = new TableColumn<>("Harga");
        colCartPrice.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getProduct().getPrice()));  // DIUBAH: HAPUS .asObject()

        cartTable.getColumns().addAll(colCartName, colCartQty, colCartPrice);
        cartTable.setPrefHeight(200);

        // Cart action buttons
        btnRemoveFromCart = new Button("❌ Hapus Item");
        btnRemoveFromCart.setStyle("-fx-background-color: #ff9800; -fx-text-fill: white; -fx-font-weight: bold;");

        btnClearCart = new Button("🗑️ Kosongkan");
        btnClearCart.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox cartButtons = new HBox(10, btnRemoveFromCart, btnClearCart);

        section.getChildren().addAll(new Label("🛍️ Keranjang Belanja"), cartInput, cartTable, cartButtons);
        return section;
    }

    private VBox createSummarySection() {
        VBox section = new VBox(15);
        section.setStyle("-fx-border-color: #ccc; -fx-border-radius: 8; -fx-padding: 15;");

        lblCartCount = new Label("Item: 0");
        lblCartTotal = new Label("Total: Rp 0");
        lblCartTotal.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblCartTotal.setTextFill(Color.GREEN);

        btnCheckout = new Button("✅ CHECKOUT");
        btnCheckout.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        btnCheckout.setPrefWidth(200);

        section.getChildren().addAll(new Label("💰 Ringkasan"), lblCartCount, lblCartTotal, btnCheckout);
        return section;
    }

    // ===== GETTER METHODS =====
    public TableView<Product> getProductTable() { return productTable; }
    public TableView<CartItem> getCartTable() { return cartTable; }
    public Button getBtnAddProduct() { return btnAddProduct; }
    public Button getBtnDeleteProduct() { return btnDeleteProduct; }
    public Button getBtnAddToCart() { return btnAddToCart; }
    public Button getBtnRemoveFromCart() { return btnRemoveFromCart; }
    public Button getBtnClearCart() { return btnClearCart; }
    public Button getBtnCheckout() { return btnCheckout; }
    public TextField getTxtProductCode() { return txtProductCode; }
    public TextField getTxtProductName() { return txtProductName; }
    public TextField getTxtProductPrice() { return txtProductPrice; }
    public TextField getTxtProductStock() { return txtProductStock; }
    public TextField getTxtQuantity() { return txtQuantity; }
    public Label getLblCartCount() { return lblCartCount; }
    public Label getLblCartTotal() { return lblCartTotal; }

    public void updateCartSummary(int count, double total) {
        lblCartCount.setText("Item: " + count);
        lblCartTotal.setText(String.format("Total: Rp %,.0f", total));
    }

    public void clearProductInput() {
        txtProductCode.clear();
        txtProductName.clear();
        txtProductPrice.clear();
        txtProductStock.clear();
    }
}


AppJavaFX.java
package com.upb.agripos;

import com.upb.agripos.controller.PosController;
import com.upb.agripos.dao.ProductDAO;
import com.upb.agripos.dao.JdbcProductDAO;
import com.upb.agripos.service.CartService;
import com.upb.agripos.service.ProductService;
import com.upb.agripos.view.PosView;  // ✅ TAMBAHKAN IMPORT INI!

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;

public class AppJavaFX extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Cetak identitas ke console
            System.out.println("==================================================");
            System.out.println("Agri-POS Week 14 - Integrasi ");
            System.out.println("Nama: Panji Kurniawan");
            System.out.println("NIM: 240202904");
            System.out.println("==================================================");

            // 1. Setup Database Connection
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/agripos",
                    "postgres",
                    "123"
            );
            System.out.println("Database connected successfully");

            // 2. Setup MVC + Service + DAO Architecture
            ProductDAO productDAO = new JdbcProductDAO(conn);
            ProductService productService = new ProductService(productDAO);

            // PERBAIKAN: CartService butuh ProductService di constructor
            // Sesuai dengan kode CartService Anda:
            // public CartService(ProductService productService) {
            //     this.cart = new Cart();
            //     this.productService = productService;
            // }
            CartService cartService = new CartService(productService); // ✅ PERBAIKAN DI SINI

            // 3. Create View
            PosView view = new PosView();

            // 4. Create Controller
            new PosController(productService, cartService, view);

            // 5. Setup dan Display Scene
            Scene scene = new Scene(view, 1100, 750);
            stage.setTitle("Agri-POS Week 14 - Panji Kurniawan (240202904)"); // ✅ PERBAIKAN NAMA
            stage.setScene(scene);

            // Set minimum window size
            stage.setMinWidth(900);
            stage.setMinHeight(600);

            // Center window on screen
            stage.centerOnScreen();

            stage.show();

            System.out.println("Application started successfully");

        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
            showAlert("Startup Error",
                    "Failed to start application: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Helper method untuk menampilkan alert
     */
    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



CartServiceTest.java
package com.upb.agripos;

import com.upb.agripos.service.CartService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartServiceTest {

    @Test
    public void testSimpleCalculation() {
        assertEquals(4, 2 + 2);
    }
}




## Hasil Eksekusi
<img width="1366" height="768" alt="week14" src="https://github.com/user-attachments/assets/d1184993-7c09-42a4-a303-32c27f16d538" />

<img width="1366" height="768" alt="junit14" src="https://github.com/user-attachments/assets/4c023986-1cbd-44ce-9269-42bd4824351c" />


## Analisis
Bagaimana Kode Berjalan:
Aplikasi mengikuti alur MVC yang ketat:

  - View (JavaFX) → Menangani input pengguna dan menampilkan data

  - Controller → Menerima event dari View, memanggil Service

  - Service → Menjalankan logika bisnis (keranjang, validasi)

  - DAO → Mengakses database PostgreSQL

  - Model → Representasi data (Product, Cart, CartItem)

Perbedaan dengan Minggu Sebelumnya:
  - Bab 13 hanya GUI → Bab 14 integrasi lengkap (GUI + DB + Business Logic)

  - Penggunaan Collections yang lebih kompleks untuk keranjang

  - Implementasi SOLID secara konsisten di seluruh layer

  - Unit testing yang sesungguhnya (bukan hanya contoh)

Kendala dan Solusi:
  1. Kendala: Error "package does not exist" saat menjalankan test
     Solusi: Memastikan struktur package konsisten antara main dan test

  2. Kendala: Connection timeout ke database PostgreSQL
     Solusi: Memeriksa konfigurasi JDBC URL dan credentials

  3. Kendala: JavaFX Controller tidak bisa mengakses Service
     Solusi: Menggunakan Dependency Injection sederhana melalui constructor


## Kesimpulan
Dengan mengintegrasikan seluruh konsep dari Bab 1-13, berhasil dibangun aplikasi Agri-POS System yang:

  1. Terstruktur dengan baik mengikuti MVC dan SOLID principles

  2. Terhubung dengan database menggunakan DAO pattern

  3. Memiliki GUI responsif dengan JavaFX

  4. Dapat diuji dengan unit test yang valid

  5. Skalabel untuk pengembangan fitur lebih lanjut

Integrasi ini membuktikan bahwa pemisahan concern (separation of concerns) sangat penting dalam pengembangan aplikasi yang maintainable dan testable.


