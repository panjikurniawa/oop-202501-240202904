# Laporan Praktikum Minggu 4 (sesuaikan minggu ke berapa?)
Topik: Polymorphism

## Identitas
- Nama  : Panji Kurniawan
- NIM   : 240202904
- Kelas : 3IKKA

---

## Tujuan
•	memahami konsep Polymorphism dalam pemrograman berorientasi objek.
•	dapat mengimplementasikan Overriding method pada subclass.
• dapat mengaplikasikan Dynamic Binding menggunakan array objek dari superclass.


## Dasar Teori
1.	Polymorphism adalah kemampuan suatu objek untuk memiliki banyak bentuk.
2.	Overriding digunakan untuk menulis ulang method di subclass dengan perilaku yang berbeda dari superclass.
3.	Dynamic Binding memungkinkan Java memilih method yang sesuai berdasarkan tipe objek sebenarnya saat runtime.
4.	Superclass dan subclass digunakan untuk membangun hierarki objek agar kode lebih modular.
5.	Konsep ini mendukung prinsip reusability dan maintainability dalam OOP.


## Langkah Praktikum
1.	Membuat package com.upb.agripos.model dan com.upb.agripos.util.
2.	Membuat class Produk sebagai superclass dengan atribut kode, nama, harga, dan stok.
3.	Membuat subclass:
o	Benih (memiliki atribut tambahan varietas)
o	Pupuk (memiliki atribut tambahan jenis)
o	AlatPertanian (memiliki atribut tambahan material)
4.	Menerapkan method getInfo() pada Produk dan melakukan overriding pada masing-masing subclass.
5.	Membuat class MainPolymorphism untuk menguji implementasi polymorphism dengan array Produk[].
6.	Menjalankan program dan memastikan output sesuai dengan dynamic binding.
7.	Menambahkan informasi “credit by” di akhir output.


## Kode Program
Produk.java

package com.upb.agripos.model;

public class Produk {
    private String kode;
    private String nama;
    private double harga;
    private int stok;

    public Produk(String kode, String nama, double harga, int stok) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public void tambahStok(int jumlah) {
        this.stok += jumlah;
    }

    public void tambahStok(double jumlah) {
        this.stok += (int) jumlah;
    }

    public String getInfo() {
        return "Produk: " + nama + " (Kode: " + kode + ")";
    }
}

Benih.java

package com.upb.agripos.model;

public class Benih extends Produk {
    private String varietas;

    public Benih(String kode, String nama, double harga, int stok, String varietas) {
        super(kode, nama, harga, stok);
        this.varietas = varietas;
    }

    @Override
    public String getInfo() {
        return "Benih: " + super.getInfo() + ", Varietas: " + varietas;
    }
}

MainPolymorphism.java

package com.upb.agripos;

import com.upb.agripos.model.*;
import com.upb.agripos.util.CreditBy;

public class MainPolymorphism {
    public static void main(String[] args) {
        Produk[] daftarProduk = {
            new Benih("BNH-001", "Benih Padi IR64", 25000, 100, "IR64"),
            new Pupuk("PPK-101", "Pupuk Urea", 350000, 40, "Urea"),
            new AlatPertanian("ALT-501", "Cangkul Baja", 90000, 15, "Baja")
        };

        for (Produk p : daftarProduk) {
            System.out.println(p.getInfo()); // Dynamic Binding
        }

        CreditBy.print("<NIM>", "<Nama Mahasiswa>");
    }
}

## Hasil Eksekusi
(Sertakan screenshot hasil eksekusi program.  
<img width="1366" height="768" alt="week 4" src="https://github.com/user-attachments/assets/b394de13-d317-4245-8ac8-1df3e2d9cf2c" />


## Analisis
•	Method getInfo() pada setiap subclass menimpa method di superclass (overriding), menampilkan informasi tambahan sesuai jenis produk.
•	Saat program dijalankan, method yang dipanggil menyesuaikan tipe objek aktual pada array Produk[], inilah contoh Dynamic Binding.
•	Keuntungan pendekatan ini: kode lebih fleksibel, mudah diperluas, dan tetap terstruktur.
•	Kendala yang dihadapi: awalnya terjadi error tipe data karena class Produk dan subclass berbeda package, diselesaikan dengan import com.upb.agripos.model.*;.


## Kesimpulan
  
Dengan menggunakan Polymorphism dan Dynamic Binding, program dapat memanggil method yang sesuai dengan tipe objek aktual tanpa harus mengetahui jenis subclass secara eksplisit. Hal ini membuat kode lebih efisien dan mudah dikembangkan.

## Quiz
1.	Apa perbedaan antara Overloading dan Overriding?Jawaban: Overloading terjadi ketika dua atau lebih method memiliki nama yang sama, tetapi parameter berbeda (jumlah atau tipe datanya). Ini ditentukan saat compile time.
Contoh:
public void tambahStok(int jumlah) { ... }
public void tambahStok(double jumlah) { ... }

Overriding terjadi ketika subclass mengubah implementasi method yang ada di superclass dengan nama, parameter, dan tipe kembalian yang sama. Ini ditentukan saat runtime (dynamic binding).
Contoh:
@Override
public String getInfo() { ... }
 
2. Bagaimana Java menentukan method mana yang dipanggil dalam dynamic binding?Jawaban:
Dalam dynamic binding, Java akan memanggil method yang sesuai dengan tipe objek sebenarnya (runtime type), bukan tipe referensinya.
Jadi, jika sebuah variabel bertipe Produk tetapi berisi objek Pupuk, maka method getInfo() milik class Pupuk yang akan dijalankan, bukan milik Produk.
Contoh:
Produk p = new Pupuk(...);
p.getInfo(); // Memanggil getInfo() milik class Pupuk
  
3. Berikan contoh kasus polymorphism dalam sistem POS selain produk pertanian.Jawaban:
Contoh polymorphism di sistem POS non-pertanian adalah pada toko elektronik.
Misalnya, terdapat superclass Barang dan subclass seperti Laptop, Smartphone, dan Televisi.
Masing-masing mengoverride method getInfo() untuk menampilkan detail spesifik:
Laptop menampilkan jenis prosesor dan RAM.
Smartphone menampilkan kapasitas baterai.
Televisi menampilkan ukuran layar.
Dengan polymorphism, kita bisa menyimpan semua objek tersebut dalam array Barang[] dan memanggil getInfo() tanpa perlu tahu jenis barangnya satu per satu.   

