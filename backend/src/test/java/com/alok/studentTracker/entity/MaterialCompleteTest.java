package com.alok.studentTracker.entity;

import com.alok.studentTracker.entity.type.CompletionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MaterialCompleteTest {

    private MaterialComplete materialComplete;
    private MaterialCompleteId compositeId;
    private User testUser;
    private EducationalMaterial testMaterial;
    private TrainingSubject testSubject;

    @BeforeEach
    void setUp() {
        testSubject = TrainingSubject.builder()
                .id(1L)
                .title("Java Programming")
                .build();

        testMaterial = EducationalMaterial.builder()
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

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        compositeId = new MaterialCompleteId(1L, 1L);

        materialComplete = MaterialComplete.builder()
                .id(compositeId)
                .user(testUser)
                .educationalMaterial(testMaterial)
                .status(CompletionStatus.COMPLETED)
                .build();
    }

    @Test
    void testMaterialCompleteCreation() {
        assertNotNull(materialComplete);
        assertNotNull(materialComplete.getId());
        assertEquals(1L, materialComplete.getId().getUserId());
        assertEquals(1L, materialComplete.getId().getEducationalMaterialId());
        assertEquals(CompletionStatus.COMPLETED, materialComplete.getStatus());
    }

    @Test
    void testMaterialCompleteWithNotCompletedStatus() {
        materialComplete.setStatus(CompletionStatus.NOT_COMPLETED);
        assertEquals(CompletionStatus.NOT_COMPLETED, materialComplete.getStatus());
    }

    @Test
    void testMaterialCompleteIdEquality() {
        MaterialCompleteId id1 = new MaterialCompleteId(1L, 1L);
        MaterialCompleteId id2 = new MaterialCompleteId(1L, 1L);
        assertEquals(id1, id2);
    }

    @Test
    void testMaterialCompleteIdInequality() {
        MaterialCompleteId id1 = new MaterialCompleteId(1L, 1L);
        MaterialCompleteId id2 = new MaterialCompleteId(1L, 2L);
        assertNotEquals(id1, id2);
    }

    @Test
    void testMaterialCompleteUserRelationship() {
        assertNotNull(materialComplete.getUser());
        assertEquals("testuser", materialComplete.getUser().getUsername());
    }

    @Test
    void testMaterialCompleteMaterialRelationship() {
        assertNotNull(materialComplete.getEducationalMaterial());
        assertEquals("Chapter 1: Introduction", materialComplete.getEducationalMaterial().getTitle());
    }

    @Test
    void testMaterialCompleteTimestamps() {
        // Manually trigger onCreate() since it's called by JPA before persisting
        materialComplete.onCreate();
        assertNotNull(materialComplete.getCreatedAt());
        assertNotNull(materialComplete.getUpdatedAt());
    }

    @Test
    void testMaterialCompleteStatusEnum() {
        assertTrue(CompletionStatus.COMPLETED == CompletionStatus.COMPLETED);
        assertTrue(CompletionStatus.NOT_COMPLETED == CompletionStatus.NOT_COMPLETED);
    }
}
