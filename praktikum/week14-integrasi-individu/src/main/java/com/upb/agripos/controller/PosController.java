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