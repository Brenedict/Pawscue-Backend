package com.T4BAM.PawscuePh.Backend.TableRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.T4BAM.PawscuePh.Backend.TableClasses.AdopterPets;

@Repository
public interface AdopterPets_Repository extends JpaRepository<AdopterPets, String> {
    // Retrieves the last PetId
    @Query(value = "SELECT PetId FROM pawscueadoptions.adopter_pets ORDER BY PetId desc LIMIT 1", nativeQuery = true)
    String getLastId();
}
