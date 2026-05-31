package com.alok.studentTracker.Exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorDetailsTest {

    @Test
    void testErrorDetailsCreation() {
        ErrorDetails errorDetails = new ErrorDetails();
        assertNotNull(errorDetails);
        assertNotNull(errorDetails.getTimestamp());
    }

    @Test
    void testErrorDetailsWithParameters() {
        ErrorDetails errorDetails = new ErrorDetails("Bad Request", HttpStatus.BAD_REQUEST);
        
        assertNotNull(errorDetails.getTimestamp());
        assertEquals("Bad Request", errorDetails.getError());
        assertEquals(HttpStatus.BAD_REQUEST, errorDetails.getStatus());
    }

    @Test
    void testErrorDetailsDefaultConstructor() {
        ErrorDetails errorDetails = new ErrorDetails();
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);
        
        assertNotNull(errorDetails.getTimestamp());
        assertTrue(errorDetails.getTimestamp().isAfter(before));
        assertTrue(errorDetails.getTimestamp().isBefore(after));
    }

    @Test
    void testErrorDetailsSetters() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError("Not Found");
        errorDetails.setStatus(HttpStatus.NOT_FOUND);
        
        assertEquals("Not Found", errorDetails.getError());
        assertEquals(HttpStatus.NOT_FOUND, errorDetails.getStatus());
    }

    @Test
    void testErrorDetailsVariousStatuses() {
        ErrorDetails errorDetails1 = new ErrorDetails("Unauthorized", HttpStatus.UNAUTHORIZED);
        ErrorDetails errorDetails2 = new ErrorDetails("Forbidden", HttpStatus.FORBIDDEN);
        ErrorDetails errorDetails3 = new ErrorDetails("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        
        assertEquals(HttpStatus.UNAUTHORIZED, errorDetails1.getStatus());
        assertEquals(HttpStatus.FORBIDDEN, errorDetails2.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorDetails3.getStatus());
    }

    @Test
    void testErrorDetailsTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(now);
        
        assertEquals(now, errorDetails.getTimestamp());
    }

    @Test
    void testErrorDetailsNullError() {
        ErrorDetails errorDetails = new ErrorDetails();
        assertNull(errorDetails.getError());
        assertNull(errorDetails.getStatus());
    }
}
