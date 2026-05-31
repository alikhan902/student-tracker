package com.alok.studentTracker.controller;

import com.alok.studentTracker.Repository.UserRepository;
import com.alok.studentTracker.dto.MaterialCompleteDTO;
import com.alok.studentTracker.dto.MaterialCompleteUpdateDTO;
import com.alok.studentTracker.entity.User;
import com.alok.studentTracker.entity.type.CompletionStatus;
import com.alok.studentTracker.service.MaterialCompleteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MaterialCompleteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MaterialCompleteService materialCompleteService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private ObjectMapper objectMapper;
    private User testUser;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        mockMvc = MockMvcBuilders.standaloneSetup(
            new MaterialCompleteController(materialCompleteService, userRepository)
        ).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testUpdateOrCreateMaterialCompleteSuccess() throws Exception {
        Long materialId = 1L;
        MaterialCompleteUpdateDTO updateDTO = new MaterialCompleteUpdateDTO(CompletionStatus.COMPLETED);
        MaterialCompleteDTO resultDTO = MaterialCompleteDTO.builder()
                .userId(1L)
                .educationalMaterialId(materialId)
                .status(CompletionStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(materialCompleteService.updateOrCreateMaterialComplete(eq(1L), eq(materialId), any(MaterialCompleteUpdateDTO.class)))
                .thenReturn(resultDTO);

        mockMvc.perform(put("/api/material-complete/{materialId}", materialId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.educationalMaterialId").value(materialId))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testUpdateOrCreateMaterialCompleteNotCompleted() throws Exception {
        Long materialId = 1L;
        MaterialCompleteUpdateDTO updateDTO = new MaterialCompleteUpdateDTO(CompletionStatus.NOT_COMPLETED);
        MaterialCompleteDTO resultDTO = MaterialCompleteDTO.builder()
                .userId(1L)
                .educationalMaterialId(materialId)
                .status(CompletionStatus.NOT_COMPLETED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(materialCompleteService.updateOrCreateMaterialComplete(eq(1L), eq(materialId), any(MaterialCompleteUpdateDTO.class)))
                .thenReturn(resultDTO);

        mockMvc.perform(put("/api/material-complete/{materialId}", materialId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("NOT_COMPLETED"));
    }

    @Test
    void testGetMaterialCompleteStatusSuccess() throws Exception {
        Long materialId = 1L;
        MaterialCompleteDTO resultDTO = MaterialCompleteDTO.builder()
                .userId(1L)
                .educationalMaterialId(materialId)
                .status(CompletionStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(materialCompleteService.getMaterialCompleteStatus(1L, materialId))
                .thenReturn(Optional.of(resultDTO));

        mockMvc.perform(get("/api/material-complete/{materialId}", materialId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.educationalMaterialId").value(materialId))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testGetMaterialCompleteStatusNotFound() throws Exception {
        Long materialId = 1L;

        when(materialCompleteService.getMaterialCompleteStatus(1L, materialId))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/material-complete/{materialId}", materialId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateOrCreateMaterialCompleteError() throws Exception {
        Long materialId = 1L;
        MaterialCompleteUpdateDTO updateDTO = new MaterialCompleteUpdateDTO(CompletionStatus.COMPLETED);

        when(materialCompleteService.updateOrCreateMaterialComplete(eq(1L), eq(materialId), any(MaterialCompleteUpdateDTO.class)))
                .thenThrow(new RuntimeException("Material not found"));

        mockMvc.perform(put("/api/material-complete/{materialId}", materialId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isString());
    }
}
