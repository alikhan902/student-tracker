package com.alok.studentTracker.service;

import com.alok.studentTracker.dto.MaterialCompleteDTO;
import com.alok.studentTracker.dto.MaterialCompleteUpdateDTO;

import java.util.Optional;

public interface MaterialCompleteService {

    MaterialCompleteDTO updateOrCreateMaterialComplete(Long userId, Long materialId, MaterialCompleteUpdateDTO dto);

    Optional<MaterialCompleteDTO> getMaterialCompleteStatus(Long userId, Long materialId);
}
