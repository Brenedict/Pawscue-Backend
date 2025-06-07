package com.T4BAM.PawscuePh.Backend.TableRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.T4BAM.PawscuePh.Backend.TableClasses.Adopter;
import com.T4BAM.PawscuePh.Backend.TableClasses.HouseholdAdults;

@Repository
public interface HouseholdAdults_Repository extends JpaRepository<HouseholdAdults, String> {
    // Retrieves the last HouseholdAdultId
    @Query(value = "SELECT HouseholdAdultId FROM pawscueadoptions.household_adults ORDER BY HouseholdAdultId desc LIMIT 1", nativeQuery = true)
    String getLastId();
}
