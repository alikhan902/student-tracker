package com.alok.studentTracker.Repository;

import com.alok.studentTracker.entity.TrainingSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingSubjectRepository extends JpaRepository<TrainingSubject, Long> {
    
    @Query("SELECT ts FROM TrainingSubject ts WHERE ts.group.id = :groupId")
    List<TrainingSubject> findByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT ts FROM TrainingSubject ts WHERE ts.group.id = :groupId ORDER BY ts.createdAt DESC")
    List<TrainingSubject> findByGroupIdOrderByCreatedAtDesc(@Param("groupId") Long groupId);

    @Query("SELECT ts FROM TrainingSubject ts WHERE ts.title ILIKE %:title%")
    List<TrainingSubject> findByTitleContainingIgnoreCase(@Param("title") String title);
}
