# Laporan Praktikum Minggu 10 
Topik:Design Pattern (Singleton, MVC) dan Unit Testing menggunakan JUnit

## Identitas
- Nama  : Panji Kurniawan 
- NIM   : 240202904
- Kelas : 3IKKA

---

## Tujuan
1. Menjelaskan dan mengimplementasikan Singleton Pattern dengan benar

2. Memahami dan menerapkan Model-View-Controller (MVC) Pattern pada aplikasi sederhana

3. Membuat dan menjalankan unit test menggunakan JUnit

4. Menganalisis manfaat penerapan design pattern dan unit testing terhadap kualitas perangkat lunak



## Dasar Teori
1. Design Pattern adalah solusi desain yang telah teruji untuk menyelesaikan masalah umum dalam pengembangan perangkat lunak. Pattern yang   dipelajari minggu ini: Singleton dan MVC.

2. Singleton Pattern bertujuan untuk menjamin suatu class hanya memiliki satu instance dan menyediakan titik akses global.                    Karakteristiknya:

 - Constructor bersifat private

 - Atribut instance bersifat static

 - Method getInstance() bersifat static
   

3. MVC (Model-View-Controller) Pattern memisahkan aplikasi menjadi tiga komponen:

  - Model: Menangani data dan logika bisnis

  - View: Menangani tampilan dan presentasi data

  - Controller: Menjadi penghubung antara Model dan View
    

4. Unit Testing dengan JUnit digunakan untuk menguji unit terkecil dari kode (method/function). Tujuannya:

  - Memastikan fungsi berjalan sesuai harapan

  - Mendeteksi kesalahan lebih awal

  - Meningkatkan kepercayaan terhadap kode



## Langkah Praktikum
Langkah 1: Setup Project
1. Membuat struktur direktori sesuai panduan

2. Setup Maven project dengan dependency JUnit

3. Membuat package structure: com.upb.agripos

Langkah 2: Implementasi Singleton Pattern
1. Membuat class DatabaseConnection dengan constructor private

2. Mengimplementasikan method getInstance() yang bersifat static

3. Menambahkan method untuk koneksi dan disconnect database

Langkah 3: Implementasi MVC Pattern
1. Model: Membuat class Product dengan atribut dan business logic

2. View: Membuat class ProductView untuk menampilkan data

3. Controller: Membuat class ProductController yang menghubungkan Model dan View

Langkah 4: Implementasi Unit Testing
1. Membuat test class ProductTest di direktori test

2. Menulis 9 test cases dengan berbagai skenario

3. Menggunakan anotasi @Test, @BeforeEach, dan @DisplayName

5. Menggunakan assertion methods dari JUnit

Langkah 5: Integrasi dan Testing
1. Membuat class AppMVC sebagai entry point aplikasi

2. Menjalankan aplikasi untuk demonstrasi kedua pattern

3. Menjalankan unit test dengan JUnit

4. Mengambil screenshot hasil test

   

## Kode Program
product.java
package com.upb.agripos.model;

public class Product {
    private final String code;
    private final String name;

    public Product(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
}


ConsoleView.java
package com.upb.agripos.view;

public class ConsoleView {
    public void showMessage(String message) {
        System.out.println(message);
    }
}

ProductController.java
package com.upb.agripos.controller;

import com.upb.agripos.model.Product;
import com.upb.agripos.view.ConsoleView;

public class ProductController {
    private final Product model;
    private final ConsoleView view;

    public ProductController(Product model, ConsoleView view) {
        this.model = model;
        this.view = view;
    }

    public void showProduct() {
        view.showMessage("Produk: " + model.getCode() + " - " + model.getName());
    }
}


DatabaseConnection.java
package com.upb.agripos.config;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private DatabaseConnection() {}

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}


AppMVC.java
package com.upb.agripos;

import com.upb.agripos.model.Product;
import com.upb.agripos.view.ConsoleView;
import com.upb.agripos.controller.ProductController;

public class AppMVC {
    public static void main(String[] args) {
        System.out.println("Hello, I am [Nama]-[NIM] (Week10)");
        Product product = new Product("P01", "Pupuk Organik");
        ConsoleView view = new ConsoleView();
        ProductController controller = new ProductController(product, view);
        controller.showProduct();
    }
}



