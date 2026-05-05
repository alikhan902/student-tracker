package com.alok.studentTracker.dto.Group;

import com.alok.studentTracker.entity.type.StudentType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupMemberDTO {
    private Long id;
    private String name;
    private String username;
    private StudentType studentType;
}