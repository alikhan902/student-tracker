package com.alok.studentTracker.Exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IOExceptionTest {

    @Test
    void testIOExceptionCreation() {
        IOException exception = new IOException("File not found");
        
        assertNotNull(exception);
        assertEquals("File not found", exception.getMessage());
    }

    @Test
    void testIOExceptionWithMessage() {
        String message = "Failed to read file";
        IOException exception = new IOException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testIOExceptionIsRuntimeException() {
        IOException exception = new IOException("Test");
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void testIOExceptionThrow() {
        assertThrows(IOException.class, () -> {
            throw new IOException("File write error");
        });
    }

    @Test
    void testIOExceptionVariousMessages() {
        IOException readException = new IOException("Read error");
        IOException writeException = new IOException("Write error");
        
        assertEquals("Read error", readException.getMessage());
        assertEquals("Write error", writeException.getMessage());
    }

    @Test
    void testIOExceptionWithEmptyMessage() {
        IOException exception = new IOException("");
        assertEquals("", exception.getMessage());
    }
}
