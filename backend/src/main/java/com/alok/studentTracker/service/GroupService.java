package com.alok.studentTracker.service;

import com.alok.studentTracker.dto.GroupDTO;
import com.alok.studentTracker.entity.Group;

import java.util.Optional;

public interface GroupService {
    Group createGroup(GroupDTO groupDTO);

    Optional<Group> getMyGroup();
    Group updateGroup(GroupDTO groupDTO);
    void deleteGroup();
}
