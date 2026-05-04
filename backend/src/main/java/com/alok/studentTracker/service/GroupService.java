package com.alok.studentTracker.service;

import com.alok.studentTracker.dto.GroupDTO;
import com.alok.studentTracker.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    Group createGroup(GroupDTO groupDTO);
    Optional<Group> getGroupById(Long id);
    Optional<Group> getGroupByCode(String code);
    List<Group> getAllGroups();
    Group updateGroup(Long id, GroupDTO groupDTO);
    void deleteGroup(Long id);
}
