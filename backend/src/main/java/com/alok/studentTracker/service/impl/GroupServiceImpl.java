package com.alok.studentTracker.service.impl;

import com.alok.studentTracker.Repository.GroupRepository;
import com.alok.studentTracker.Repository.UserRepository;
import com.alok.studentTracker.dto.GroupDTO;
import com.alok.studentTracker.dto.Group.GroupMemberDTO;
import com.alok.studentTracker.dto.Group.GroupsAllDTO;
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

    private Optional<Group> getCurrentUserGroup() {
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
    @Transactional(readOnly = true)
    public Optional<GroupsAllDTO> getMyGroup() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = user.getGroup();

        if (group == null) {
            return Optional.empty();
        }

        // ВАЖНО: гарантируем загрузку users через repo (не lazy)
        Group fullGroup = groupRepository.findByIdWithUsers(group.getId())
                .orElse(group);

        List<GroupMemberDTO> members = fullGroup.getUsers().stream()
                .map(u -> new GroupMemberDTO(
                        u.getId(),
                        u.getName(),
                        u.getUsername(),
                        u.getStudentType()
                ))
                .toList();

        return Optional.of(new GroupsAllDTO(
                fullGroup.getId(),
                fullGroup.getName(),
                members
        ));
    }

    @Override
    @Transactional
    public Group updateGroup(GroupDTO groupDTO) {

        Group group = getCurrentUserGroup()
                .orElseThrow(() -> new RuntimeException("Group not found"));

        validationService.validateHeadmanAccess(group.getId());

        group.setName(groupDTO.getName());

        return groupRepository.save(group);
    }

    @Override
    @Transactional
    public void deleteGroup() {

        Group group = getCurrentUserGroup()
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User currentUser = validationService.validateHeadmanAccess(group.getId());

        List<User> users = userRepository.findAllByGroup(group);

        for (User user : users) {
            user.setGroup(null);
        }

        userRepository.saveAll(users);

        groupRepository.delete(group);
    }

    @Override
    @Transactional
    public void addStudentToGroup(String username) {
        Group group = getCurrentUserGroup()
                .orElseThrow(() -> new RuntimeException("Group not found"));

        validationService.validateHeadmanAccess(group.getId());

        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (student.getStudentType() != StudentType.STANDARD) {
            throw new RuntimeException("Only students can be added to group");
        }

        if (student.getGroup() != null) {
            throw new RuntimeException("Student already has a group");
        }

        student.setGroup(group);
        userRepository.save(student);
    }

    @Override
    @Transactional
    public void deleteStudentFromGroup(Long studentId) {
        Group group = getCurrentUserGroup()
                .orElseThrow(() -> new RuntimeException("Group not found"));
        validationService.validateHeadmanAccess(group.getId());
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        if (student.getGroup() == null || !student.getGroup().getId().equals(group.getId())) {
            throw new RuntimeException("Student is not in your group");
        }
        if (student.getStudentType() != StudentType.STANDARD) {
            throw new RuntimeException("Only students can be removed from group");
        }
        student.setGroup(null);
        userRepository.save(student);
    }
}

