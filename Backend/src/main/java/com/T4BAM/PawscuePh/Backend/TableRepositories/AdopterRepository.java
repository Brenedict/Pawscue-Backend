package com.T4BAM.PawscuePh.Backend.TableRepositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.T4BAM.PawscuePh.Backend.TableClasses.Adopter;

@Repository
public interface AdopterRepository extends JpaRepository<Adopter, String> {

}
