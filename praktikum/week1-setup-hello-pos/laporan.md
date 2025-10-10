# Laporan Praktikum Minggu 1 (sesuaikan minggu ke berapa?)
Topik: Pendahuluan Pemrograman Berorientasi Objek (OOP) – Pendekatan Procedural, Functional, dan OOP

## Identitas
- Nama  : Panji Kurniawan
- NIM   : 240202904
- Kelas : 3IKKA

---

## Tujuan
(Tuliskan tujuan praktikum minggu ini.  
1.	Mahasiswa memahami perbedaan antara pendekatan Procedural, Functional, dan Object-Oriented Programming (OOP).
2.	Mahasiswa mampu membuat program sederhana dengan masing-masing pendekatan tersebut.
3.	Mahasiswa dapat menjalankan program menggunakan IntelliJ IDEA.


## Dasar Teori
(Tuliskan ringkasan teori singkat (3–5 poin) yang mendasari praktikum.   
1.	Pemrograman Prosedural: Program disusun dalam bentuk urutan instruksi yang dieksekusi langkah demi langkah.
2.	Pemrograman Fungsional: Pendekatan dengan fungsi sebagai elemen utama, mengutamakan pengolahan data melalui fungsi.
3.	Pemrograman Berorientasi Objek (OOP): Menggunakan konsep class dan object untuk membangun program yang modular dan mudah dikembangkan.
4.	Class adalah blueprint dari objek; mendefinisikan atribut dan perilaku.
5.	Object adalah instansiasi dari class yang nyata di program.


## Langkah Praktikum
(Tuliskan Langkah-langkah dalam prakrikum, contoh:
1.	Membuka IntelliJ IDEA dan membuat project baru bernama oop-pos-240202904.
2.	Membuat struktur folder seperti berikut:
3.	src/main/java/com/upb/agripos/
4.	Membuat tiga file Java:
o	HelloProcedural.java
o	HelloFunctional.java
o	HelloOOP.java
5.	Menulis kode untuk masing-masing pendekatan.
6.	Menjalankan setiap file dengan klik kanan → Run ‘HelloProcedural.main()’.
7.	Melakukan commit dengan pesan:
8.	feat: add HelloProcedural, HelloFunctional, and HelloOOP examples
9.	Push ke GitHub dengan perintah:
10.	git add .
11.	git commit -m "feat: add HelloProcedural, HelloFunctional, and HelloOOP examples"
12.	git push -u origin main


## Kode Program
(Tuliskan kode utama yang dibuat, contoh:  
🔹 HelloProcedural.java
package com.upb.agripos;

public class HelloProcedural {
    public static void main(String[] args) {
        String nama = "Panji";
        System.out.println("Halo, " + nama + "!");
        System.out.println("Selamat datang di dunia pemrograman Java (Procedural).");
    }
}

🔹 HelloFunctional.java
package com.upb.agripos;

import java.util.function.Function;

public class HelloFunctional {
    public static void main(String[] args) {
        Function<String, String> sapa = (nama) -> "Halo, " + nama + "!";
        System.out.println(sapa.apply("Panji"));
    }
}

🔹 HelloOOP.java
package com.upb.agripos;

class Greeter {
    private String nama;

    public Greeter(String nama) {
        this.nama = nama;
    }

    public void sapa() {
        System.out.println("Halo, " + nama + "!");
        System.out.println("Selamat datang di dunia OOP Java.");
    }
}

public class HelloOOP {
    public static void main(String[] args) {
        Greeter g = new Greeter("Panji");
        g.sapa();
    }
}


## Hasil Eksekusi
(Sertakan screenshot hasil eksekusi program.  
<img width="747" height="441" alt="image" src="https://github.com/user-attachments/assets/8929d2f8-13e3-45af-9e54-1dce503c812e" />
<img width="798" height="448" alt="image" src="https://github.com/user-attachments/assets/11774cb2-5bc7-43ec-a57a-04ddfa25c173" />
<img width="940" height="529" alt="image" src="https://github.com/user-attachments/assets/1c5eb71c-6f1e-4e8e-abcd-cdc22060429f" />

## Analisis
•	Program Procedural mengeksekusi instruksi secara berurutan tanpa pemisahan tanggung jawab.
•	Program Functional menggunakan fungsi anonim (lambda expression) untuk menghasilkan output.
•	Program OOP membungkus data dan perilaku dalam satu kesatuan melalui class dan object.
•	Pendekatan OOP lebih mudah dikembangkan untuk proyek besar karena struktur kode lebih terorganisir.
•	Kendala: Awalnya program tidak bisa dijalankan karena salah memilih Run Configuration.
→ Solusi: Klik kanan pada file yang berisi main() → pilih Run ‘HelloProcedural.main()’.


## Kesimpulan
(Tuliskan kesimpulan dari praktikum minggu ini.  
Dengan memahami perbedaan antara pendekatan procedural, functional, dan OOP, mahasiswa dapat memilih pendekatan yang sesuai dengan kebutuhan program.
Pendekatan OOP memberikan struktur dan modularitas yang lebih baik untuk pengembangan jangka panjang


## Quiz
1.	Apa perbedaan utama antara pendekatan prosedural dan OOP?
Jawaban: Prosedural berfokus pada urutan instruksi, sedangkan OOP berfokus pada objek yang memiliki atribut dan perilaku.
2.	Apa keuntungan menggunakan class dalam OOP?
Jawaban: Class memudahkan pengelolaan kode, memungkinkan enkapsulasi, dan meningkatkan reusabilitas.
3.	Sebutkan tiga konsep utama dalam OOP!
Jawaban: Encapsulation, Inheritance, dan Polymorphism.

