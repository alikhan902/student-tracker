package com.alok.studentTracker.Security;

import com.alok.studentTracker.entity.type.PermissionType;
import com.alok.studentTracker.entity.type.RoleType;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RolePermissionMappingTest {

    @Test
    void testAdminRolePermissions() {
        Set<SimpleGrantedAuthority> authorities = RolePermissionMapping.getAuthoritiesForRole(RoleType.ADMIN);
        
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void testUserRolePermissions() {
        Set<SimpleGrantedAuthority> authorities = RolePermissionMapping.getAuthoritiesForRole(RoleType.USER);
        
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testAuditorRolePermissions() {
        Set<SimpleGrantedAuthority> authorities = RolePermissionMapping.getAuthoritiesForRole(RoleType.AUDITOR);
        
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_AUDITOR")));
    }

    @Test
    void testAdminHasAllPermissions() {
        Set<SimpleGrantedAuthority> authorities = RolePermissionMapping.getAuthoritiesForRole(RoleType.ADMIN);
        
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("expense:read")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("expense:create")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("expense:update")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("expense:delete")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("user:manage")));
    }

    @Test
    void testUserDoesNotHaveUserManagePermission() {
        Set<SimpleGrantedAuthority> authorities = RolePermissionMapping.getAuthoritiesForRole(RoleType.USER);
        
        assertFalse(authorities.stream().anyMatch(a -> a.getAuthority().equals("user:manage")));
    }

    @Test
    void testAuditorHasOnlyReadPermissions() {
        Set<SimpleGrantedAuthority> authorities = RolePermissionMapping.getAuthoritiesForRole(RoleType.AUDITOR);
        
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("expense:read")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("budget:read")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("income:read")));
        
        assertFalse(authorities.stream().anyMatch(a -> a.getAuthority().equals("expense:create")));
        assertFalse(authorities.stream().anyMatch(a -> a.getAuthority().equals("expense:delete")));
    }

    @Test
    void testAdminHasUserReadPermission() {
        Set<SimpleGrantedAuthority> authorities = RolePermissionMapping.getAuthoritiesForRole(RoleType.ADMIN);
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("user:read")));
    }

    @Test
    void testHasPermissionAdminRole() {
        Set<RoleType> adminRole = Set.of(RoleType.ADMIN);
        
        assertTrue(RolePermissionMapping.hasPermission(adminRole, PermissionType.EXPENSE_DELETE));
        assertTrue(RolePermissionMapping.hasPermission(adminRole, PermissionType.USER_MANAGE));
        assertTrue(RolePermissionMapping.hasPermission(adminRole, PermissionType.BUDGET_CREATE));
    }

    @Test
    void testHasPermissionUserRole() {
        Set<RoleType> userRole = Set.of(RoleType.USER);
        
        assertTrue(RolePermissionMapping.hasPermission(userRole, PermissionType.EXPENSE_CREATE));
        assertFalse(RolePermissionMapping.hasPermission(userRole, PermissionType.USER_MANAGE));
    }

    @Test
    void testHasPermissionAuditorRole() {
        Set<RoleType> auditorRole = Set.of(RoleType.AUDITOR);
        
        assertTrue(RolePermissionMapping.hasPermission(auditorRole, PermissionType.EXPENSE_READ));
        assertFalse(RolePermissionMapping.hasPermission(auditorRole, PermissionType.EXPENSE_CREATE));
    }

    @Test
    void testHasPermissionMultipleRoles() {
        Set<RoleType> multipleRoles = Set.of(RoleType.USER, RoleType.AUDITOR);
        
        assertTrue(RolePermissionMapping.hasPermission(multipleRoles, PermissionType.EXPENSE_READ));
        assertTrue(RolePermissionMapping.hasPermission(multipleRoles, PermissionType.EXPENSE_CREATE));
    }

    @Test
    void testHasPermissionEmptyRoles() {
        Set<RoleType> emptyRoles = Set.of();
        
        assertFalse(RolePermissionMapping.hasPermission(emptyRoles, PermissionType.EXPENSE_READ));
    }

    @Test
    void testUserCanManageBudget() {
        Set<RoleType> userRole = Set.of(RoleType.USER);
        
        assertTrue(RolePermissionMapping.hasPermission(userRole, PermissionType.BUDGET_READ));
        assertTrue(RolePermissionMapping.hasPermission(userRole, PermissionType.BUDGET_CREATE));
        assertTrue(RolePermissionMapping.hasPermission(userRole, PermissionType.BUDGET_UPDATE));
        assertTrue(RolePermissionMapping.hasPermission(userRole, PermissionType.BUDGET_DELETE));
    }

    @Test
    void testAuditorCannotManageCategory() {
        Set<RoleType> auditorRole = Set.of(RoleType.AUDITOR);
        
        assertTrue(RolePermissionMapping.hasPermission(auditorRole, PermissionType.CATEGORY_READ));
        assertFalse(RolePermissionMapping.hasPermission(auditorRole, PermissionType.CATEGORY_CREATE));
        assertFalse(RolePermissionMapping.hasPermission(auditorRole, PermissionType.CATEGORY_DELETE));
    }

    @Test
    void testAdminAuthoritiesContainRolePrefix() {
        Set<SimpleGrantedAuthority> authorities = RolePermissionMapping.getAuthoritiesForRole(RoleType.ADMIN);
        
        assertTrue(authorities.stream()
                .anyMatch(a -> a.getAuthority().startsWith("ROLE_")));
    }

    @Test
    void testAllPermissionsHaveString() {
        for (PermissionType permission : PermissionType.values()) {
            assertNotNull(permission.getPermission());
            assertFalse(permission.getPermission().isEmpty());
        }
    }
}
