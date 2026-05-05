package com.alok.studentTracker.dto.Group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupsAllDTO {
    private Long id;
    private String name;
    private List<GroupMemberDTO> members;
}