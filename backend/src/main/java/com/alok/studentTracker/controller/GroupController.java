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
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }



    @GetMapping("/my-group")
    public ResponseEntity<GroupsAllDTO> getMyGroup() {
        return groupService.getMyGroup()
                .map(g -> new GroupsAllDTO(
                        g.getId(),
                        g.getName(),
                        g.getCode(),
                        g.getDescription()
                ))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @Valid @RequestBody GroupDTO groupDTO) {
        try {
            Group group = groupService.updateGroup(id, groupDTO);
            return ResponseEntity.ok(group);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}
