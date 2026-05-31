package com.alok.studentTracker.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class TrainingSubjectTest {

    private TrainingSubject trainingSubject;
    private Group testGroup;

    @BeforeEach
    void setUp() {
        testGroup = Group.builder()
                .id(1L)
                .name("Test Group")
                .build();

        trainingSubject = TrainingSubject.builder()
                .id(1L)
                .title("Java Programming")
                .description("Learn Java basics and advanced concepts")
                .group(testGroup)
                .photoUrl("https://example.com/photo.jpg")
                .educationalMaterials(new HashSet<>())
                .build();
    }

    @Test
    void testTrainingSubjectCreation() {
        assertNotNull(trainingSubject);
        assertEquals(1L, trainingSubject.getId());
        assertEquals("Java Programming", trainingSubject.getTitle());
    }

    @Test
    void testTrainingSubjectFields() {
        assertEquals("Learn Java basics and advanced concepts", trainingSubject.getDescription());
        assertEquals("https://example.com/photo.jpg", trainingSubject.getPhotoUrl());
        assertEquals(testGroup, trainingSubject.getGroup());
    }

    @Test
    void testTrainingSubjectEducationalMaterials() {
        assertTrue(trainingSubject.getEducationalMaterials().isEmpty());
        
        EducationalMaterial material = EducationalMaterial.builder()
                .id(1L)
                .title("Chapter 1")
                .trainingSubject(trainingSubject)
                .filePath("/files/chapter1.pdf")
                .originalFileName("chapter1.pdf")
                .build();
        
        trainingSubject.getEducationalMaterials().add(material);
        assertEquals(1, trainingSubject.getEducationalMaterials().size());
    }

    @Test
    void testTrainingSubjectBuilder() {
        TrainingSubject built = TrainingSubject.builder()
                .id(2L)
                .title("Python Basics")
                .description("Introduction to Python")
                .group(testGroup)
                .build();
        
        assertEquals(2L, built.getId());
        assertEquals("Python Basics", built.getTitle());
    }

    @Test
    void testTrainingSubjectSetters() {
        trainingSubject.setTitle("Updated Title");
        trainingSubject.setDescription("Updated Description");
        
        assertEquals("Updated Title", trainingSubject.getTitle());
        assertEquals("Updated Description", trainingSubject.getDescription());
    }

    @Test
    void testTrainingSubjectNoArgsConstructor() {
        TrainingSubject newSubject = new TrainingSubject();
        assertNull(newSubject.getId());
        assertNull(newSubject.getTitle());
        assertNotNull(newSubject.getEducationalMaterials());
        assertTrue(newSubject.getEducationalMaterials().isEmpty());
    }

    @Test
    void testTrainingSubjectAllArgsConstructor() {
        Group group = new Group();
        TrainingSubject subject = new TrainingSubject(
                3L,
                "Test Subject",
                group,
                "Description",
                "photo.jpg",
                null,
                null,
                new HashSet<>()
        );
        
        assertEquals(3L, subject.getId());
        assertEquals("Test Subject", subject.getTitle());
    }

    @Test
    void testTrainingSubjectTimestamps() {
        LocalDateTime now = LocalDateTime.now();
        trainingSubject.setCreatedAt(now);
        trainingSubject.setUpdatedAt(now);
        
        assertEquals(now, trainingSubject.getCreatedAt());
        assertEquals(now, trainingSubject.getUpdatedAt());
    }

    @Test
    void testTrainingSubjectMultipleMaterials() {
        EducationalMaterial material1 = EducationalMaterial.builder()
                .id(1L)
                .title("Material 1")
                .trainingSubject(trainingSubject)
                .filePath("/files/mat1.pdf")
                .originalFileName("mat1.pdf")
                .build();
        
        EducationalMaterial material2 = EducationalMaterial.builder()
                .id(2L)
                .title("Material 2")
                .trainingSubject(trainingSubject)
                .filePath("/files/mat2.pdf")
                .originalFileName("mat2.pdf")
                .build();
        
        trainingSubject.getEducationalMaterials().add(material1);
        trainingSubject.getEducationalMaterials().add(material2);
        
        assertEquals(2, trainingSubject.getEducationalMaterials().size());
    }
}
