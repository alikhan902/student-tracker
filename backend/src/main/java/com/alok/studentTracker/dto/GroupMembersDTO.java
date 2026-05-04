package com.alok.studentTracker.dto;

import com.alok.studentTracker.entity.type.GroupMemberRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMembersDTO {
    
    @NotNull(message = "Group ID is required")
    private Long groupId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private String username;

    private String userEmail;

    @NotNull(message = "Role is required")
    private GroupMemberRole role;

    private LocalDateTime joinedAt;
}
