package com.alok.studentTracker.Exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {

    @Test
    void testBadRequestExceptionCreation() {
        BadRequestException exception = new BadRequestException("Invalid input");
        
        assertNotNull(exception);
        assertEquals("Invalid input", exception.getMessage());
    }

    @Test
    void testBadRequestExceptionWithMessage() {
        String message = "Bad request: invalid parameters";
        BadRequestException exception = new BadRequestException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testBadRequestExceptionIsRuntimeException() {
        BadRequestException exception = new BadRequestException("Test");
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void testBadRequestExceptionThrow() {
        assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException("Validation failed");
        });
    }

    @Test
    void testBadRequestExceptionMessage() {
        String errorMessage = "Email already exists";
        BadRequestException exception = new BadRequestException(errorMessage);
        
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testBadRequestExceptionWithEmptyMessage() {
        BadRequestException exception = new BadRequestException("");
        assertEquals("", exception.getMessage());
    }

    @Test
    void testBadRequestExceptionWithNull() {
        BadRequestException exception = new BadRequestException(null);
        assertNull(exception.getMessage());
    }
}
