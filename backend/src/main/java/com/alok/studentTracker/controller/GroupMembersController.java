package com.alok.studentTracker.controller;

import com.alok.studentTracker.dto.GroupMembersDTO;
import com.alok.studentTracker.entity.GroupMembers;
import com.alok.studentTracker.entity.type.GroupMemberRole;
import com.alok.studentTracker.service.GroupMembersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/group-members")
@RequiredArgsConstructor
public class GroupMembersController {

    private final GroupMembersService groupMembersService;

    @PostMapping
    public ResponseEntity<GroupMembers> addUserToGroup(@Valid @RequestBody GroupMembersDTO groupMembersDTO) {
        GroupMembers groupMembers = groupMembersService.addUserToGroup(
                groupMembersDTO.getGroupId(),
                groupMembersDTO.getUserId(),
                groupMembersDTO.getRole()
        );
        return new ResponseEntity<>(groupMembers, HttpStatus.CREATED);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<GroupMembers>> getGroupMembers(@PathVariable Long groupId) {
        List<GroupMembers> members = groupMembersService.getGroupMembers(groupId);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GroupMembers>> getUserGroups(@PathVariable Long userId) {
        List<GroupMembers> groups = groupMembersService.getUserGroups(userId);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/group/{groupId}/headmen")
    public ResponseEntity<List<GroupMembers>> getGroupHeadmen(@PathVariable Long groupId) {
        List<GroupMembers> headmen = groupMembersService.getGroupHeadmen(groupId);
        return ResponseEntity.ok(headmen);
    }

    @GetMapping("/{groupId}/{userId}")
    public ResponseEntity<GroupMembers> getGroupMember(@PathVariable Long groupId, @PathVariable Long userId) {
        return groupMembersService.getGroupMember(groupId, userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{groupId}/{userId}/role")
    public ResponseEntity<Void> changeUserRole(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @RequestParam GroupMemberRole newRole) {
        try {
            groupMembersService.changeUserRole(groupId, userId, newRole);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{groupId}/{userId}")
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        groupMembersService.removeUserFromGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }
}
