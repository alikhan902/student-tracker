package com.alok.studentTracker.entity.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthProviderTypeTest {

    @Test
    void testAuthProviderTypeValues() {
        AuthProviderType[] values = AuthProviderType.values();
        assertTrue(values.length >= 2);
    }

    @Test
    void testAuthProviderTypeLocal() {
        AuthProviderType local = AuthProviderType.LOCAL;
        assertEquals("LOCAL", local.name());
    }

    @Test
    void testAuthProviderTypeGoogle() {
        AuthProviderType google = AuthProviderType.GOOGLE;
        assertEquals("GOOGLE", google.name());
    }

    @Test
    void testAuthProviderTypeValueOf() {
        AuthProviderType local = AuthProviderType.valueOf("LOCAL");
        assertEquals(AuthProviderType.LOCAL, local);
    }

    @Test
    void testAuthProviderTypeComparison() {
        assertTrue(AuthProviderType.LOCAL == AuthProviderType.LOCAL);
        assertNotEquals(AuthProviderType.LOCAL, AuthProviderType.GOOGLE);
    }

    @Test
    void testAuthProviderTypeOrdinal() {
        AuthProviderType[] types = AuthProviderType.values();
        assertTrue(types.length > 0);
    }

    @Test
    void testAllAuthProviderTypesExist() {
        boolean hasLocal = false;
        boolean hasGoogle = false;

        for (AuthProviderType type : AuthProviderType.values()) {
            if (type == AuthProviderType.LOCAL) hasLocal = true;
            if (type == AuthProviderType.GOOGLE) hasGoogle = true;
        }

        assertTrue(hasLocal && hasGoogle);
    }
}
