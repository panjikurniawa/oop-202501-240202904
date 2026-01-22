# Laporan Praktikum Minggu 6 
Topik: Desain Sistem dengan UML untuk AgriPOS

## Identitas
- Nama  : Panji Kurniawan
- NIM   : 240202904
- Kelas : 3IKKA

---

## Tujuan
Mahasiswa mampu memahami dan membuat diagram UML (Unified Modeling Language) untuk mendesain sistem perangkat lunak, khususnya sistem Point of Sale (POS) pertanian (Agri-POS), dengan menggunakan berbagai jenis diagram UML seperti Activity Diagram, Class Diagram, Sequence Diagram, dan Use Case Diagram.



## Dasar Teori
1.UML (Unified Modeling Language) adalah bahasa standar untuk memvisualisasikan, menspesifikasikan, membangun, dan mendokumentasikan         artefak sistem perangkat lunak.

2.Activity Diagram digunakan untuk memodelkan alur kerja (workflow) dan proses bisnis dalam sistem.

3.Class Diagram menunjukkan struktur statis sistem dengan kelas-kelas, atribut, operasi, dan hubungan antar kelas.

4.Sequence Diagram menggambarkan interaksi antar objek dalam sistem berdasarkan urutan waktu.

5.Use Case Diagram merepresentasikan fungsionalitas sistem dari perspektif pengguna (aktor).



## Langkah Praktikum
1.Analisis sistem Agri-POS yang meliputi proses checkout, pembayaran, dan manajemen produk.

2.Membuat Activity Diagram untuk alur proses checkout oleh kasir menggunakan PlantUML.

3.Membuat Class Diagram untuk struktur kelas sistem pembayaran dan produk menggunakan PlantUML.

4.Membuat Sequence Diagram untuk interaksi proses pembayaran menggunakan PlantUML.

5.Membuat Use Case Diagram untuk fungsionalitas sistem dari perspektif admin dan kasir.

6.Implementasi ke PlantUML dengan menulis kode sesuai sintaks PlantUML untuk setiap diagram.

7.Menguji dan memvalidasi diagram dengan PlantUML online server.


## Hasil Eksekusi
Activity Diagram
<img width="1655" height="1075" alt="activity diagram 1" src="https://github.com/user-attachments/assets/57e92af1-607a-4e9e-ae79-329d95701fb5" />

use case diagram
<img width="502" height="813" alt="use case diagram 1" src="https://github.com/user-attachments/assets/1544e0cf-c832-4d62-a96f-2ae7f944591f" />

class diagram
<img width="3077" height="1356" alt="class diagram 1" src="https://github.com/user-attachments/assets/44d56154-2d89-436b-a1b0-36186782a055" />

squence diagram
<img width="1261" height="1070" alt="squence diagram 1" src="https://github.com/user-attachments/assets/21ea9e2b-9e77-465c-8856-6e99e643ec11" />



## Analisis
1.Activity Diagram menggambarkan alur lengkap proses checkout mulai dari pemilihan metode pembayaran hingga transaksi selesai, dengan        penanganan kasus error seperti keranjang kosong dan saldo tidak cukup.

2.Class Diagram menunjukkan struktur kelas sistem dengan pola desain Factory Method untuk pembuatan PaymentMethod, menerapkan prinsip        enkapsulasi dan interface segregation.

3.Sequence Diagram memvisualisasikan interaksi runtime antara aktor (Kasir) dan komponen sistem selama proses pembayaran.

4.Use Case Diagram memberikan gambaran fungsionalitas sistem dari perspektif pengguna (Admin dan Kasir).


## Kesimpulan
1.UML merupakan alat yang powerful untuk memodelkan sistem perangkat lunak sebelum implementasi.

2.Dengan menggunakan empat jenis diagram UML (Activity, Class, Sequence, dan Use Case), kita dapat memahami sistem dari berbagai             perspektif.

3.PlantUML memudahkan pembuatan diagram UML melalui kode teks yang dapat di-versioning.

4.Desain yang baik dengan UML dapat mengurangi kesalahan implementasi dan meningkatkan komunikasi dalam tim pengembangan.

5.Sistem Agri-POS yang dirancang telah menerapkan prinsip OOP dan pola desain yang sesuai.



## Quiz
1.Jelaskan perbedaan aggregation dan composition serta berikan contoh penerapannya pada desain Anda.  
  **Jawaban:** 
  Perbedaan Aggregation dan Composition:
  Aggregation (Has-a)
  Hubungan "has-a" (memiliki)
  Siklus hidup independen
  Anak bisa eksis tanpa induk
  Dilambangkan dengan diamond kosong (◇)

  Composition (Part-of)
  Hubungan "part-of" (bagian dari)
  Siklus hidup terikat
  Anak tidak bisa eksis tanpa induk
  Dilambangkan dengan diamond penuh (◆)

  Contoh Penerapan dalam Desain Agri-POS:

Aggregation (Contoh):
  class PaymentGateway {
  + processPayment()
  + refund()
}

class EWalletPayment {
  - paymentGateway: PaymentGateway
  + EWalletPayment(gateway: PaymentGateway)
}

EWalletPayment memiliki PaymentGateway (aggregation) - PaymentGateway bisa eksis tanpa EWalletPayment.


Composition (Contoh):
class Transaction {
  - id: String
  - order: Order
  - paymentMethod: String
  - amount: double
  - status: String
  - createdAt: Date
  - updatedAt: Date
}

class Order {
  - items: List<OrderItem>
  - total: double
  - date: Date
}

class OrderItem {
  - productId: String
  - quantity: int
  - price: double
}