## Hasil Eksekusi
(Sertakan screenshot hasil eksekusi program.  
<img width="1366" height="768" alt="week10" src="https://github.com/user-attachments/assets/b4fe1f81-8e18-4908-ac01-3df38a122d9c" />

<img width="811" height="396" alt="junit" src="https://github.com/user-attachments/assets/597a8bd0-3e8a-404e-81bf-97e4821838f8" />


## Analisis
1. Bagaimana Kode Berjalan
    1. Aplikasi dimulai dari AppMVC.main() yang mendemonstrasikan:

    2. Singleton Pattern: Membuktikan bahwa dua referensi DatabaseConnection mengarah ke instance yang sama

    3. MVC Pattern: Menjalankan aplikasi Product management dengan alur: Controller → Model → View

2. Perbedaan dengan Minggu Sebelumnya
    1. Minggu ini: Fokus pada architectural patterns (Singleton, MVC) dan testing

    2. Minggu sebelumnya: Lebih fokus pada konsep OOP dasar dan library management

    3. Peningkatan: Kode lebih terstruktur, reusable, dan testable

3. Kendala dan Solusi
    Kendala 1: Thread safety pada Singleton

   - Solusi: Menggunakan lazy initialization dengan double-check locking (untuk production)

    Kendala 2: Dependency antara komponen MVC

    - Solusi: Menggunakan dependency injection melalui constructor

    Kendala 3: Test coverage yang komprehensif

    - Solusi: Membuat test cases untuk berbagai skenario (normal, edge, negative)

4. Manfaat yang Dirasakan
     1. Singleton: Memastikan resource sharing yang efisien (database connection)

      2. MVC: Memudahkan maintenance dan pengembangan fitur baru

      3. Unit Test: Memberikan confidence saat melakukan perubahan kode



## Kesimpulan
Dari praktikum minggu ini, dapat disimpulkan bahwa:

1. Design Pattern seperti Singleton dan MVC sangat membantu dalam membuat kode yang terstruktur, reusable, dan maintainable

2. Singleton Pattern efektif untuk mengelola resource yang mahal atau harus single instance

3. MVC Pattern memisahkan concerns dengan jelas sehingga memudahkan kolaborasi tim dan testing

4. Unit Testing dengan JUnit merupakan praktik penting untuk menjaga kualitas kode dan mendeteksi bug lebih awal

5. Kombinasi design pattern dan unit testing menghasilkan perangkat lunak yang lebih robust dan scalable

  Penerapan pattern dan testing ini sangat relevan untuk pengembangan Agri-POS yang kompleks dan akan terus berkembang.

  

## Quiz
(1. Mengapa constructor pada Singleton harus bersifat private?  
   **Jawaban:** …
   Constructor harus private untuk mencegah instantiasi dari luar class. Ini menjamin bahwa instance hanya dapat dibuat melalui method       getInstance() yang dikontrol oleh class itu sendiri, sehingga memastikan hanya ada satu instance yang dibuat selama aplikasi berjalan.


2. Jelaskan manfaat pemisahan Model, View, dan Controller. 
   **Jawaban:** …
   - Separation of Concerns: Setiap komponen fokus pada tanggung jawab spesifiknya

   - Reusability: Model dapat digunakan dengan view yang berbeda-beda

   - Testability: Masing-masing komponen dapat diuji secara terpisah

   - Maintainability: Perubahan di satu komponen tidak mempengaruhi komponen lain secara signifikan

   - Team Collaboration: Developer dapat bekerja parallel pada komponen berbeda
   

3. Apa peran unit testing dalam menjaga kualitas perangkat lunak?  
   **Jawaban:** …
   - Early Bug Detection: Mendeteksi kesalahan sejak fase development

   - Regression Prevention: Memastikan perubahan kode tidak merusak fungsionalitas yang sudah ada

   - Documentation: Berfungsi sebagai dokumentasi hidup tentang bagaimana kode seharusnya berperilaku

   - Design Improvement: Memaksa developer untuk membuat kode yang lebih modular dan testable

   - Confidence: Memberikan kepercayaan saat melakukan refactoring atau menambah fitur baru
4. Apa risiko jika Singleton tidak diimplementasikan dengan benar?
   **Jawaban:** …
   - Multiple Instances: Dapat terjadi race condition yang membuat lebih dari satu instance

   - Thread Safety Issues: Dalam environment multithreaded, dapat terjadi inkonsistensi data

   - Testing Difficulties: Sulit di-test karena dependensi global state

   - Memory Leaks: Instance tidak bisa di-garbage collected jika tidak diatur dengan baik

   - Violation of SRP: Class Singleton cenderung mengambil terlalu banyak tanggung jawab

   - Hidden Dependencies: Dependency yang tersembunyi membuat kode sulit dipahami
