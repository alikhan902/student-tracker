package com.alok.studentTracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "educational_materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationalMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Название материала
    @Column(nullable = false)
    private String title;

    // Описание
    @Column(columnDefinition = "TEXT")
    private String description;

    // Предмет (математика, физика...)
    @Column(nullable = false)
    private String subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_item_id", nullable = false)
    private TrainingItem trainingItem;

    // Путь к файлу на диске
    @Column(nullable = false)
    private String filePath;

    // Оригинальное имя файла
    @Column(nullable = false)
    private String originalFileName;

    // MIME type
    private String contentType;

    // Размер файла
    private Long fileSize;

    // дата загрузки
    private LocalDateTime uploadedAt;

    // дата обновления
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}