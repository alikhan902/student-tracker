package com.alok.studentTracker.Exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedExceptionTest {

    @Test
    void testUnauthorizedExceptionCreation() {
        UnauthorizedException exception = new UnauthorizedException("Access denied");
        
        assertNotNull(exception);
        assertEquals("Access denied", exception.getMessage());
    }

    @Test
    void testUnauthorizedExceptionWithMessage() {
        String message = "Invalid credentials";
        UnauthorizedException exception = new UnauthorizedException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testUnauthorizedExceptionIsRuntimeException() {
        UnauthorizedException exception = new UnauthorizedException("Test");
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void testUnauthorizedExceptionThrow() {
        assertThrows(UnauthorizedException.class, () -> {
            throw new UnauthorizedException("User not authenticated");
        });
    }

    @Test
    void testUnauthorizedExceptionVariousMessages() {
        UnauthorizedException authException = new UnauthorizedException("Not authenticated");
        UnauthorizedException permException = new UnauthorizedException("Permission denied");
        
        assertEquals("Not authenticated", authException.getMessage());
        assertEquals("Permission denied", permException.getMessage());
    }

    @Test
    void testUnauthorizedExceptionWithEmptyMessage() {
        UnauthorizedException exception = new UnauthorizedException("");
        assertEquals("", exception.getMessage());
    }
}
