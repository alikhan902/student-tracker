package com.alok.studentTracker.controller;

import com.alok.studentTracker.dto.GroupDTO;
import com.alok.studentTracker.dto.Group.GroupsAllDTO;
import com.alok.studentTracker.entity.Group;
import com.alok.studentTracker.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<Group> createGroup(@Valid @RequestBody GroupDTO groupDTO) {
        Group group = groupService.createGroup(groupDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/my-group")
    public ResponseEntity<GroupsAllDTO> getMyGroup() {
        return groupService.getMyGroup()
                .map(g -> new GroupsAllDTO(
                        g.getId(),
                        g.getName()
                ))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping()
    public ResponseEntity<String> updateGroup(@Valid @RequestBody GroupDTO groupDTO) {
        try {
            Group group = groupService.updateGroup(groupDTO);
            return ResponseEntity.ok("Group updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteGroup() {
        groupService.deleteGroup();
        return ResponseEntity.ok("Group deleted successfully");
    }
}
