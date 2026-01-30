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