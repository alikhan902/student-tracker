package com.alok.studentTracker.Repository;

import com.alok.studentTracker.entity.TrainingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingItemRepository extends JpaRepository<TrainingItem, Long> {
    
    @Query("SELECT ti FROM TrainingItem ti WHERE ti.group.id = :groupId")
    List<TrainingItem> findByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT ti FROM TrainingItem ti WHERE ti.group.id = :groupId ORDER BY ti.createdAt DESC")
    List<TrainingItem> findByGroupIdOrderByCreatedAtDesc(@Param("groupId") Long groupId);

    @Query("SELECT ti FROM TrainingItem ti WHERE ti.title ILIKE %:title%")
    List<TrainingItem> findByTitleContainingIgnoreCase(@Param("title") String title);
}
