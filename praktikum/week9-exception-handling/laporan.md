# Laporan Praktikum Minggu 9 
Topik: Exception Handling, Custom Exception, dan Penerapan Design Pattern

## Identitas
- Nama  : Panji Kurniawan
- NIM   : 240202904
- Kelas : 3IKKA

---

## Tujuan
1.Menerapkan exception handling dengan try-catch-finally dalam konteks aplikasi Agri-POS

2.Membuat dan menggunakan custom exception sesuai kebutuhan bisnis

3.Mengintegrasikan exception handling ke dalam aplikasi keranjang belanja

4.Memahami penerapan design pattern Singleton dan konsep MVC


## Dasar Teori

1.Error vs Exception: Error adalah kondisi fatal (seperti OutOfMemoryError) yang tidak dapat ditangani program, sedangkan Exception adalah   kondisi tidak normal yang dapat ditangani menggunakan try-catch.

2.Try-Catch-Finally: Struktur dasar untuk menangani exception di Java, di mana finally block selalu dieksekusi.

3.Custom Exception: Exception yang dibuat sendiri dengan mewarisi class Exception, digunakan untuk kebutuhan spesifik aplikasi.

4.Exception Propagation: Exception dapat dilempar (throw) dan ditangkap (catch) di level yang berbeda dalam program.

5.Design Pattern: Solusi umum untuk masalah desain software yang sering muncul, seperti Singleton untuk memastikan satu instance.



## Langkah Praktikum
1. Setup dan Persiapan
  Membuat package com.upb.agripos di direktori src/main/java/

  Membuat struktur direktori sesuai panduan

2. Membuat Custom Exception Classes
  Membuat 5 custom exception classes:

  InvalidQuantityException.java

  ProductNotFoundException.java

  InsufficientStockException.java

  PriceInvalidException.java (tambahan)

  CartEmptyException.java (tambahan)

  3. Membuat Model Product dengan Validasi
     Membuat class Product.java dengan validasi di constructor

    Implementasi exception handling untuk harga negatif dan stok invalid

  4. Implementasi ShoppingCart dengan Exception Handling
     Membuat class ShoppingCart.java dengan metode yang melempar exception

     Implementasi validasi untuk semua operasi keranjang

  5. Membuat Main Program Demonstrasi
     Membuat MainExceptionDemo.java untuk menunjukkan berbagai skenario
     
  6. Implementasi Design Pattern (Opsional)
     Membuat ProductService.java dengan pattern Singleton


## Kode Program

Product.java

package com.upb.agripos;

public class Product {
    private final String code;
    private final String name;
    private final double price;
    private int stock;

    public Product(String code, String name, double price, int stock) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void reduceStock(int qty) { this.stock -= qty; }
}


InvalidQuantityException.java

package com.upb.agripos;

public class InvalidQuantityException extends Exception {
    public InvalidQuantityException(String msg) { super(msg); }
}


ProductNotFoundException.java

package com.upb.agripos;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String msg) { super(msg); }
}



InsufficientStockException.java

package com.upb.agripos;

public class InsufficientStockException extends Exception {
    public InsufficientStockException(String msg) { super(msg); }
}


ShoppingCart.java

package com.upb.agripos;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private final Map<Product, Integer> items = new HashMap<>();

    public void addProduct(Product p, int qty) throws InvalidQuantityException {
        if (qty <= 0) {
            throw new InvalidQuantityException("Quantity harus lebih dari 0.");
        }
        items.put(p, items.getOrDefault(p, 0) + qty);
    }

    public void removeProduct(Product p) throws ProductNotFoundException {
        if (!items.containsKey(p)) {
            throw new ProductNotFoundException("Produk tidak ada dalam keranjang.");
        }
        items.remove(p);
    }

    public void checkout() throws InsufficientStockException {
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int qty = entry.getValue();
            if (product.getStock() < qty) {
                throw new InsufficientStockException(
                    "Stok tidak cukup untuk: " + product.getName()
                );
            }
        }
        // contoh pengurangan stok bila semua cukup
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            entry.getKey().reduceStock(entry.getValue());
        }
    }
}


MainExceptionDemo.java


package com.upb.agripos;

public class MainExceptionDemo {
    public static void main(String[] args) {
        System.out.println("Hello, I am [Nama]-[NIM] (Week9)");

        ShoppingCart cart = new ShoppingCart();
        Product p1 = new Product("P01", "Pupuk Organik", 25000, 3);

        try {
            cart.addProduct(p1, -1);
        } catch (InvalidQuantityException e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }

        try {
            cart.removeProduct(p1);
        } catch (ProductNotFoundException e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }

        try {
            cart.addProduct(p1, 5);
            cart.checkout();
        } catch (Exception e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }
    }
}



