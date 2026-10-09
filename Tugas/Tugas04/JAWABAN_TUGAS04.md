# JAWABAN TUGAS 04 — Implementasi Unit Testing dengan GitHub Actions
Nama: Lutfiana Faisal Patah | NIM: 3224018 | Kelas: Sistem Informasi

Hasil verifikasi lokal: `mvn test` → Tests run: 26, Failures: 0, Errors: 0, BUILD SUCCESS.

---

## BAGIAN A — GIT & GITHUB DASAR

### Soal 1
**1. Centralized VCS vs Distributed VCS**
- Centralized VCS: satu server pusat menyimpan semua versi/history. Client hanya punya working copy. Harus konek ke server untuk commit/history. Contoh: SVN (Apache Subversion), CVS.
- Distributed VCS: setiap developer punya salinan lengkap repo + history. Bisa commit/branch offline, lalu sync via push/pull. Tidak ada single point of failure. Contoh: Git, Mercurial.
- Perbedaan kunci: lokasi history (pusat vs tiap client), kerja offline (tidak bisa vs bisa), single point of failure (ada vs tidak), branching (berat vs ringan).

**2. 5 Git commands wajib:**
1. `git clone <url>` — menyalin remote repo ke lokal.
2. `git add .` — memasukkan file ke staging area.
3. `git commit -m "pesan"` — menyimpan snapshot staging ke history lokal.
4. `git push origin main` — mengirim commit lokal ke remote.
5. `git pull origin main` — mengambil + menggabungkan perubahan remote ke lokal.
(Tambahan yang dipakai di tugas: `git branch`, `git checkout`/`switch`, `git merge`, `git log --oneline`, `git status`.)

**3. Fungsi `.gitignore`:**
File teks berisi pola path yang harus diabaikan Git (tidak di-track/commit). Untuk mencegah file build/IDE/OS/coverage (`target/`, `.idea/`, `.vscode/`, `*.class`, `.DS_Store`, `*.exec`) masuk repo. Lihat `.gitignore` di repo ini.

### Soal 2 — Praktik (sudah dieksekusi di repo ini)
```bash
# 1-2. Buat repo latihan-git-3224018 di github.com (via web), lalu:
git clone https://github.com/<username>/latihan-git-3224018.git
cd latihan-git-3224018

# 3-5. README + commit + push
# (isi README.md dengan nama + NIM)
git add README.md .gitignore
git commit -m "docs: add README"
git push -u origin main

# 6-7. branch baru + pindah
git branch feature-test
git checkout feature-test
# atau: git checkout -b feature-test

# 8. file test + commit
echo "file test untuk praktik branch feature-test" > test.txt
git add test.txt
git commit -m "test: add test file"

# 9. push branch
git push -u origin feature-test

# 10. merge ke main
git checkout main
git merge feature-test
git push origin main
```
Bukti di repo ini: `git log --oneline` → docs → test → feat → test-fix; `git branch -a` memuat `main` + `feature-test`.

---

## BAGIAN B — UNIT TESTING

### Soal 3 — validasiPendaftaran (5 independent path → 6 @Test)
| # | Input | Expected |
|---|-------|----------|
| P1 | nama="" / null, umur=20, email valid, setuju=true | "Nama harus diisi" |
| P2 | nama="Budi", umur=15 | "Umur minimal 17 tahun" |
| P3 | nama valid, umur=20, email="budimail.com" | "Email tidak valid" |
| P5 | semua valid kecuali setuju=false | "Harus menyetujui syarat dan ketentuan" |
| P-ok | nama="Budi", 20, "budi@mail.com", true | "Pendaftaran berhasil" |
Kode produksi: `Tugas/Tugas04/src/main/java/com/stmik/PendaftaranValidator.java`
Kode test: `Tugas/Tugas04/src/test/java/com/stmik/PendaftaranValidatorTest.java` (6 test, termasuk null).

### Soal 4 — StudentGrade
**1. Cyclomatic Complexity calculateGrade():**
Predicate nodes: `score<0||score>100` (1 if + 1 || = 2), `score>=80`, `>=70`, `>=60`, `>=50` (4) → total decision = 6 → V(G) = decisions + 1 = 7. Dengan rumus E−N+2P juga 7. Catatan: materi slide menyebut 6 dengan menghitung `||` sebagai 1; perhitungan ketat dengan `||` = 7. Keduanya jelaskan di laporan; jumlah test minimal mengikuti V(G).
**2. Independent Path (6-7 path):**
1. score invalid (<0/>100) → throw IllegalArgumentException
2. score>=80 → "A"
3. 70–79 → "B"
4. 60–69 → "C"
5. 50–59 → "D"
6. 0–49 → "E"
**3. JUnit:** `StudentGradeTest.java` — 7 test calculateGrade (2 invalid + 5 grade) + 4 test isPassed (lulus/batas/gagal/invalid). Lihat file.

