package com.alok.studentTracker.dto;

import com.alok.studentTracker.entity.type.CompletionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialCompleteUpdateDTO {
    
    private CompletionStatus status;
}
