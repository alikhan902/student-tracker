package com.alok.studentTracker.service.impl;

import com.alok.studentTracker.Repository.GroupRepository;
import com.alok.studentTracker.Repository.UserRepository;
import com.alok.studentTracker.dto.GroupDTO;
import com.alok.studentTracker.entity.Group;
import com.alok.studentTracker.entity.User;
import com.alok.studentTracker.service.GroupService;
import com.alok.studentTracker.entity.type.StudentType;
import com.alok.studentTracker.entity.UserPrinciple;
import com.alok.studentTracker.service.ValidationService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ValidationService validationService;

    @Override
    public Group createGroup(GroupDTO groupDTO) {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getStudentType() != StudentType.HEADMAN) {
            throw new RuntimeException("Only HEADMAN can create group");
        }

        if (currentUser.getGroup() != null) {
            throw new RuntimeException("HEADMAN already has a group");
        }

        Group group = Group.builder()
                .name(groupDTO.getName())
                .build();

        Group savedGroup = groupRepository.save(group);

        currentUser.setGroup(savedGroup);
        userRepository.save(currentUser);

        return savedGroup;
    }



    @Override
    public Optional<Group> getMyGroup() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        var principal = authentication.getPrincipal();
        if (principal instanceof UserPrinciple userPrinciple) {
            return Optional.ofNullable(userPrinciple.getUser().getGroup());
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Group updateGroup(GroupDTO groupDTO) {

        Group group = getMyGroup()
                .orElseThrow(() -> new RuntimeException("Group not found"));

        validationService.validateHeadmanAccess(group.getId());

        group.setName(groupDTO.getName());

        return groupRepository.save(group);
    }

    @Override
    @Transactional
    public void deleteGroup() {

        Group group = getMyGroup()
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User currentUser = validationService.validateHeadmanAccess(group.getId());

        List<User> users = userRepository.findAllByGroup(group);

        for (User user : users) {
            user.setGroup(null);
        }

        userRepository.saveAll(users);

        groupRepository.delete(group);
    }
}
