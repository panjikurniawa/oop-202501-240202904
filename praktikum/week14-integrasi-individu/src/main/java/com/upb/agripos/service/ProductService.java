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