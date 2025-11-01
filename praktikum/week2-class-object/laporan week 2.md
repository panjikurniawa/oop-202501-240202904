# Laporan Praktikum Minggu 1 (sesuaikan minggu ke berapa?)
Topik: Class dan Object pada Pemrograman Berorientasi Objek (OOP)

## Identitas
- Nama  : Panji kurniawan
- NIM   : 240202904
- Kelas : 3IKKA

---

## Tujuan
(Tuliskan tujuan praktikum minggu ini. 

1.Memahami konsep dasar class dan object dalam pemrograman berorientasi objek (OOP).

2.Mampu membuat class Produk yang memiliki atribut dan metode dengan prinsip enkapsulasi.

3.Mampu membuat class utama (MainProduk) untuk menginstansiasi objek dan menampilkan hasilnya.


## Dasar Teori
(Tuliskan ringkasan teori singkat (3–5 poin) yang mendasari praktikum.  
1.	Class adalah blueprint atau cetak biru dari objek, yang mendefinisikan atribut dan perilaku (method).
2.	Object adalah instansiasi dari sebuah class yang merepresentasikan entitas nyata dalam program.
3.	Enkapsulasi adalah konsep menyembunyikan data (atribut) agar hanya dapat diakses melalui method tertentu.
4.	Dalam OOP, setiap objek memiliki atribut (state) dan method (behavior).
5.	Pemisahan class ke dalam paket (package) seperti model dan util membantu modularisasi dan keteraturan kode.


## Langkah Praktikum
(Tuliskan Langkah-langkah dalam prakrikum, contoh:
1.	Membuka proyek Java di IntelliJ IDEA dengan struktur folder:
2.	src/main/java/com/upb/agripos/
3.	Membuat folder:
o	model untuk class Produk
o	util untuk class CreditBy
4.	Membuat file Produk.java di dalam folder model.
5.	Membuat file CreditBy.java di dalam folder util.
6.	Membuat file MainProduk.java di dalam package com.upb.agripos.
7.	Menjalankan program untuk menampilkan data produk dan identitas pembuat.
8.	Melakukan commit dengan pesan:
9.	feat: tambah class Produk, CreditBy, dan MainProduk


## Kode Program
(Tuliskan kode utama yang dibuat, contoh:  
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

    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }
}

CreditBy.java
package com.upb.agripos.util;

public class CreditBy {
    public static void print(String nim, String nama) {
        System.out.println("\ncredit by: " + nim + " - " + nama);
    }
}

MainProduk.java
package com.upb.agripos;

import com.upb.agripos.model.Produk;
import com.upb.agripos.util.CreditBy;

public class MainProduk {
    public static void main(String[] args) {
        Produk p1 = new Produk("BNH-001", "Benih Padi IR64", 25000, 100);
        Produk p2 = new Produk("PPK-101", "Pupuk Urea 50kg", 350000, 40);
        Produk p3 = new Produk("ALT-501", "Cangkul Baja", 90000, 15);

        System.out.println("Kode: " + p1.getKode() + ", Nama: " + p1.getNama() +
                ", Harga: " + p1.getHarga() + ", Stok: " + p1.getStok());
        System.out.println("Kode: " + p2.getKode() + ", Nama: " + p2.getNama() +
                ", Harga: " + p2.getHarga() + ", Stok: " + p2.getStok());
        System.out.println("Kode: " + p3.getKode() + ", Nama: " + p3.getNama() +
                ", Harga: " + p3.getHarga() + ", Stok: " + p3.getStok());

        // Tampilkan identitas
        CreditBy.print("240202904", "Panji Kurniawan");
    }
}


## Hasil Eksekusi
(Sertakan screenshot hasil eksekusi program.  
<img width="1366" height="768" alt="week2" src="https://github.com/user-attachments/assets/5f8d2e08-63c1-472c-b063-0ad0e0f7dc91" />
Output di console:
Kode: BNH-001, Nama: Benih Padi IR64, Harga: 25000.0, Stok: 100
Kode: PPK-101, Nama: Pupuk Urea 50kg, Harga: 350000.0, Stok: 40
Kode: ALT-501, Nama: Cangkul Baja, Harga: 90000.0, Stok: 15

credit by: 240202904 - Panji Kurniawan


## Analisis

1.Program berhasil dijalankan dengan benar tanpa error.

2.Class Produk menerapkan prinsip enkapsulasi dengan atribut private dan method getter/setter.

3.Class MainProduk menggunakan tiga instance Produk untuk menampilkan data.

4.Package util digunakan untuk membuat class terpisah (CreditBy) agar kode lebih modular.

5.Kendala yang sempat muncul adalah error “class Produk is public should be declared in a file named Produk.java”, yang diselesaikan 
  dengan mengganti nama file sesuai nama class.


## Kesimpulan
Dengan menggunakan konsep class dan object, program menjadi lebih terstruktur, mudah dikembangkan, dan dapat digunakan kembali (reusable).
Mahasiswa dapat memahami bagaimana sebuah objek dibuat dan digunakan dalam program Java berbasis OOP.


## Quiz
1. Mengapa atribut sebaiknya dideklarasikan sebagai private dalam class?
Jawaban:
Atribut sebaiknya dideklarasikan sebagai private agar data dalam class tidak bisa diakses atau diubah secara langsung dari luar class.
Hal ini merupakan prinsip enkapsulasi, yaitu melindungi data dari manipulasi yang tidak diinginkan dan menjaga integritas objek. Dengan private, perubahan nilai atribut hanya bisa dilakukan melalui method resmi seperti getter dan setter, sehingga kontrol terhadap data tetap terjaga.

2. Apa fungsi getter dan setter dalam enkapsulasi?
Jawaban:
•	Getter berfungsi untuk mengambil nilai atribut private agar bisa dibaca oleh class lain.
•	Setter berfungsi untuk mengubah atau menetapkan nilai atribut private dengan cara yang terkontrol.
Dengan getter dan setter, kita bisa menambahkan validasi, aturan logika, atau pembatasan akses sebelum data dibaca atau diubah, sehingga penerapan enkapsulasi menjadi efektif.
3. Bagaimana cara class Produk mendukung pengembangan aplikasi POS yang lebih kompleks?
Jawaban:
Class Produk berfungsi sebagai model dasar (blueprint) untuk mewakili data produk dalam aplikasi POS.
Dengan adanya class ini, pengembang dapat:
•	Mengelola data produk secara terstruktur dan konsisten.
•	Menambahkan fitur lanjutan seperti perhitungan total harga, manajemen stok, kategori produk, atau integrasi dengan transaksi penjualan.
•	Mempermudah pemeliharaan dan pengembangan sistem, karena logika terkait produk tersentral di satu class.
 

