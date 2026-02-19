package com.java.ecoaula.repository;

//import java.util.List;
//import java.util.Locale.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.ecoaula.entity.Category;
//import com.java.ecoaula.dto.VolumeByCategoryDTO;
import com.java.ecoaula.entity.Waste;

@Repository
public interface WasteRepository extends JpaRepository<Waste,Integer> {
    /*@Query("""
        SELECT new com.java.ecoaula.dto.VolumeByCategoryDTO(
            w.category,
            SUM(w.heavy)
        )
        FROM Waste w
        GROUP BY w.category
    """)
    List<VolumeByCategoryDTO> getTotalVolumeByCategory();*/

    @Query("""
    SELECT SUM(w.heavy)
    FROM Waste w
    WHERE w.category = :category
    """)
    Double getTotalVolumeByCategory(@Param("category") Category category);


}