---

## BAGIAN C — GITHUB ACTIONS

### Soal 5 — Analisis workflow Python CI
1. Nama: `Python CI`
2. Trigger: `push` ke branch `main, develop`; `pull_request` ke `main`.
3. `strategy.matrix` (`python-version: ['3.10','3.11','3.12']`): menjalankan job `test` paralel 3x (satu per versi Python) untuk memastikan kompatibilitas lintas versi.
4. Steps — catatan koreksi: YAML yang diberikan hanya memuat **4 steps**, bukan 5:
   1. Checkout code (`actions/checkout@v4`)
   2. Setup Python ${{ matrix.python-version }} (`actions/setup-python@v5`)
   3. Install dependencies (`pip install pytest pytest-cov`)
   4. Run tests (`pytest --cov=src --cov-report=xml`)
   → Soal meminta 5 steps, jadi ada ketidaksesuaian soal vs YAML; jawab 4 sesuai fakta.

### Soal 6 — Workflow Java (file: `.github/workflows/java-test.yml`)
```yaml
name: Java Unit Test

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  test:
    runs-on: ubuntu-latest
    defaults:
      run:
        working-directory: Tugas/Tugas04
    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Setup JDK 17
        uses: actions/setup-java@v4
        with:
          java-version: '17'
          distribution: 'temurin'
          cache: maven

      - name: Run tests dengan mvn test
        run: mvn -B test

      - name: Generate coverage report dengan mvn jacoco:report
        run: mvn -B jacoco:report

      - name: Upload artifact dengan nama "jacoco-report"
        uses: actions/upload-artifact@v4
        with:
          name: jacoco-report
          path: Tugas/Tugas04/target/site/jacoco/
```
Catatan: `working-directory` + path absolut artifact karena project Maven ada di `Tugas/Tugas04` (sesuai format pengumpulan).

---

## BAGIAN D — COVERAGE (Soal 7)
Dari tabel: calculator 10/0, login 15/2, payment 20/8, utils 5/1, TOTAL 50/11/78%.
1. Total statement = **50** (10+15+20+5).
2. Total miss = **11** (0+2+8+1).
3. Terendah = **src/payment.py, 60%**.
4. Tingkatkan payment.py: jalankan test yang menyentuh 8 baris miss (baca `htmlcov`/laporan baris merah), tambah test untuk branch if/else, exception, dan edge case payment yang belum tercakup; ulangi `pytest --cov` sampai naik.
5. 78% **belum cukup** — target materi 80–90% line/statement coverage. 78% di bawah ambang, apalagi payment.py hanya 60% (risiko bug tinggi). Perlu tambah test hingga ≥80% (ideal ≥90% untuk modul kritis).

---

## BAGIAN E — BONUS BankAccount (Soal 8)
- Produksi: `src/main/java/com/stmik/BankAccount.java` (deposit/withdraw/getBalance + validasi: amount>0, withdraw≤saldo, saldo awal≥0).
- Test: `src/test/java/com/stmik/BankAccountTest.java` — 9 test (saldo awal 0, deposit ok, deposit negatif/nol gagal, withdraw ok, withdraw over-balance gagal, withdraw negatif gagal, konstruktor negatif gagal, konstruktor positif ok).
- CI: workflow sama di atas menjalankan `mvn test` + `jacoco:report` otomatis.
- Lokal: Tests run 26 (6 Pendaftaran + 11 StudentGrade + 9 BankAccount), 0 gagal; JaCoCo LINE 42/44 ≈95% sebelum fix terakhir, 100% setelah tambah test konstruktor positif.
- Output untuk laporan: link repo + screenshot Actions hijau + screenshot `target/site/jacoco/index.html`.

---

## PUSH KE GITHUB (dilakukan user — butuh login)
```bash
cd latihan-git-3224018
# buat repo kosong latihan-git-3224018 di github.com dulu (tanpa README agar tidak konflik)
git remote add origin https://github.com/<username>/latihan-git-3224018.git
git push -u origin main
git push -u origin feature-test
```
Lalu di GitHub: tab Actions → workflow "Java Unit Test" hijau → download artifact `jacoco-report`. Screenshot: VS Code, `mvn test BUILD SUCCESS`, repo + folder Tugas/Tugas04, Actions sukses, coverage. Susun ke PDF `Tugas04_GitHubActions_3224018_LutfianaFaisalPatah.pdf`, upload PDF ke `Tugas/Tugas04/` (opsional), submit link repo ke LMS.
