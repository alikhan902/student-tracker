package com.alok.studentTracker.service;

import com.alok.studentTracker.dto.GroupMembersDTO;
import com.alok.studentTracker.entity.GroupMembers;
import com.alok.studentTracker.entity.type.GroupMemberRole;

import java.util.List;
import java.util.Optional;

public interface GroupMembersService {
    GroupMembers addUserToGroup(Long groupId, Long userId, GroupMemberRole role);
    void removeUserFromGroup(Long groupId, Long userId);
    Optional<GroupMembers> getGroupMember(Long groupId, Long userId);
    List<GroupMembers> getGroupMembers(Long groupId);
    List<GroupMembers> getUserGroups(Long userId);
    List<GroupMembers> getGroupHeadmen(Long groupId);
    void changeUserRole(Long groupId, Long userId, GroupMemberRole newRole);
}
