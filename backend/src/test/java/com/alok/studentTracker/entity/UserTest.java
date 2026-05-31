package com.alok.studentTracker.entity;

import com.alok.studentTracker.entity.type.AuthProviderType;
import com.alok.studentTracker.entity.type.RoleType;
import com.alok.studentTracker.entity.type.StudentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Group testGroup;

    @BeforeEach
    void setUp() {
        testGroup = Group.builder()
                .id(1L)
                .name("Test Group")
                .users(new HashSet<>())
                .trainingSubjects(new HashSet<>())
                .build();

        user = User.builder()
                .id(1L)
                .name("John Doe")
                .username("johndoe")
                .email("john@example.com")
                .password("encodedPassword123")
                .studentType(StudentType.STANDARD)
                .providerType(AuthProviderType.LOCAL)
                .group(testGroup)
                .roles(new HashSet<>(Set.of(RoleType.USER)))
                .build();
    }

    @Test
    void testUserCreation() {
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("johndoe", user.getUsername());
    }

    @Test
    void testUserFields() {
        assertEquals("john@example.com", user.getEmail());
        assertEquals("encodedPassword123", user.getPassword());
        assertEquals(StudentType.STANDARD, user.getStudentType());
        assertEquals(AuthProviderType.LOCAL, user.getProviderType());
        assertEquals(testGroup, user.getGroup());
    }

    @Test
    void testUserWithMultipleRoles() {
        Set<RoleType> roles = new HashSet<>(Set.of(RoleType.USER, RoleType.AUDITOR));
        user.setRoles(roles);
        
        assertTrue(user.getRoles().contains(RoleType.USER));
        assertTrue(user.getRoles().contains(RoleType.AUDITOR));
        assertEquals(2, user.getRoles().size());
    }

    @Test
    void testUserAuthorities() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testUserIsAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void testUserIsAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void testUserIsCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testUserIsEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    void testUserResetToken() {
        user.setResetToken("reset123");
        assertEquals("reset123", user.getResetToken());
    }

    @Test
    void testUserEmailVerificationToken() {
        user.setEmailVerificationToken("verify123");
        assertEquals("verify123", user.getEmailVerificationToken());
    }

    @Test
    void testUserBuilder() {
        User builtUser = User.builder()
                .id(2L)
                .name("Jane Doe")
                .username("janedoe")
                .email("jane@example.com")
                .password("password456")
                .providerType(AuthProviderType.GOOGLE)
                .build();

        assertEquals(2L, builtUser.getId());
        assertEquals("Jane Doe", builtUser.getName());
        assertEquals(AuthProviderType.GOOGLE, builtUser.getProviderType());
    }

    @Test
    void testUserDefaultValues() {
        User newUser = new User();
        assertEquals(StudentType.STANDARD, newUser.getStudentType());
        assertNotNull(newUser.getRoles());
        assertTrue(newUser.getRoles().isEmpty());
    }

    @Test
    void testUserEquality() {
        User user2 = User.builder()
                .id(1L)
                .name("John Doe")
                .username("johndoe")
                .email("john@example.com")
                .password("encodedPassword123")
                .studentType(StudentType.STANDARD)
                .providerType(AuthProviderType.LOCAL)
                .group(testGroup)
                .roles(new HashSet<>(Set.of(RoleType.USER)))
                .build();

        assertEquals(user.getId(), user2.getId());
        assertEquals(user.getUsername(), user2.getUsername());
    }
}
