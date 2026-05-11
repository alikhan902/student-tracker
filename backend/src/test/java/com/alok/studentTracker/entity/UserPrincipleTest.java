package com.alok.studentTracker.entity;

import com.alok.studentTracker.entity.type.AuthProviderType;
import com.alok.studentTracker.entity.type.RoleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserPrincipleTest {

    private UserPrinciple userPrinciple;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("John Doe")
                .username("johndoe")
                .email("john@example.com")
                .password("encodedPassword123")
                .providerType(AuthProviderType.LOCAL)
                .roles(new HashSet<>(Set.of(RoleType.USER)))
                .build();

        userPrinciple = new UserPrinciple(testUser);
    }

    @Test
    void testUserPrincipleCreation() {
        assertNotNull(userPrinciple);
        assertEquals(testUser, userPrinciple.getUser());
    }

    @Test
    void testUserPrincipleGetUser() {
        User user = userPrinciple.getUser();
        assertEquals(testUser.getId(), user.getId());
        assertEquals(testUser.getUsername(), user.getUsername());
    }

    @Test
    void testUserPrincipleGetUsername() {
        assertEquals("johndoe", userPrinciple.getUsername());
    }

    @Test
    void testUserPrincipleGetPassword() {
        assertEquals("encodedPassword123", userPrinciple.getPassword());
    }

    @Test
    void testUserPrincipleGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userPrinciple.getAuthorities();
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
    }

    @Test
    void testUserPrincipleIsAccountNonExpired() {
        assertTrue(userPrinciple.isAccountNonExpired());
    }

    @Test
    void testUserPrincipleIsAccountNonLocked() {
        assertTrue(userPrinciple.isAccountNonLocked());
    }

    @Test
    void testUserPrincipleIsCredentialsNonExpired() {
        assertTrue(userPrinciple.isCredentialsNonExpired());
    }

    @Test
    void testUserPrincipleIsEnabled() {
        assertTrue(userPrinciple.isEnabled());
    }

    @Test
    void testUserPrincipleImplementsUserDetails() {
        assertInstanceOf(org.springframework.security.core.userdetails.UserDetails.class, userPrinciple);
    }

    @Test
    void testUserPrincipleWithDifferentUser() {
        User anotherUser = User.builder()
                .id(2L)
                .username("janedoe")
                .password("password456")
                .build();

        UserPrinciple anotherPrinciple = new UserPrinciple(anotherUser);

        assertEquals("janedoe", anotherPrinciple.getUsername());
        assertEquals("password456", anotherPrinciple.getPassword());
    }

    @Test
    void testUserPrincipleWrapsUser() {
        User wrappedUser = userPrinciple.getUser();
        assertEquals(testUser.getName(), wrappedUser.getName());
        assertEquals(testUser.getEmail(), wrappedUser.getEmail());
    }

    @Test
    void testUserPrincipleWithMultipleRoles() {
        User multiRoleUser = User.builder()
                .id(3L)
                .username("admin")
                .password("admin123")
                .roles(new HashSet<>(Set.of(RoleType.ADMIN, RoleType.USER)))
                .build();

        UserPrinciple adminPrinciple = new UserPrinciple(multiRoleUser);
        Collection<? extends GrantedAuthority> authorities = adminPrinciple.getAuthorities();

        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
    }
}
