package com.alok.studentTracker.entity.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTypeTest {

    @Test
    void testRoleTypeValues() {
        RoleType[] values = RoleType.values();
        assertEquals(3, values.length);
    }

    @Test
    void testRoleTypeAdmin() {
        RoleType admin = RoleType.ADMIN;
        assertEquals("ADMIN", admin.name());
    }

    @Test
    void testRoleTypeUser() {
        RoleType user = RoleType.USER;
        assertEquals("USER", user.name());
    }

    @Test
    void testRoleTypeAuditor() {
        RoleType auditor = RoleType.AUDITOR;
        assertEquals("AUDITOR", auditor.name());
    }

    @Test
    void testRoleTypeValueOf() {
        RoleType admin = RoleType.valueOf("ADMIN");
        assertEquals(RoleType.ADMIN, admin);
    }

    @Test
    void testRoleTypeComparison() {
        assertTrue(RoleType.ADMIN == RoleType.ADMIN);
        assertNotEquals(RoleType.ADMIN, RoleType.USER);
    }

    @Test
    void testRoleTypeOrdinal() {
        assertEquals(0, RoleType.ADMIN.ordinal());
        assertEquals(1, RoleType.USER.ordinal());
        assertEquals(2, RoleType.AUDITOR.ordinal());
    }

    @Test
    void testRoleTypeAllValuesExist() {
        boolean hasAdmin = false;
        boolean hasUser = false;
        boolean hasAuditor = false;

        for (RoleType role : RoleType.values()) {
            if (role == RoleType.ADMIN) hasAdmin = true;
            if (role == RoleType.USER) hasUser = true;
            if (role == RoleType.AUDITOR) hasAuditor = true;
        }

        assertTrue(hasAdmin && hasUser && hasAuditor);
    }
}
