package com.alok.studentTracker.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuditorTest {

    private Auditor auditor;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("password123")
                .build();

        auditor = new Auditor(1L, "auditor_user", "auditor_pass", "Auditor Name", testUser);
    }

    @Test
    void testAuditorCreation() {
        assertNotNull(auditor);
        assertEquals(1L, auditor.getId());
        assertEquals("auditor_user", auditor.getUsername());
    }

    @Test
    void testAuditorFields() {
        assertEquals("auditor_pass", auditor.getPassword());
        assertEquals("Auditor Name", auditor.getName());
        assertEquals(testUser, auditor.getUser());
    }

    @Test
    void testAuditorConstructor() {
        Auditor newAuditor = new Auditor();
        assertNull(newAuditor.getId());
        assertNull(newAuditor.getUsername());
    }

    @Test
    void testAuditorWithUser() {
        assertEquals(testUser.getId(), auditor.getUser().getId());
        assertEquals(testUser.getUsername(), auditor.getUser().getUsername());
    }

    @Test
    void testAuditorMultipleInstances() {
        User user2 = User.builder()
                .id(2L)
                .username("testuser2")
                .password("password456")
                .build();

        Auditor auditor2 = new Auditor(2L, "auditor_user2", "auditor_pass2", "Auditor Name 2", user2);

        assertNotEquals(auditor.getId(), auditor2.getId());
        assertNotEquals(auditor.getUsername(), auditor2.getUsername());
    }

    @Test
    void testAuditorGetter() {
        User user = auditor.getUser();
        assertNotNull(user);
        assertEquals(testUser.getId(), user.getId());
    }
}
