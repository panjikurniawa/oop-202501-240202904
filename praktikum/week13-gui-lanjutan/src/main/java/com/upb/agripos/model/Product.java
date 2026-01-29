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
