package com.alok.studentTracker.service;

import com.alok.studentTracker.Exception.ResourceNotFoundException;
import com.alok.studentTracker.Repository.EducationalMaterialRepository;
import com.alok.studentTracker.Repository.MaterialCompleteRepository;
import com.alok.studentTracker.Repository.UserRepository;
import com.alok.studentTracker.dto.MaterialCompleteDTO;
import com.alok.studentTracker.dto.MaterialCompleteUpdateDTO;
import com.alok.studentTracker.entity.*;
import com.alok.studentTracker.entity.type.CompletionStatus;
import com.alok.studentTracker.service.impl.MaterialCompleteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MaterialCompleteServiceTest {

    @Mock
    private MaterialCompleteRepository materialCompleteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EducationalMaterialRepository educationalMaterialRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private MaterialCompleteServiceImpl materialCompleteService;

    private User testUser;
    private EducationalMaterial testMaterial;
    private MaterialComplete materialComplete;
    private MaterialCompleteDTO materialCompleteDTO;
    private MaterialCompleteUpdateDTO updateDTO;

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

        testMaterial = EducationalMaterial.builder()
                .id(1L)
                .title("Test Material")
                .build();

        materialComplete = MaterialComplete.builder()
                .id(new MaterialCompleteId(1L, 1L))
                .user(testUser)
                .educationalMaterial(testMaterial)
                .status(CompletionStatus.COMPLETED)
                .build();

        materialCompleteDTO = MaterialCompleteDTO.builder()
                .userId(1L)
                .educationalMaterialId(1L)
                .status(CompletionStatus.COMPLETED)
                .build();

        updateDTO = new MaterialCompleteUpdateDTO(CompletionStatus.COMPLETED);
    }

    @Test
    void testCreateMaterialCompleteSuccess() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(educationalMaterialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(materialCompleteRepository.findByUserIdAndMaterialId(1L, 1L)).thenReturn(Optional.empty());
        when(materialCompleteRepository.save(any(MaterialComplete.class))).thenReturn(materialComplete);
        when(modelMapper.map(materialComplete, MaterialCompleteDTO.class)).thenReturn(materialCompleteDTO);

        MaterialCompleteDTO result = materialCompleteService.updateOrCreateMaterialComplete(1L, 1L, updateDTO);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getEducationalMaterialId());
        assertEquals(CompletionStatus.COMPLETED, result.getStatus());

        verify(materialCompleteRepository, times(1)).save(any(MaterialComplete.class));
    }

    @Test
    void testUpdateMaterialCompleteSuccess() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(educationalMaterialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(materialCompleteRepository.findByUserIdAndMaterialId(1L, 1L)).thenReturn(Optional.of(materialComplete));
        when(materialCompleteRepository.save(any(MaterialComplete.class))).thenReturn(materialComplete);
        when(modelMapper.map(materialComplete, MaterialCompleteDTO.class)).thenReturn(materialCompleteDTO);

        MaterialCompleteDTO result = materialCompleteService.updateOrCreateMaterialComplete(1L, 1L, updateDTO);

        assertNotNull(result);
        verify(materialCompleteRepository, times(1)).save(any(MaterialComplete.class));
    }

    @Test
    void testUpdateMaterialCompleteUnauthorizedUser() {
        User differentUser = User.builder()
                .id(2L)
                .username("differentuser")
                .build();

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(differentUser));

        assertThrows(ResourceNotFoundException.class, 
            () -> materialCompleteService.updateOrCreateMaterialComplete(1L, 1L, updateDTO));
    }

    @Test
    void testGetMaterialCompleteStatusSuccess() {
        when(materialCompleteRepository.findByUserIdAndMaterialId(1L, 1L)).thenReturn(Optional.of(materialComplete));
        when(modelMapper.map(materialComplete, MaterialCompleteDTO.class)).thenReturn(materialCompleteDTO);

        Optional<MaterialCompleteDTO> result = materialCompleteService.getMaterialCompleteStatus(1L, 1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getUserId());
        assertEquals(1L, result.get().getEducationalMaterialId());
    }

    @Test
    void testGetMaterialCompleteStatusNotFound() {
        when(materialCompleteRepository.findByUserIdAndMaterialId(1L, 1L)).thenReturn(Optional.empty());

        Optional<MaterialCompleteDTO> result = materialCompleteService.getMaterialCompleteStatus(1L, 1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testMaterialNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(educationalMaterialRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> materialCompleteService.updateOrCreateMaterialComplete(1L, 1L, updateDTO));
    }

    @Test
    void testUserNotFoundInDatabase() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(educationalMaterialRepository.findById(1L)).thenReturn(Optional.of(testMaterial));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> materialCompleteService.updateOrCreateMaterialComplete(1L, 1L, updateDTO));
    }
}
