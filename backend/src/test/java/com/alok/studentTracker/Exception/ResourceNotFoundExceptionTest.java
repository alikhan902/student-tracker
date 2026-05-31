package com.alok.studentTracker.Exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void testResourceNotFoundExceptionCreation() {
        ResourceNotFoundException exception = new ResourceNotFoundException("User not found");
        
        assertNotNull(exception);
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testResourceNotFoundExceptionWithMessage() {
        String message = "Resource with id 123 not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);
        
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testResourceNotFoundExceptionIsRuntimeException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Test");
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void testResourceNotFoundExceptionThrow() {
        assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException("Group not found");
        });
    }

    @Test
    void testResourceNotFoundExceptionVariousMessages() {
        ResourceNotFoundException userException = new ResourceNotFoundException("User not found");
        ResourceNotFoundException groupException = new ResourceNotFoundException("Group not found");
        
        assertEquals("User not found", userException.getMessage());
        assertEquals("Group not found", groupException.getMessage());
    }

    @Test
    void testResourceNotFoundExceptionWithEmptyMessage() {
        ResourceNotFoundException exception = new ResourceNotFoundException("");
        assertEquals("", exception.getMessage());
    }

    @Test
    void testResourceNotFoundExceptionWithId() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Entity with id 999 not found");
        assertTrue(exception.getMessage().contains("999"));
    }
}
