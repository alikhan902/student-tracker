package com.alok.studentTracker.service.impl;

import com.alok.studentTracker.Exception.ResourceNotFoundException;
import com.alok.studentTracker.Repository.EducationalMaterialRepository;
import com.alok.studentTracker.Repository.MaterialCompleteRepository;
import com.alok.studentTracker.Repository.UserRepository;
import com.alok.studentTracker.dto.MaterialCompleteDTO;
import com.alok.studentTracker.dto.MaterialCompleteUpdateDTO;
import com.alok.studentTracker.entity.EducationalMaterial;
import com.alok.studentTracker.entity.MaterialComplete;
import com.alok.studentTracker.entity.MaterialCompleteId;
import com.alok.studentTracker.entity.User;
import com.alok.studentTracker.service.MaterialCompleteService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaterialCompleteServiceImpl implements MaterialCompleteService {

    private final MaterialCompleteRepository materialCompleteRepository;
    private final UserRepository userRepository;
    private final EducationalMaterialRepository educationalMaterialRepository;
    private final ModelMapper modelMapper;

    @Override
    public MaterialCompleteDTO updateOrCreateMaterialComplete(Long userId, Long materialId, MaterialCompleteUpdateDTO dto) {
        // Get current user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check if user has permission to update (only own records or has special permission)
        if (!currentUser.getId().equals(userId)) {
            throw new ResourceNotFoundException("You can only update your own completion status");
        }

        // Verify material exists
        EducationalMaterial material = educationalMaterialRepository.findById(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Educational material not found"));

        // Verify user exists
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Find existing or create new
        MaterialComplete materialComplete = materialCompleteRepository
                .findByUserIdAndMaterialId(userId, materialId)
                .orElse(null);

        if (materialComplete == null) {
            materialComplete = MaterialComplete.builder()
                    .id(new MaterialCompleteId(userId, materialId))
                    .user(targetUser)
                    .educationalMaterial(material)
                    .status(dto.getStatus())
                    .build();
        } else {
            materialComplete.setStatus(dto.getStatus());
        }

        MaterialComplete saved = materialCompleteRepository.save(materialComplete);
        return modelMapper.map(saved, MaterialCompleteDTO.class);
    }

    @Override
    public Optional<MaterialCompleteDTO> getMaterialCompleteStatus(Long userId, Long materialId) {
        return materialCompleteRepository
                .findByUserIdAndMaterialId(userId, materialId)
                .map(mc -> modelMapper.map(mc, MaterialCompleteDTO.class));
    }
}
