package com.alok.studentTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {
    private Long id;

    @NotBlank(message = "Group name is required")
    private String name;

    @NotBlank(message = "Group code is required")
    private String code;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
