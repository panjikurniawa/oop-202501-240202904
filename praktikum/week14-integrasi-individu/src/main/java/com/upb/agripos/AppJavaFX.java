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