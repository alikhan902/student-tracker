package com.alok.studentTracker.controller;

import com.alok.studentTracker.dto.MaterialCompleteDTO;
import com.alok.studentTracker.dto.MaterialCompleteUpdateDTO;
import com.alok.studentTracker.Repository.UserRepository;
import com.alok.studentTracker.entity.User;
import com.alok.studentTracker.service.MaterialCompleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material-complete")
@RequiredArgsConstructor
public class MaterialCompleteController {

    private final MaterialCompleteService materialCompleteService;
    private final UserRepository userRepository;

    /**
     * Update or create material completion status for current user
     * PUT /api/material-complete/{materialId}
     */
    @PutMapping("/{materialId}")
    public ResponseEntity<?> updateOrCreateMaterialComplete(
            @PathVariable Long materialId,
            @RequestBody MaterialCompleteUpdateDTO dto
    ) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            MaterialCompleteDTO result = materialCompleteService.updateOrCreateMaterialComplete(
                    currentUser.getId(), materialId, dto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Get material completion status for current user
     * GET /api/material-complete/{materialId}
     * Returns 404 if not found
     */
    @GetMapping("/{materialId}")
    public ResponseEntity<?> getMaterialCompleteStatus(
            @PathVariable Long materialId
    ) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return materialCompleteService.getMaterialCompleteStatus(currentUser.getId(), materialId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
