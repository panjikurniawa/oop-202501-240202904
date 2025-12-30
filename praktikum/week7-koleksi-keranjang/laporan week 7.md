# Laporan Praktikum Minggu 7 
Topik: Collections dan Implementasi Keranjang Belanja (Shopping Cart) Agri-POS

## Identitas
- Nama  : Panji Kurniawan
- NIM   : 240202904
- Kelas : 3 IKKA

---

## Tujuan
(Tuliskan tujuan praktikum minggu ini.
Tujuan dari praktikum minggu ke-7 ini adalah agar mahasiswa mampu memahami dan menerapkan Java Collections Framework, khususnya penggunaan List (ArrayList) dan Map (HashMap) untuk mengelola data produk dalam sistem keranjang belanja (Shopping Cart) pada aplikasi Agri-POS, serta dapat melakukan operasi tambah, hapus, dan perhitungan total transaksi.


## Dasar Teori
(Tuliskan ringkasan teori singkat (3–5 poin) yang mendasari praktikum.  
1.Java Collections Framework menyediakan struktur data dinamis untuk menyimpan dan mengelola kumpulan objek.

2.List (ArrayList) digunakan untuk menyimpan data secara berurutan dan memungkinkan duplikasi data.

3.Map (HashMap) menyimpan data dalam pasangan key–value dan cocok untuk pengelolaan data berbasis kunci.

4.Collection mempermudah proses penambahan, penghapusan, dan pencarian data.

5.Pemilihan struktur data yang tepat dapat meningkatkan efisiensi program, khususnya pada aplikasi POS.


## Langkah Praktikum
(Tuliskan Langkah-langkah dalam prakrikum, contoh:
1.Membuat package com.upb.agripos.

2.Membuat class Product untuk merepresentasikan data produk (kode, nama, harga).

3.Mengimplementasikan class ShoppingCart menggunakan ArrayList untuk menyimpan produk.

4.(Opsional) Mengimplementasikan ShoppingCartMap menggunakan HashMap untuk menyimpan produk beserta quantity.

5.Membuat class MainCart untuk menjalankan dan menguji program.

6.Menjalankan program dan mencatat hasil eksekusi.

7.Melakukan commit dan push ke repository Git dengan commit message:-


## Kode Program
(Tuliskan kode utama yang dibuat, contoh:  
Product.java

package com.upb.agripos;

public class Product {
    private final String code;
    private final String name;
    private final double price;

    public Product(String code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getPrice() { return price; }
}


ShoppingCart.java

package com.upb.agripos;

import java.util.ArrayList;

public class ShoppingCart {
    private final ArrayList<Product> items = new ArrayList<>();

    public void addProduct(Product p) { items.add(p); }
    public void removeProduct(Product p) { items.remove(p); }

    public double getTotal() {
        double sum = 0;
        for (Product p : items) {
            sum += p.getPrice();
        }
        return sum;
    }

    public void printCart() {
        System.out.println("Isi Keranjang:");
        for (Product p : items) {
            System.out.println("- " + p.getCode() + " " + p.getName() + " = " + p.getPrice());
        }
        System.out.println("Total: " + getTotal());
    }
}


ShoppingCartMap.java

package com.upb.agripos;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCartMap {
    private final Map<Product, Integer> items = new HashMap<>();

    public void addProduct(Product p) { items.put(p, items.getOrDefault(p, 0) + 1); }

    public void removeProduct(Product p) {
        if (!items.containsKey(p)) return;
        int qty = items.get(p);
        if (qty > 1) items.put(p, qty - 1);
        else items.remove(p);
    }

    public double getTotal() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public void printCart() {
        System.out.println("Isi Keranjang (Map):");
        for (Map.Entry<Product, Integer> e : items.entrySet()) {
            System.out.println("- " + e.getKey().getCode() + " " + e.getKey().getName() + " x" + e.getValue());
        }
        System.out.println("Total: " + getTotal());
    }
}


MainCart.java

package com.upb.agripos;

public class MainCart {
    public static void main(String[] args) {
        System.out.println("Hello, I am [Nama]-[NIM] (Week7)");

        Product p1 = new Product("P01", "Beras", 50000);
        Product p2 = new Product("P02", "Pupuk", 30000);

        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(p1);
        cart.addProduct(p2);
        cart.printCart();

        cart.removeProduct(p1);
        cart.printCart();
    }
}



## Hasil Eksekusi
(Sertakan screenshot hasil eksekusi program.  
<img width="1366" height="768" alt="image" src="https://github.com/user-attachments/assets/d5c88730-5281-42fb-9491-9fee2b0373f6" />


## Analisis
(
1.Program berjalan dengan memanfaatkan ArrayList untuk menyimpan objek Product.

2.Method addProduct() digunakan untuk menambah produk ke keranjang, sedangkan removeProduct() untuk menghapus produk.

3.Method getTotal() menghitung total harga dengan menjumlahkan harga setiap produk.

4.Dibandingkan minggu sebelumnya yang masih berfokus pada class dan object, minggu ini lebih menekankan pada pengelolaan banyak objek    menggunakan collection.

5.Kendala yang dihadapi adalah pemahaman penggunaan Map sebagai key–value, namun dapat diatasi dengan memahami konsep getOrDefault() dan iterasi entrySet().



## Kesimpulan
(Tuliskan kesimpulan dari praktikum minggu ini.  
Dari praktikum minggu ke-7 ini dapat disimpulkan bahwa Java Collections Framework sangat membantu dalam pengelolaan data yang bersifat dinamis. Penggunaan ArrayList dan HashMap membuat implementasi keranjang belanja pada sistem Agri-POS menjadi lebih efisien, terstruktur, dan mudah dikembangkan.


## Quiz
1. Jelaskan perbedaan mendasar antara List, Map, dan Set. 
   **Jawaban:**  
   List adalah collection yang menyimpan data secara berurutan (ordered) dan mengizinkan data duplikat. Elemen dapat diakses berdasarkan     indeks.
   Contoh: ArrayList

  Map adalah collection yang menyimpan data dalam bentuk pasangan key–value, di mana key harus unik. Digunakan untuk pencarian data         berdasarkan key.
  Contoh: HashMap

  Set adalah collection yang menyimpan data tanpa duplikasi dan umumnya tidak menjamin urutan data.
  Contoh: HashSet  


2. Mengapa ArrayList cocok digunakan untuk keranjang belanja sederhana? 
   **Jawaban:**
  ArrayList cocok digunakan untuk keranjang belanja sederhana karena:

  Mudah diimplementasikan dan digunakan

  Ukuran data bersifat dinamis (bisa bertambah dan berkurang)

  Mempertahankan urutan data sesuai urutan penambahan produk

  Cocok ketika setiap produk dianggap satu item tanpa pengelolaan jumlah (quantity)  


3. Bagaimana struktur Set mencegah duplikasi data?  
   **Jawaban:**
   Set mencegah duplikasi data dengan:

  Tidak mengizinkan elemen yang sama disimpan lebih dari satu kali

  Menggunakan metode equals() dan hashCode() untuk mengecek kesamaan objek

 Jika data yang sama ditambahkan kembali, Set akan menolaknya secara otomatis
 
   
4. Kapan sebaiknya menggunakan Map dibandingkan List? Jelaskan dengan contoh.
   **Jawaban:**
   Map sebaiknya digunakan ketika:

   Data memiliki hubungan key–value

   Diperlukan pencarian data yang cepat berdasarkan key

   Perlu menyimpan jumlah (quantity) dari suatu data

   Contoh:
   Pada keranjang belanja, Map digunakan untuk menyimpan produk dan jumlahnya:
   
   Map<Product, Integer> cart = new HashMap<>();
   cart.put(p1, 3); // Produk p1 sebanyak 3 buah

   
