package com.java.ecoaula.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.java.ecoaula.dto.CategoryVolumeDTO;
import com.java.ecoaula.entity.Category;
import com.java.ecoaula.entity.Container;

@Repository
public interface ContainerRepository extends JpaRepository<Container,Integer> {
   @Query("""
    SELECT new com.java.ecoaula.dto.CategoryVolumeDTO(
        w.category, SUM(w.heavy)
    )
    FROM Waste w
    GROUP BY w.category
""")
List<CategoryVolumeDTO> getVolumeByCategory();

 Optional<Container> findByAllowedCategory(Category category);
}
