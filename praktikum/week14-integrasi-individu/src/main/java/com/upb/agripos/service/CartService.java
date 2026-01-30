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