package com.alok.studentTracker.service.impl;

import com.alok.studentTracker.entity.User;
import com.alok.studentTracker.service.ValidationService;
import com.alok.studentTracker.entity.type.StudentType;
import com.alok.studentTracker.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    private final UserRepository userRepository;

    public User validateHeadmanAccess(Long groupId) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getStudentType() != StudentType.HEADMAN) {
            throw new RuntimeException("Only HEADMAN can manage group");
        }

        if (currentUser.getGroup() == null ||
            !currentUser.getGroup().getId().equals(groupId)) {
            throw new RuntimeException("You are not member of this group");
        }

        return currentUser;
    }
}