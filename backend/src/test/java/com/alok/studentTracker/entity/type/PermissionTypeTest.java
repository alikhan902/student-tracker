package com.alok.studentTracker.entity.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PermissionTypeTest {

    @Test
    void testPermissionTypeValues() {
        PermissionType[] values = PermissionType.values();
        assertTrue(values.length >= 18);
    }

    @Test
    void testExpenseReadPermission() {
        assertEquals("expense:read", PermissionType.EXPENSE_READ.getPermission());
    }

    @Test
    void testExpenseCreatePermission() {
        assertEquals("expense:create", PermissionType.EXPENSE_CREATE.getPermission());
    }

    @Test
    void testExpenseUpdatePermission() {
        assertEquals("expense:update", PermissionType.EXPENSE_UPDATE.getPermission());
    }

    @Test
    void testExpenseDeletePermission() {
        assertEquals("expense:delete", PermissionType.EXPENSE_DELETE.getPermission());
    }

    @Test
    void testIncomePermissions() {
        assertEquals("income:read", PermissionType.INCOME_READ.getPermission());
        assertEquals("income:create", PermissionType.INCOME_CREATE.getPermission());
        assertEquals("income:update", PermissionType.INCOME_UPDATE.getPermission());
        assertEquals("income:delete", PermissionType.INCOME_DELETE.getPermission());
    }

    @Test
    void testBudgetPermissions() {
        assertEquals("budget:read", PermissionType.BUDGET_READ.getPermission());
        assertEquals("budget:create", PermissionType.BUDGET_CREATE.getPermission());
        assertEquals("budget:update", PermissionType.BUDGET_UPDATE.getPermission());
        assertEquals("budget:delete", PermissionType.BUDGET_DELETE.getPermission());
    }

    @Test
    void testCategoryPermissions() {
        assertEquals("category:read", PermissionType.CATEGORY_READ.getPermission());
        assertEquals("category:create", PermissionType.CATEGORY_CREATE.getPermission());
        assertEquals("category:update", PermissionType.CATEGORY_UPDATE.getPermission());
        assertEquals("category:delete", PermissionType.CATEGORY_DELETE.getPermission());
    }

    @Test
    void testUserPermissions() {
        assertEquals("user:read", PermissionType.USER_READ.getPermission());
        assertEquals("user:manage", PermissionType.USER_MANAGE.getPermission());
    }

    @Test
    void testPermissionTypeValueOf() {
        PermissionType perm = PermissionType.valueOf("EXPENSE_READ");
        assertEquals(PermissionType.EXPENSE_READ, perm);
    }

    @Test
    void testAllPermissionsHaveStringFormat() {
        for (PermissionType permission : PermissionType.values()) {
            String perm = permission.getPermission();
            assertNotNull(perm);
            assertTrue(perm.contains(":"));
        }
    }

    @Test
    void testPermissionTypeComparison() {
        assertTrue(PermissionType.EXPENSE_READ == PermissionType.EXPENSE_READ);
        assertNotEquals(PermissionType.EXPENSE_READ, PermissionType.EXPENSE_CREATE);
    }

    @Test
    void testPermissionTypeCount() {
        int count = PermissionType.values().length;
        assertTrue(count >= 18, "Should have at least 18 permission types");
    }
}
