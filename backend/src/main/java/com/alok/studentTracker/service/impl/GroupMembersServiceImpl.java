package com.alok.studentTracker.service.impl;

import com.alok.studentTracker.Repository.GroupMembersRepository;
import com.alok.studentTracker.Repository.GroupRepository;
import com.alok.studentTracker.Repository.UserRepository;
import com.alok.studentTracker.entity.Group;
import com.alok.studentTracker.entity.GroupMembers;
import com.alok.studentTracker.entity.GroupMembersId;
import com.alok.studentTracker.entity.User;
import com.alok.studentTracker.entity.type.GroupMemberRole;
import com.alok.studentTracker.service.GroupMembersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupMembersServiceImpl implements GroupMembersService {

    private final GroupMembersRepository groupMembersRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Override
    public GroupMembers addUserToGroup(Long groupId, Long userId, GroupMemberRole role) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        GroupMembersId id = new GroupMembersId(groupId, userId);
        
        GroupMembers groupMembers = GroupMembers.builder()
                .id(id)
                .group(group)
                .user(user)
                .role(role)
                .joinedAt(LocalDateTime.now())
                .build();

        return groupMembersRepository.save(groupMembers);
    }

    @Override
    public void removeUserFromGroup(Long groupId, Long userId) {
        GroupMembersId id = new GroupMembersId(groupId, userId);
        groupMembersRepository.deleteById(id);
    }

    @Override
    public Optional<GroupMembers> getGroupMember(Long groupId, Long userId) {
        return groupMembersRepository.findByGroupIdAndUserId(groupId, userId);
    }

    @Override
    public List<GroupMembers> getGroupMembers(Long groupId) {
        return groupMembersRepository.findByGroupId(groupId);
    }

    @Override
    public List<GroupMembers> getUserGroups(Long userId) {
        return groupMembersRepository.findByUserId(userId);
    }

    @Override
    public List<GroupMembers> getGroupHeadmen(Long groupId) {
        return groupMembersRepository.findByGroupIdAndRole(groupId, GroupMemberRole.HEADMAN);
    }

    @Override
    public void changeUserRole(Long groupId, Long userId, GroupMemberRole newRole) {
        GroupMembers groupMembers = groupMembersRepository.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found in group"));
        
        groupMembers.setRole(newRole);
        groupMembersRepository.save(groupMembers);
    }
}
