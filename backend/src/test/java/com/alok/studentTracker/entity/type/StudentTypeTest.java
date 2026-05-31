package com.alok.studentTracker.entity.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTypeTest {

    @Test
    void testStudentTypeValues() {
        StudentType[] values = StudentType.values();
        assertTrue(values.length >= 1);
    }

    @Test
    void testStudentTypeStandard() {
        StudentType standard = StudentType.STANDARD;
        assertEquals("STANDARD", standard.name());
    }

    @Test
    void testStudentTypeValueOf() {
        StudentType type = StudentType.valueOf("STANDARD");
        assertEquals(StudentType.STANDARD, type);
    }

    @Test
    void testStudentTypeComparison() {
        assertTrue(StudentType.STANDARD == StudentType.STANDARD);
    }

    @Test
    void testStudentTypeOrdinal() {
        StudentType standard = StudentType.STANDARD;
        assertNotNull(standard.ordinal());
    }

    @Test
    void testAllStudentTypesExist() {
        StudentType[] types = StudentType.values();
        boolean hasStandard = false;

        for (StudentType type : types) {
            if (type == StudentType.STANDARD) {
                hasStandard = true;
                break;
            }
        }

        assertTrue(hasStandard);
    }
}
