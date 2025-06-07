package com.T4BAM.PawscuePh.Backend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// Entity Imports
import com.T4BAM.PawscuePh.Backend.TableClasses.Adopter;
import com.T4BAM.PawscuePh.Backend.TableClasses.AdopterHomeDetails;
import com.T4BAM.PawscuePh.Backend.TableClasses.AdopterPets;
import com.T4BAM.PawscuePh.Backend.TableClasses.HouseholdAdults;
import com.T4BAM.PawscuePh.Backend.TableClasses.Spouse;

// Entity Repository Imports
import com.T4BAM.PawscuePh.Backend.TableRepositories.Adopter_Repository;
import com.T4BAM.PawscuePh.Backend.TableRepositories.AdopterHomeDetails_Repository;
import com.T4BAM.PawscuePh.Backend.TableRepositories.AdopterPets_Repository;
import com.T4BAM.PawscuePh.Backend.TableRepositories.HouseholdAdults_Repository;
import com.T4BAM.PawscuePh.Backend.TableRepositories.Spouse_Repository;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/adoption-record")
public class MainController {

    private final Adopter_Repository adopterRepository;
    private final AdopterHomeDetails_Repository adopterHomeDetailsRepository;
    private final AdopterPets_Repository adopterPetsRepository;
    private final HouseholdAdults_Repository householdAdultsRepository;
    private final Spouse_Repository spouseRepository;

    private final IdGeneration idGeneration;

    @Autowired
    public MainController(
            Adopter_Repository adopterRepository, 
            AdopterHomeDetails_Repository adopterHomeDetailsRepository,
            AdopterPets_Repository adopterPetsRepository,
            HouseholdAdults_Repository householdAdultsRepository,
            Spouse_Repository spouseRepository,
            IdGeneration idGeneration) 
    {
        this.adopterRepository = adopterRepository;
        this.adopterHomeDetailsRepository = adopterHomeDetailsRepository;
        this.adopterPetsRepository = adopterPetsRepository;
        this.householdAdultsRepository = householdAdultsRepository;
        this.spouseRepository = spouseRepository;
        this.idGeneration = idGeneration;
    }

    // Open on web browser and search: localhost:8080/api/adopters
    @GetMapping(path = "/adopter")
    public List<Adopter> getAdopters() {
        return adopterRepository.findAll();
    }
    
    // Open on web browser and search: localhost:8080/api/adopter-home-details
    @GetMapping(path = "/adopter-home-details")
    public List<AdopterHomeDetails> getAdopterHomeDetails() {
        return adopterHomeDetailsRepository.findAll();
    }

    @GetMapping(path = "/adopter-pets") 
    public List<AdopterPets> getAdopterPets() {
        return adopterPetsRepository.findAll();
    }

    @GetMapping(path = "/household-adults")
    public List<HouseholdAdults> getHouseholdAdults() {
        return householdAdultsRepository.findAll();
    }

    @GetMapping(path = "/spouse")
    public List<Spouse> getSpouse() {
        return spouseRepository.findAll();
    }

    @GetMapping(path = "/adopter/{adopterId}")
    public Adopter getAdopterById(@PathVariable String adopterId) {
        return adopterRepository.getAdopterById(adopterId);
    }

    @Transactional
    @DeleteMapping(path = "/adopter/{adopterId}/delete-record")
    public void deleteRecord(@PathVariable String adopterId) {
        // Queries the adopterAddressId from adopter record since home details is not affected by cascade deletion
        // String adopterAddressId = adopterRepository.getAdopterById(adopterId).getAddressDetails().getAdopterAddressId();
        
        adopterRepository.deleteAdopterRecord(adopterId);
        // adopterHomeDetailsRepository.deleteAdopterHomeDetailsRecord(adopterAddressId);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(path = "/adopter/save")
    public ResponseEntity<Adopter> postAdopter(@RequestBody Adopter adopter) {
        String adopterId = idGeneration.generateAdopterId();
        adopter.setAdopterId(adopterId);
        
        // Inserts the JSON into the DBMS without the FK's: AdopterAddressId and SpouseId due to their parent and children relation
        adopterRepository.save(adopter);
        
        // if(adopter.getSpouse() != null) {
        //     adopter.getSpouse().setAdopterId(idGeneration.generateSpouseId());
        // }

        // adopter.getAddressDetails().setAdopterAddressId(idGeneration.generateAdopterAddressId());

        return ResponseEntity.ok(adopter);
    }

    
    
}
