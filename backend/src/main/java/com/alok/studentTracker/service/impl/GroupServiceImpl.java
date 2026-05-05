package com.alok.studentTracker.service.impl;

import com.alok.studentTracker.Repository.GroupRepository;
import com.alok.studentTracker.dto.GroupDTO;
import com.alok.studentTracker.entity.Group;
import com.alok.studentTracker.entity.UserPrinciple;
import com.alok.studentTracker.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    @Override
    public Group createGroup(GroupDTO groupDTO) {
        Group group = Group.builder()
                .name(groupDTO.getName())
                .code(groupDTO.getCode())
                .description(groupDTO.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return groupRepository.save(group);
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
    public Group updateGroup(Long id, GroupDTO groupDTO) {
        return groupRepository.findById(id).map(group -> {
            group.setName(groupDTO.getName());
            group.setCode(groupDTO.getCode());
            group.setDescription(groupDTO.getDescription());
            group.setUpdatedAt(LocalDateTime.now());
            return groupRepository.save(group);
        }).orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + id));
    }

    @Override
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}
