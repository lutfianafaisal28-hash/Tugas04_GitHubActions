package com.stmik;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentGradeTest {

    private StudentGrade grade;

    @BeforeEach
    public void setUp() {
        grade = new StudentGrade();
    }

    // --- calculateGrade: 6 independent path ---
    @Test
    public void testCalculateGrade_InvalidNegatif() {
        assertThrows(IllegalArgumentException.class, () -> grade.calculateGrade(-5));
    }

    @Test
    public void testCalculateGrade_InvalidLebih100() {
        assertThrows(IllegalArgumentException.class, () -> grade.calculateGrade(101));
    }

    @Test
    public void testCalculateGrade_A() {
        assertEquals("A", grade.calculateGrade(85));
    }

    @Test
    public void testCalculateGrade_B() {
        assertEquals("B", grade.calculateGrade(75));
    }

    @Test
    public void testCalculateGrade_C() {
        assertEquals("C", grade.calculateGrade(65));
    }

    @Test
    public void testCalculateGrade_D() {
        assertEquals("D", grade.calculateGrade(55));
    }

    @Test
    public void testCalculateGrade_E() {
        assertEquals("E", grade.calculateGrade(30));
    }

    // --- isPassed ---
    @Test
    public void testIsPassed_Lulus() {
        assertTrue(grade.isPassed(70));
    }

    @Test
    public void testIsPassed_BatasLulus60() {
        assertTrue(grade.isPassed(60));
    }

    @Test
    public void testIsPassed_TidakLulus() {
        assertFalse(grade.isPassed(50));
    }

    @Test
    public void testIsPassed_Invalid() {
        assertThrows(IllegalArgumentException.class, () -> grade.isPassed(-1));
        assertThrows(IllegalArgumentException.class, () -> grade.isPassed(120));
    }
}
