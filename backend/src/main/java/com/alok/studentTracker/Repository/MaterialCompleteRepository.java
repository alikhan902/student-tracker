package com.alok.studentTracker.Repository;

import com.alok.studentTracker.entity.MaterialComplete;
import com.alok.studentTracker.entity.MaterialCompleteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaterialCompleteRepository extends JpaRepository<MaterialComplete, MaterialCompleteId> {

    @Query("SELECT mc FROM MaterialComplete mc WHERE mc.user.id = :userId AND mc.educationalMaterial.id = :materialId")
    Optional<MaterialComplete> findByUserIdAndMaterialId(@Param("userId") Long userId, @Param("materialId") Long materialId);

    @Query("SELECT COUNT(mc) FROM MaterialComplete mc WHERE mc.user.id = :userId AND mc.educationalMaterial.id = :materialId")
    boolean existsByUserIdAndMaterialId(@Param("userId") Long userId, @Param("materialId") Long materialId);
}
