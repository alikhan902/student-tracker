package com.alok.studentTracker.controller;

import com.alok.studentTracker.dto.TrainingItemDTO;
import com.alok.studentTracker.entity.TrainingItem;
import com.alok.studentTracker.service.TrainingItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/training-items")
@RequiredArgsConstructor
public class TrainingItemController {

    private final TrainingItemService trainingItemService;

    @PostMapping
    public ResponseEntity<TrainingItem> createTrainingItem(@Valid @RequestBody TrainingItemDTO trainingItemDTO) {
        TrainingItem trainingItem = trainingItemService.createTrainingItem(trainingItemDTO);
        return new ResponseEntity<>(trainingItem, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingItem> getTrainingItemById(@PathVariable Long id) {
        return trainingItemService.getTrainingItemById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<TrainingItem>> getTrainingItemsByGroupId(@PathVariable Long groupId) {
        List<TrainingItem> trainingItems = trainingItemService.getTrainingItemsByGroupId(groupId);
        return ResponseEntity.ok(trainingItems);
    }

    @GetMapping("/group/{groupId}/latest")
    public ResponseEntity<List<TrainingItem>> getTrainingItemsByGroupIdLatest(@PathVariable Long groupId) {
        List<TrainingItem> trainingItems = trainingItemService.getTrainingItemsByGroupIdOrderByCreatedAtDesc(groupId);
        return ResponseEntity.ok(trainingItems);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrainingItem>> searchTrainingItems(@RequestParam String title) {
        List<TrainingItem> trainingItems = trainingItemService.searchTrainingItemsByTitle(title);
        return ResponseEntity.ok(trainingItems);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingItem> updateTrainingItem(@PathVariable Long id, @Valid @RequestBody TrainingItemDTO trainingItemDTO) {
        try {
            TrainingItem trainingItem = trainingItemService.updateTrainingItem(id, trainingItemDTO);
            return ResponseEntity.ok(trainingItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingItem(@PathVariable Long id) {
        trainingItemService.deleteTrainingItem(id);
        return ResponseEntity.noContent().build();
    }
}
