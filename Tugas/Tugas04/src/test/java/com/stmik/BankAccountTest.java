package com.stmik;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    private BankAccount account;

    @BeforeEach
    public void setUp() {
        account = new BankAccount();
    }

    @Test
    public void testSaldoAwalNol() {
        assertEquals(0.0, account.getBalance());
    }

    @Test
    public void testDepositBerhasil() {
        account.deposit(100000);
        assertEquals(100000.0, account.getBalance());
    }

    @Test
    public void testDepositNegatifGagal() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-5000));
    }

    @Test
    public void testDepositNolGagal() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(0));
    }

    @Test
    public void testWithdrawBerhasil() {
        account.deposit(100000);
        account.withdraw(40000);
        assertEquals(60000.0, account.getBalance());
    }

    @Test
    public void testWithdrawMelebihiSaldoGagal() {
        account.deposit(50000);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(100000));
    }

    @Test
    public void testWithdrawNegatifGagal() {
        account.deposit(50000);
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-1000));
    }

    @Test
    public void testSaldoAwalNegatifGagal() {
        assertThrows(IllegalArgumentException.class, () -> new BankAccount(-1000));
    }
}
