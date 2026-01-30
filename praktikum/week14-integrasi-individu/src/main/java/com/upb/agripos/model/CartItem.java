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