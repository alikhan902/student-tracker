package com.alok.studentTracker.dto;

import com.alok.studentTracker.entity.type.StudentType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {
    private String username;
    private String password;
    private String name;
    private StudentType studentType;
}
