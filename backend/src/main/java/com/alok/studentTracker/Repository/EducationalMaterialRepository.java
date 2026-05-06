package com.alok.studentTracker.Repository;

import com.alok.studentTracker.entity.EducationalMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationalMaterialRepository extends JpaRepository<EducationalMaterial, Long> {

    @Query("SELECT em FROM EducationalMaterial em WHERE em.trainingSubject.id = :trainingSubjectId")
    List<EducationalMaterial> findByTrainingSubjectId(@Param("trainingSubjectId") Long trainingSubjectId);

    @Query("SELECT em FROM EducationalMaterial em WHERE em.trainingSubject.group.id = :groupId")
    List<EducationalMaterial> findByGroupId(@Param("groupId") Long groupId);
}

