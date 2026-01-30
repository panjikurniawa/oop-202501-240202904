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