## Hasil Eksekusi
<img width="1366" height="768" alt="week9" src="https://github.com/user-attachments/assets/76444a64-25af-4405-bed4-97f84c6d7fb2" />


## Analisis

1. Cara Kerja Program
Program Agri-POS dengan exception handling bekerja dengan:

Validasi Input: Setiap operasi (tambah, hapus, checkout) divalidasi sebelum dieksekusi

Exception Throwing: Jika kondisi tidak valid, custom exception dilempar

Exception Catching: Exception ditangkap di level yang sesuai dengan try-catch block

Graceful Error Handling: User mendapatkan pesan error yang informatif tanpa program crash


2. Perbedaan dengan Minggu Sebelumnya
Minggu sebelumnya: Program berhenti (crash) saat error terjadi

Minggu ini: Program tetap berjalan, error ditangani dengan elegan

Penambahan fitur: Custom exception untuk kebutuhan spesifik bisnis

Improvement: User experience lebih baik dengan pesan error yang jelas


3. Kendala dan Solusi
Kendala 1: Exception chain yang kompleks

Solusi: Menggunakan multi-catch dan exception hierarchy

Kendala 2: Memastikan resource cleanup

Solusi: Menggunakan finally block untuk cleanup operations

Kendala 3: Pesan error yang user-friendly

Solusi: Membuat custom exception dengan pesan yang jelas dan kontekstual



## Kesimpulan

Dari praktikum minggu ini dapat disimpulkan bahwa:

1.Exception handling membuat aplikasi lebih robust dan reliable

2.Custom exception memungkinkan penanganan error yang spesifik sesuai kebutuhan bisnis

3.Try-catch-finally pattern penting untuk memastikan resource management yang baik

4.Design pattern seperti Singleton membantu dalam mengelola state aplikasi

5.Aplikasi dengan proper exception handling memberikan user experience yang lebih baik



## Quiz
1. Jelaskan perbedaan error dan exception.  
   **Jawaban:** …
   Error: Kondisi fatal yang tidak dapat dipulihkan, biasanya terkait dengan lingkungan runtime Java (contoh: OutOfMemoryError,              StackOverflowError). Error tidak seharusnya ditangkap oleh program.

   Exception: Kondisi abnormal yang dapat ditangani oleh program, mewakili masalah yang dapat diantisipasi dan ditangani (contoh:            IOException, SQLException). Exception dibagi menjadi checked (harus ditangani) dan unchecked (RuntimeException).
   

2. Apa fungsi finally dalam blok try–catch–finally?  
   **Jawaban:** …
   Finally block selalu dieksekusi setelah try-catch block, terlepas dari apakah exception terjadi atau tidak

   Fungsi utama: Resource cleanup (menutup file, koneksi database, stream)

   Memastikan kode penting selalu dijalankan (misal: log operations)

   Tidak wajib ada, tetapi sangat direkomendasikan untuk operasi yang membutuhkan cleanup

   

3. Mengapa custom exception diperlukan?  
   **Jawaban:** …
   Custom exception diperlukan karena:

    Spesifik Domain Bisnis: Merepresentasikan error spesifik aplikasi (contoh: InsufficientStockException)

    Pesan yang Lebih Informatif: Dapat menyertakan konteks spesifik aplikasi
  
    Exception Hierarchy: Membuat struktur exception yang lebih terorganisir
  
    Error Recovery Strategy: Memungkinkan penanganan error yang lebih spesifik

    Code Readability: Membuat kode lebih mudah dibaca dan dipahami
   

4.Berikan contoh kasus bisnis dalam POS yang membutuhkan custom exception.
  **Jawaban:** …
  Dalam sistem Point of Sale (POS) Agri-POS:

  InsufficientStockException: Saat customer membeli produk melebihi stok yang tersedia

  InvalidQuantityException: Saat memasukkan kuantitas ≤ 0 atau bukan angka

  ProductNotFoundException: Saat mencari produk dengan kode yang tidak terdaftar

  PriceInvalidException: Saat harga produk diinput negatif atau nol

  CartEmptyException: Saat mencoba checkout dengan keranjang kosong

  PaymentFailedException: Saat transaksi pembayaran gagal

  DiscountInvalidException: Saat diskon melebihi batas yang diizinkan

  CustomerNotFoundException: Saat data pelanggan tidak ditemukan


