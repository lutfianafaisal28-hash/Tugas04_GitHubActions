package com.stmik;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PendaftaranValidatorTest {

    private PendaftaranValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new PendaftaranValidator();
    }

    @Test
    public void testPath1_NamaKosong() {
        // Arrange-Act
        String result = validator.validasiPendaftaran("", 20, "user@mail.com", true);
        // Assert
        assertEquals("Nama harus diisi", result);
    }

    @Test
    public void testPath1_NamaNull() {
        String result = validator.validasiPendaftaran(null, 20, "user@mail.com", true);
        assertEquals("Nama harus diisi", result);
    }

    @Test
    public void testPath2_UmurKurangDari17() {
        String result = validator.validasiPendaftaran("Budi", 15, "budi@mail.com", true);
        assertEquals("Umur minimal 17 tahun", result);
    }

    @Test
    public void testPath3_EmailTidakValid() {
        String result = validator.validasiPendaftaran("Budi", 20, "budimail.com", true);
        assertEquals("Email tidak valid", result);
    }

    @Test
    public void testPath4_TidakSetujuSyarat() {
        String result = validator.validasiPendaftaran("Budi", 20, "budi@mail.com", false);
        assertEquals("Harus menyetujui syarat dan ketentuan", result);
    }

    @Test
    public void testPath5_SemuaValid() {
        String result = validator.validasiPendaftaran("Budi", 20, "budi@mail.com", true);
        assertEquals("Pendaftaran berhasil", result);
    }
}
