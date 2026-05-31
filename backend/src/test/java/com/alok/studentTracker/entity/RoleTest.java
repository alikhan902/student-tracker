package com.alok.studentTracker.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("ADMIN");
    }

    @Test
    void testRoleCreation() {
        assertNotNull(role);
        assertEquals(1, role.getId());
        assertEquals("ADMIN", role.getName());
    }

    @Test
    void testRoleSetters() {
        role.setId(2);
        role.setName("USER");
        
        assertEquals(2, role.getId());
        assertEquals("USER", role.getName());
    }

    @Test
    void testRoleGetters() {
        assertEquals(1, role.getId());
        assertEquals("ADMIN", role.getName());
    }

    @Test
    void testRoleCreationWithConstructor() {
        Role newRole = new Role();
        assertNotNull(newRole);
    }

    @Test
    void testRoleIdVariations() {
        role.setId(999);
        assertEquals(999, role.getId());
    }

    @Test
    void testRoleNameVariations() {
        role.setName("AUDITOR");
        assertEquals("AUDITOR", role.getName());
    }

    @Test
    void testRoleDefaultValues() {
        Role newRole = new Role();
        assertEquals(0, newRole.getId());
        assertNull(newRole.getName());
    }
}
