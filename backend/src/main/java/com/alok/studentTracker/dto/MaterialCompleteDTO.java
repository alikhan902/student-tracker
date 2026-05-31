package com.alok.studentTracker.dto;

import com.alok.studentTracker.entity.type.CompletionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialCompleteDTO {
    
    private Long userId;
    private Long educationalMaterialId;
    private CompletionStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
