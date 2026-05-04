package com.alok.studentTracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDto {

    @NotBlank(message = "Name is required")
    private String name;

//    @Email(message = "Invalid email")
//    @NotBlank(message = "Email is required")
//    private String email;
}
