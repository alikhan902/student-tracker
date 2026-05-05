package com.alok.studentTracker.entity;

import com.alok.studentTracker.Security.RolePermissionMapping;
import com.alok.studentTracker.entity.type.AuthProviderType;
import com.alok.studentTracker.entity.type.RoleType;
import com.alok.studentTracker.entity.type.StudentType;
import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.net.ProtocolFamily;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
@Table(name = "app_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private StudentType studentType = StudentType.STANDARD;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)                  // ✅ email should be unique
    private String email;

    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProviderType providerType;

    @Column
    private String resetToken;

    @Column
    private LocalDateTime resetTokenExpiry;

    @Column
    private LocalDateTime lastPasswordResetRequest;


    @Column
    private String pendingEmail;

    @Column
    private String emailVerificationToken;

    @Column
    private LocalDateTime emailVerificationExpiry;


    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<RoleType> roles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(roleType ->
                authorities.addAll(RolePermissionMapping.getAuthoritiesForRole(roleType))
        );
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }



}
