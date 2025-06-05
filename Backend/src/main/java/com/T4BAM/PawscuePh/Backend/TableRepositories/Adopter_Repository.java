package com.T4BAM.PawscuePh.Backend.TableRepositories;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.T4BAM.PawscuePh.Backend.TableClasses.Adopter;

@Repository
public interface Adopter_Repository extends JpaRepository<Adopter, String> {
    @Query(value = "SELECT * FROM Adopter WHERE AdopterId = :adopterId", nativeQuery = true)
    List<Adopter> getAdopterById(String adopterId);
}
