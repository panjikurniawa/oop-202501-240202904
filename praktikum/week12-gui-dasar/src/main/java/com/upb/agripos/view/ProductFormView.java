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