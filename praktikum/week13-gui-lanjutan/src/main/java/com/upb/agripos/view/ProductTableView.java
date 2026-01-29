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