Transaction terdiri dari Order (composition) - Order tidak bisa eksis tanpa Transaction. Order juga terdiri dari OrderItem (composition) - OrderItem tidak bisa eksis tanpa Order.


2.Bagaimana prinsip Open/Closed dapat memastikan sistem mudah dikembangkan? 
   **Jawaban:** …  
   Prinsip Open/Closed Principle (OCP) menyatakan bahwa entitas perangkat lunak (class, modul, fungsi) harus terbuka untuk ekstensi          tetapi tertutup untuk modifikasi.
   
   Cara OCP memastikan sistem mudah dikembangkan:

 1. Menambahkan Fitur Baru Tanpa Mengubah Kode Existing:
  // Contoh: Menambahkan metode pembayaran baru
public interface PaymentMethod {
    PaymentResult pay(double amount);
}

public class CreditCardPayment implements PaymentMethod {
    public PaymentResult pay(double amount) {
        // Implementasi khusus kartu kredit
    }
}

// Untuk menambah metode pembayaran baru (misal: QRIS)
public class QRISPayment implements PaymentMethod {
    public PaymentResult pay(double amount) {
        // Implementasi QRIS
    }
}
Hanya perlu tambah class baru, tidak perlu ubah PaymentService.

2.Mengurangi Risiko Regresi:

Kode yang sudah tested tidak dimodifikasi

Penambahan fitur tidak mempengaruhi fungsionalitas existing

Mengurangi Risiko Regresi:

Kode yang sudah tested tidak dimodifikasi

Penambahan fitur tidak mempengaruhi fungsionalitas existing

3.Meningkatkan Maintainability:
// PaymentService tidak perlu diubah
public class PaymentService {
    public PaymentResult processPayment(PaymentMethod method, double amount) {
        return method.pay(amount); // Polimorfisme
    }
}

4.Mendukung Pola Desain:

Strategy Pattern untuk algoritma berbeda

Factory Pattern untuk pembuatan objek

Template Method untuk kerangka proses

Dalam Desain Agri-POS:

PaymentMethodFactory menerapkan OCP dengan mudah menambahkan tipe pembayaran baru

IPaymentMethod interface memungkinkan ekstensi tanpa mengubah kode pemroses pembayaran


4.Desain harus menunjukkan pola pikir rekayasa perangkat lunak yang sistematis: modular, reusable, extensible, dan terdokumentasi dengan baik. 
   **Jawaban:** …  
   Dependency Inversion Principle (DIP) memiliki dua prinsip:

Modul tingkat tinggi tidak bergantung pada modul tingkat rendah, keduanya bergantung pada abstraksi

Abstraksi tidak bergantung pada detail, detail bergantung pada abstraksi

Cara DIP Meningkatkan Testability:

1.Mengurangi Keterikatan (Low Coupling):
// Tanpa DIP (Tight Coupling)
public class PaymentService {
    private EWalletPayment payment; // Bergantung pada implementasi konkret
    
    public PaymentService() {
        this.payment = new EWalletPayment(); // Sulit di-test
    }
}

// Dengan DIP (Loose Coupling)
public class PaymentService {
    private IPaymentMethod payment; // Bergantung pada abstraksi
    
    public PaymentService(IPaymentMethod payment) { // Dependency Injection
        this.payment = payment; // Mudah di-test dengan mock
    }
}

2.Memungkinkan Mocking dan Stubbing:
// Contoh Testing dengan Mock
@Test
public void testPaymentSuccess() {
    // Arrange
    IPaymentMethod mockPayment = Mockito.mock(IPaymentMethod.class);
    when(mockPayment.pay(100.0)).thenReturn(new PaymentResult(true, "Success"));
    
    PaymentService service = new PaymentService(mockPayment);
    
    // Act
    PaymentResult result = service.processPayment(100.0);
    
    // Assert
    assertTrue(result.isSuccess());
}

3.Mengisolasi Unit Testing:

Tidak perlu database nyata untuk testing TransactionRepository

Tidak perlu payment gateway nyata untuk testing pembayaran

Testing lebih cepat dan reliable

Contoh Penerapan dalam Desain Agri-POS:
// Abstraksi (Interface)
public interface ITransactionRepository {
    void save(Transaction transaction);
    Transaction findById(String id);
    List<Transaction> findByDateRange(Date start, Date end);
}

// Implementasi konkret
public class TransactionRepositoryImpl implements ITransactionRepository {
    private JdbcTemplate jdbcTemplate;
    
    public void save(Transaction transaction) {
        // Implementasi database nyata
    }
}

// Modul tingkat tinggi bergantung pada abstraksi
public class PaymentService {
    private ITransactionRepository transactionRepo; // Abstraksi, bukan konkret
    
    // Constructor Injection
    public PaymentService(ITransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
    }
    
    public void processTransaction(Transaction transaction) {
        // Business logic...
        transactionRepo.save(transaction); // Bergantung pada abstraksi
    }
}

// Testing dengan Mock
public class PaymentServiceTest {
    @Test
    public void testProcessTransaction() {
        // Mock repository
        ITransactionRepository mockRepo = Mockito.mock(ITransactionRepository.class);
        
        // SUT (System Under Test)
        PaymentService service = new PaymentService(mockRepo);
        
        // Test business logic tanpa database
        service.processTransaction(new Transaction());
        
        // Verifikasi
        verify(mockRepo, times(1)).save(any(Transaction.class));
    }
}

Keuntungan Testability:

Isolated Testing: Setiap komponen bisa di-test secara terpisah

Fast Execution: Testing tanpa infrastruktur eksternal

Deterministic: Hasil test konsisten dan predictable

Easy Maintenance: Test tidak patah ketika implementasi detail berubah


