# Laporan Praktikum Minggu 5 (sesuaikan minggu ke berapa?)
Topik: Abstraction (Abstract Class & Interface)

## Identitas
- Nama  : Panji Kurniawan
- NIM   : 240202904
- Kelas : 3IKKA

---

## Tujuan
(Tuliskan tujuan praktikum minggu ini.  
1.  mampu menjelaskan perbedaan antara abstract class dan interface.

2.  mampu mendesain dan mengimplementasikan abstract class dengan method abstrak sesuai kebutuhan.

3.  mampu membuat dan mengimplementasikan interface pada class.

4.  mampu menerapkan konsep multiple inheritance melalui interface di Java.

5.  mampu mendokumentasikan kode program dengan baik.

   

## Dasar Teori
(Tuliskan ringkasan teori singkat (3–5 poin) yang mendasari praktikum.  

1. Abstraksi adalah proses menyembunyikan detail implementasi dan hanya menampilkan fitur penting kepada pengguna.

2. Abstract class digunakan untuk mendefinisikan perilaku dasar (dapat memiliki atribut dan method konkret).

3. Interface digunakan untuk mendefinisikan kontrak perilaku yang dapat diimplementasikan oleh berbagai kelas.

4. Java hanya mendukung single inheritance untuk class, tetapi multiple inheritance dapat dilakukan melalui interface.

5. Penggunaan abstraksi membuat program lebih modular, fleksibel, dan mudah diperluas.

   

## Langkah Praktikum

1. Membuat package pembayaran dan kontrak di dalam folder com.upb.agripos.model.

2. Membuat abstract class Pembayaran yang memiliki method abstrak biaya() dan prosesPembayaran().

4. Membuat subclass Cash dan EWallet yang meng-extend Pembayaran.

5. Membuat interface Validatable dan Receiptable untuk mendefinisikan kontrak validasi() dan cetakStruk().

6. Mengimplementasikan multiple inheritance melalui EWallet (mengimplementasi dua interface).

7. Membuat MainAbstraction.java untuk menjalankan program dan menampilkan hasil proses pembayaran.

8. Menjalankan program dan memastikan hasil output sesuai dengan ketentuan.

9. Melakukan commit dengan pesan:

    week5-abstraction-interface



## Kode Program

Pembayaran.java (abstract)
package com.upb.agripos.model.pembayaran;

public abstract class Pembayaran {
    protected String invoiceNo;
    protected double total;

    public Pembayaran(String invoiceNo, double total) {
        this.invoiceNo = invoiceNo;
        this.total = total;
    }

    public abstract double biaya();               // fee/biaya tambahan
    public abstract boolean prosesPembayaran();   // proses spesifik tiap metode

    public double totalBayar() {
        return total + biaya();
    }

    public String getInvoiceNo() { return invoiceNo; }
    public double getTotal() { return total; }
}

Interface: Validatable & Receiptable

package com.upb.agripos.model.kontrak;

public interface Validatable {
    boolean validasi(); // misal validasi OTP/ PIN
}


package com.upb.agripos.model.kontrak;

public interface Receiptable {
    String cetakStruk();
}


Cash.java

package com.upb.agripos.model.pembayaran;

import com.upb.agripos.model.kontrak.Receiptable;

public class Cash extends Pembayaran implements Receiptable {
    private double tunai;

    public Cash(String invoiceNo, double total, double tunai) {
        super(invoiceNo, total);
        this.tunai = tunai;
    }

    @Override
    public double biaya() {
        return 0.0;
    }

    @Override
    public boolean prosesPembayaran() {
        return tunai >= totalBayar(); // sederhana: cukup uang tunai
    }

    @Override
    public String cetakStruk() {
        return String.format("INVOICE %s | TOTAL: %.2f | BAYAR CASH: %.2f | KEMBALI: %.2f",
                invoiceNo, totalBayar(), tunai, Math.max(0, tunai - totalBayar()));
    }
}

EWallet.java

package com.upb.agripos.model.pembayaran;

import com.upb.agripos.model.kontrak.Validatable;
import com.upb.agripos.model.kontrak.Receiptable;

public class EWallet extends Pembayaran implements Validatable, Receiptable {
    private String akun;
    private String otp; // sederhana untuk simulasi

    public EWallet(String invoiceNo, double total, String akun, String otp) {
        super(invoiceNo, total);
        this.akun = akun;
        this.otp = otp;
    }

    @Override
    public double biaya() {
        return total * 0.015; // 1.5% fee
    }

    @Override
    public boolean validasi() {
        return otp != null && otp.length() == 6; // contoh validasi sederhana
    }

    @Override
    public boolean prosesPembayaran() {
        return validasi(); // jika validasi lolos, anggap berhasil
    }

    @Override
    public String cetakStruk() {
        return String.format("INVOICE %s | TOTAL+FEE: %.2f | E-WALLET: %s | STATUS: %s",
                invoiceNo, totalBayar(), akun, prosesPembayaran() ? "BERHASIL" : "GAGAL");
    }
}

MainAbstraction.java

package com.upb.agripos;

import com.upb.agripos.model.pembayaran.*;
import com.upb.agripos.model.kontrak.*;
import com.upb.agripos.util.CreditBy;

public class MainAbstraction {
    public static void main(String[] args) {
        Pembayaran cash = new Cash("INV-001", 100000, 120000);
        Pembayaran ew = new EWallet("INV-002", 150000, "user@ewallet", "123456");

        System.out.println(((Receiptable) cash).cetakStruk());
        System.out.println(((Receiptable) ew).cetakStruk());

    CreditBy.print("[NIM]", "[Nama Mahasiswa]");
    }
}




## Hasil Eksekusi
(Sertakan screenshot hasil eksekusi program.  
<img width="1366" height="768" alt="image" src="https://github.com/user-attachments/assets/b8d32787-0fd2-4c88-8121-6008b91b3b20" />


## Analisis

1. Program berjalan sesuai konsep abstraction, di mana Pembayaran menjadi kelas abstrak yang tidak dapat diinstansiasi langsung.

2. Kelas Cash dan EWallet mengimplementasikan method abstrak sesuai perilaku masing-masing.

3. Konsep interface digunakan untuk memperluas kemampuan (Validatable, Receiptable) tanpa mengganggu hierarki pewarisan.

4. Polimorfisme terlihat saat objek Pembayaran diakses secara generik namun berperilaku sesuai tipe konkret (Cash, EWallet).

5. Kendala kecil: memastikan struktur package benar agar import dapat dikenali di IntelliJ. Setelah folder disusun sesuai path
   com.upb.agripos.model, program berjalan lancar.


## Kesimpulan

Dengan menggunakan konsep abstraksi (abstract class dan interface), program menjadi lebih modular, fleksibel, dan mudah dikembangkan.
Pembayaran sebagai abstract class menyimpan perilaku umum, sedangkan Validatable dan Receiptable sebagai interface memungkinkan penerapan multiple inheritance secara aman di Java.-

## Quiz
1. Jelaskan perbedaan konsep dan penggunaan abstract class dan interface.
   
  Jawaban:

  Abstract class adalah kelas yang dapat memiliki field (state) dan method abstrak maupun non-abstrak (dengan implementasi). Abstract
  class digunakan ketika beberapa kelas memiliki kesamaan perilaku dan atribut dasar, tetapi juga membutuhkan implementasi spesifik di
  masing-masing subclass.

  Interface adalah kumpulan kontrak perilaku (method tanpa implementasi konkret, kecuali default dan static method sejak Java 8).
  Interface digunakan untuk mendefinisikan kemampuan (capability) yang bisa dimiliki oleh berbagai kelas yang tidak harus berada pada
  satu hierarki pewarisan.

2. Mengapa multiple inheritance lebih aman dilakukan dengan interface pada Java?
   
  Jawaban:

  Karena Java tidak mendukung multiple inheritance antar class untuk menghindari konflik pewarisan (seperti diamond problem). Namun,
  interface tidak membawa state (data/field) sehingga tidak menimbulkan ambiguitas pewarisan.
  Dengan kata lain, sebuah class bisa mengimplementasikan banyak interface tanpa risiko konflik karena hanya mewarisi kontrak method,
  bukan implementasi atau variabel yang bisa bentrok.

4. Pada contoh Agri-POS, bagian mana yang paling tepat menjadi abstract class dan mana yang menjadi interface? Jelaskan alasannya.
   
  Jawaban:

  Abstract class yang tepat: Pembayaran
  Karena Pembayaran memiliki state (invoiceNo, total) dan perilaku umum (totalBayar()) yang dibagikan ke semua jenis pembayaran, tetapi     detail biaya() dan prosesPembayaran() berbeda di setiap subclass (Cash, EWallet).
  → Cocok menjadi abstract class karena menyimpan logika dan atribut dasar.

  Interface yang tepat: Validatable dan Receiptable
  Karena kedua interface tersebut hanya mendefinisikan kontrak perilaku (misalnya validasi() dan cetakStruk()), tanpa menyimpan state
  atau implementasi dasar.
  → Cocok sebagai interface karena bisa digunakan lintas jenis pembayaran, bahkan oleh class lain di masa depan (misalnya TransferBank,
  KartuKredit, dll).
