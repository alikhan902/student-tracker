package com.alok.studentTracker.Repository;

import com.alok.studentTracker.entity.type.AuthProviderType;
import com.alok.studentTracker.entity.User;
import com.alok.studentTracker.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String resetToken);
    Optional<User> findByEmailVerificationToken(String token);
    Optional<User> findByProviderTypeAndProviderId(AuthProviderType providerType, String providerId);

    List<User> findAllByGroup(Group group);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

}
