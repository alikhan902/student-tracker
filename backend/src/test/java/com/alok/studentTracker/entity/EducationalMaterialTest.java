package com.alok.studentTracker.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EducationalMaterialTest {

    private EducationalMaterial material;
    private TrainingSubject testSubject;

    @BeforeEach
    void setUp() {
        testSubject = TrainingSubject.builder()
                .id(1L)
                .title("Java Programming")
                .build();

        material = EducationalMaterial.builder()
                .id(1L)
                .title("Chapter 1: Introduction")
                .description("Introduction to Java programming")
                .trainingSubject(testSubject)
                .filePath("/uploads/chapter1.pdf")
                .originalFileName("chapter1.pdf")
                .contentType("application/pdf")
                .fileSize(1024000L)
                .uploadedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testEducationalMaterialCreation() {
        assertNotNull(material);
        assertEquals(1L, material.getId());
        assertEquals("Chapter 1: Introduction", material.getTitle());
    }

    @Test
    void testEducationalMaterialFields() {
        assertEquals("Introduction to Java programming", material.getDescription());
        assertEquals("/uploads/chapter1.pdf", material.getFilePath());
        assertEquals("chapter1.pdf", material.getOriginalFileName());
        assertEquals("application/pdf", material.getContentType());
        assertEquals(1024000L, material.getFileSize());
    }

    @Test
    void testEducationalMaterialTrainingSubject() {
        assertEquals(testSubject, material.getTrainingSubject());
        assertEquals(1L, material.getTrainingSubject().getId());
    }

    @Test
    void testEducationalMaterialBuilder() {
        EducationalMaterial built = EducationalMaterial.builder()
                .id(2L)
                .title("Chapter 2")
                .description("Advanced Topics")
                .filePath("/uploads/chapter2.pdf")
                .originalFileName("chapter2.pdf")
                .contentType("application/pdf")
                .fileSize(2048000L)
                .build();
        
        assertEquals(2L, built.getId());
        assertEquals("Chapter 2", built.getTitle());
        assertEquals(2048000L, built.getFileSize());
    }

    @Test
    void testEducationalMaterialSetters() {
        material.setTitle("Updated Title");
        material.setDescription("Updated Description");
        material.setContentType("application/vnd.ms-word");
        
        assertEquals("Updated Title", material.getTitle());
        assertEquals("Updated Description", material.getDescription());
        assertEquals("application/vnd.ms-word", material.getContentType());
    }

    @Test
    void testEducationalMaterialNoArgsConstructor() {
        EducationalMaterial newMaterial = new EducationalMaterial();
        assertNull(newMaterial.getId());
        assertNull(newMaterial.getTitle());
        assertNull(newMaterial.getFilePath());
    }

    @Test
    void testEducationalMaterialAllArgsConstructor() {
        LocalDateTime uploadTime = LocalDateTime.now();
        LocalDateTime updateTime = LocalDateTime.now();
        EducationalMaterial material = new EducationalMaterial(
                3L,
                "Test Material",
                "Test Description",
                testSubject,
                "/path/to/file",
                "filename.txt",
                "text/plain",
                512000L,
                uploadTime,
                updateTime
        );
        
        assertEquals(3L, material.getId());
        assertEquals("Test Material", material.getTitle());
        assertEquals(uploadTime, material.getUploadedAt());
        assertEquals(updateTime, material.getUpdatedAt());
    }

    @Test
    void testEducationalMaterialFilePath() {
        String newPath = "/uploads/newfile.docx";
        material.setFilePath(newPath);
        assertEquals(newPath, material.getFilePath());
    }

    @Test
    void testEducationalMaterialFileSize() {
        Long newSize = 5000000L;
        material.setFileSize(newSize);
        assertEquals(newSize, material.getFileSize());
    }

    @Test
    void testEducationalMaterialUploadedAt() {
        LocalDateTime newTime = LocalDateTime.now().plusDays(1);
        material.setUploadedAt(newTime);
        assertEquals(newTime, material.getUploadedAt());
    }
}
