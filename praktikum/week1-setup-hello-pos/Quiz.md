Quiz
1. Apakah OOP selalu lebih baik dari prosedural?
   Jawaban: Tidak selalu.
   OOP (Object-Oriented Programming) lebih baik jika aplikasi kompleks, memiliki banyak entitas, dan membutuhkan modularitas tinggi.
   Namun, untuk program kecil dan sederhana, paradigma **prosedural** bisa lebih efisien dan mudah dipahami.


2. Kapan functional programming lebih cocok digunakan dibanding OOP atau prosedural?
   Jawaban: Functional programming lebih cocok saat kita membutuhkan proses komputasi yang bersifat stateless dan parallel, seperti pada pemrosesan data besar (big data), transformasi data, atau analisis matematika.
   Paradigma ini meminimalkan efek samping dan membuat kode lebih mudah diuji.


3. Bagaimana paradigma (prosedural, OOP, fungsional) memengaruhi maintainability dan scalability aplikasi?
   Jawaban:

   * Prosedural: mudah dibuat, tapi sulit dirawat saat program makin besar.
   * OOP: memudahkan maintainability karena kode terorganisasi dalam kelas dan objek; juga mendukung scalability dengan pewarisan dan polimorfisme.
   * Fungsional: meningkatkan maintainability dengan kode yang bebas efek samping (pure functions), dan mudah di-scale melalui paralelisasi.


4. Mengapa OOP lebih cocok untuk mengembangkan aplikasi POS dibanding prosedural?
   Jawaban: Karena aplikasi POS (Point of Sale) memiliki banyak entitas nyata seperti produk, pelanggan, transaksi, dan karyawan yang bisa dimodelkan sebagai objek.
   OOP memudahkan pengelompokan data dan perilaku tiap entitas serta memungkinkan pengembangan fitur baru tanpa mengubah banyak kode lama.


5. Bagaimana paradigma fungsional dapat membantu mengurangi kode berulang (boilerplate code)?
   Jawaban: Dengan memanfaatkan fungsi tingkat tinggi (higher-order functions) dan komposisi fungsi, paradigma fungsional memungkinkan kita membuat logika umum yang dapat digunakan kembali tanpa menulis kode serupa berulang kali.
   Contohnya penggunaan `map()`, `filter()`, atau `reduce()`.
