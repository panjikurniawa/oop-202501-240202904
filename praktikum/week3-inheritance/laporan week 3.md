# Laporan Praktikum Minggu 3 (sesuaikan minggu ke berapa?)
Topik: : Inheritance 

## Identitas
- Nama  : Panji Kurniawan
- NIM   : 240202904
- Kelas : 3IKKA

---

## Tujuan
1.	 memahami konsep inheritance (pewarisan class) dalam pemrograman berorientasi objek.
2.	 mampu membuat superclass dan subclass dalam konteks produk pertanian.
3.	 dapat menggunakan super untuk memanggil konstruktor dan method superclass.
4.	 dapat membandingkan perbedaan penggunaan inheritance dengan class tunggal.


## Dasar Teori
(Tuliskan ringkasan teori singkat (3–5 poin) yang mendasari praktikum.  
1.	Inheritance adalah mekanisme dalam OOP yang memungkinkan sebuah class mewarisi atribut dan method dari class lain.
2.	Superclass adalah class induk yang berisi atribut umum, sedangkan Subclass adalah class turunan yang mewarisi dan dapat menambahkan atribut baru.
3.	Kata kunci super digunakan untuk memanggil konstruktor atau method milik superclass.
4.	Dengan inheritance, kode menjadi lebih reusable, efisien, dan mudah dikembangkan.
5.	Hierarki class membantu program lebih terstruktur dan modular.


## Langkah Praktikum
(Tuliskan Langkah-langkah dalam prakrikum, contoh:
1.	Membuat class Produk sebagai superclass berisi atribut: kode, nama, harga, stok.
2.	Membuat subclass:
o	Benih.java → atribut tambahan varietas.
o	Pupuk.java → atribut tambahan jenis.
o	AlatPertanian.java → atribut tambahan material.
3.	Membuat class MainInheritance.java untuk menampilkan objek dari masing-masing subclass.
4.	Menambahkan class CreditBy.java untuk menampilkan identitas mahasiswa.
5.	Menjalankan program dan mengambil screenshot hasil eksekusi.
6.	Melakukan commit dengan pesan:
	week3-inheritance


## Kode Program
(Tuliskan kode utama yang dibuat, contoh:  
Benih.java
package com.upb.agripos.model;

public class Benih extends Produk {
    private String varietas;

    public Benih(String kode, String nama, double harga, int stok, String varietas) {
        super(kode, nama, harga, stok);
        this.varietas = varietas;
    }

    public String getVarietas() { return varietas; }
    public void setVarietas(String varietas) { this.varietas = varietas; }
}

Pupuk.java
package com.upb.agripos.model;

public class Pupuk extends Produk {
    private String jenis;

    public Pupuk(String kode, String nama, double harga, int stok, String jenis) {
        super(kode, nama, harga, stok);
        this.jenis = jenis;
    }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }
}

Alatpertanian.java
package com.upb.agripos.model;

public class AlatPertanian extends Produk {
    private String material;

    public AlatPertanian(String kode, String nama, double harga, int stok, String material) {
        super(kode, nama, harga, stok);
        this.material = material;
    }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
}

MainInheritance.java
package com.upb.agripos;

import com.upb.agripos.model.*;
import com.upb.agripos.util.CreditBy;

public class MainInheritance {
    public static void main(String[] args) {
        Benih b = new Benih("BNH-001", "Benih Padi IR64", 25000, 100, "IR64");
        Pupuk p = new Pupuk("PPK-101", "Pupuk Urea", 350000, 40, "Urea");
        AlatPertanian a = new AlatPertanian("ALT-501", "Cangkul Baja", 90000, 15, "Baja");

        System.out.println("Benih: " + b.getNama() + " Varietas: " + b.getVarietas());
        System.out.println("Pupuk: " + p.getNama() + " Jenis: " + p.getJenis());
        System.out.println("Alat Pertanian: " + a.getNama() + " Material: " + a.getMaterial());

        CreditBy.print("240202904", "Panji Kurniawan");
    }
}

## Hasil Eksekusi
(Sertakan screenshot hasil eksekusi program.  
<img width="1366" height="768" alt="week3" src="https://github.com/user-attachments/assets/42726372-49bf-4c19-8c4f-f033a7c0ad83" />


## Analisis
•	Program menggunakan inheritance untuk mengelompokkan produk berdasarkan jenisnya tanpa mengulang atribut dasar (kode, nama, harga, stok).
•	Superclass Produk menjadi pusat atribut dan method umum, sementara subclass (Benih, Pupuk, AlatPertanian) menambahkan atribut spesifik.
•	Dengan inheritance, program lebih mudah dikembangkan (misalnya menambah subclass baru tanpa mengubah kode lama).
•	Dibandingkan minggu sebelumnya (class tunggal), pendekatan ini lebih modular, hemat kode, dan fleksibel.
•	Kendala yang dihadapi: saat awal terjadi error karena lupa menambahkan super() di konstruktor subclass. Setelah diperbaiki, program berjalan lancar.


## Kesimpulan
Dengan menggunakan konsep inheritance, kita dapat:

1. Menghemat penulisan kode dengan reusability tinggi.

2. Menciptakan struktur program yang lebih rapi dan mudah dipahami.

3. Mengelompokkan class berdasarkan hubungan hierarkis antar objek.

Praktikum ini memperkuat pemahaman tentang pewarisan class dan penggunaan kata kunci super dalam Java


## Quiz
1. Apa keuntungan menggunakan inheritance dibanding membuat class terpisah tanpa hubungan?
   
   Jawaban:
   
   Dengan inheritance, kode menjadi lebih efisien dan mudah dipelihara, karena atribut dan method umum dapat didefinisikan sekali saja di    superclass, kemudian diwarisi oleh subclass. Hal ini menghindari duplikasi kode, meningkatkan reusability, serta memperjelas struktur     dan hubungan antar entitas dalam sistem.

3. Bagaimana cara subclass memanggil konstruktor superclass?
   
   Jawaban:
   
   Subclass memanggil konstruktor superclass menggunakan kata kunci super di dalam konstruktor subclass.
   contoh:
   public Benih(String kode, String nama, double harga, int stok, String varietas) {
    super(kode, nama, harga, stok); // memanggil konstruktor superclass Produk
    this.varietas = varietas;
}

5. Berikan contoh kasus di POS pertanian selain Benih, Pupuk, dan Alat Pertanian yang bisa dijadikan subclass.
   
   Jawaban:
   
   Beberapa contoh lain:

   Pestisida → atribut tambahan: jenis bahan aktif (misal: klorpirifos, abamektin).

   HasilPanen → atribut tambahan: jenis tanaman dan berat total.

   ObatTanaman → atribut tambahan: dosis penggunaan dan bentuk (cair, bubuk).

   PerlengkapanLain → atribut tambahan: kategori (sprayer, selang, ember).
