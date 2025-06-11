package com.T4BAM.PawscuePh.Backend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// Entity Imports
import com.T4BAM.PawscuePh.Backend.TableClasses.Adopter;
import com.T4BAM.PawscuePh.Backend.TableClasses.AdopterHomeDetails;
import com.T4BAM.PawscuePh.Backend.TableClasses.AdopterPets;
import com.T4BAM.PawscuePh.Backend.TableClasses.AdoptionApplicationDTO;
import com.T4BAM.PawscuePh.Backend.TableClasses.HouseholdAdults;
import com.T4BAM.PawscuePh.Backend.TableClasses.Spouse;

// Entity Repository Imports
import com.T4BAM.PawscuePh.Backend.TableRepositories.Adopter_Repository;
import com.T4BAM.PawscuePh.Backend.TableRepositories.AdopterHomeDetails_Repository;
import com.T4BAM.PawscuePh.Backend.TableRepositories.AdopterPets_Repository;
import com.T4BAM.PawscuePh.Backend.TableRepositories.HouseholdAdults_Repository;
import com.T4BAM.PawscuePh.Backend.TableRepositories.Spouse_Repository;
import com.T4BAM.PawscuePh.Backend.service.AdoptionFormInsertionLogic;
import com.T4BAM.PawscuePh.Backend.service.IdGeneration;

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
    private final AdoptionFormInsertionLogic adoptionFormInsertionLogic;

    @Autowired
    public MainController(
            Adopter_Repository adopterRepository, 
            AdopterHomeDetails_Repository adopterHomeDetailsRepository,
            AdopterPets_Repository adopterPetsRepository,
            HouseholdAdults_Repository householdAdultsRepository,
            Spouse_Repository spouseRepository,
            IdGeneration idGeneration,
            AdoptionFormInsertionLogic adoptionFormInsertionLogic) 
    {
        this.adopterRepository = adopterRepository;
        this.adopterHomeDetailsRepository = adopterHomeDetailsRepository;
        this.adopterPetsRepository = adopterPetsRepository;
        this.householdAdultsRepository = householdAdultsRepository;
        this.spouseRepository = spouseRepository;
        this.idGeneration = idGeneration;
        this.adoptionFormInsertionLogic = adoptionFormInsertionLogic;
    }

    // General Fetch GET method requests
    @GetMapping(path = "/adopter")
    public List<Adopter> getAdopters() {
        return adopterRepository.findAll();
    }
    
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

    // >> CRUD Function Operations

    // DELETE Function. WARNING: DELETES ALL RECORDS!
    @CrossOrigin(origins = "*")
    @Transactional
    @DeleteMapping(path = "/delete-all-records")
    public void deleteAllRecords() {
        adopterRepository.deleteAllAdopterRecords();
        adopterHomeDetailsRepository.deleteAllAdopterHomeDetailsRecords();
    }

    // DELETE Function. Deletes the record of an adopter specified by {adopterId}
    @CrossOrigin(origins = "*")
    @Transactional
    @DeleteMapping(path = "/full-application/{adopterId}/delete-record")
    public void deleteRecord(@PathVariable String adopterId) {
        // Queries the adopterAddressId from adopter record since home details is not affected by cascade deletion
        String adopterAddressId = adopterRepository.getAdopterById(adopterId).getAddressDetails().getAdopterAddressId();
        
        adopterRepository.deleteAdopterRecord(adopterId);
        adopterHomeDetailsRepository.deleteAdopterHomeDetailsRecord(adopterAddressId);
    }

    // POST Function. Saves adoption record by API requests with the use of JSON.
    @CrossOrigin(origins = "*")
    @Transactional
    @PostMapping("/full-application/save")
    public ResponseEntity<AdoptionApplicationDTO> postFullAdoptionApplication(@RequestBody AdoptionApplicationDTO fullAdoptionForm) {
        // Generates the main adopterId
        String adopterId = idGeneration.generateAdopterId();

        // Handles adopter, spouse, home details primary key IDs, foreign keys logic, and insertion
        fullAdoptionForm.setAdopter(adoptionFormInsertionLogic.save_Adopter_and_HomeDetails_and_Spouse(fullAdoptionForm.getAdopter(), adopterId));
        
        // Handles single/multiple adopter pets id generation, foreign key logic and insertion
        fullAdoptionForm.setAdopterPets(adoptionFormInsertionLogic.saveAdopterPets(fullAdoptionForm.getAdopterPets(), adopterId));
        
        // Handles single/multiple household adults id generation, foreign key logic and insertion
        fullAdoptionForm.setHouseholdAdults(adoptionFormInsertionLogic.saveHouseholdAdults(fullAdoptionForm.getHouseholdAdults(), adopterId));
    
        return ResponseEntity.ok(fullAdoptionForm);
    }

    // GET Function. Gets the record of an adopter specified by {adopterId}
    @CrossOrigin(origins = "*")
    @GetMapping("/full-application/{adopterId}")
    public ResponseEntity<AdoptionApplicationDTO> getFullAdoptionApplication(@PathVariable String adopterId) {
        AdoptionApplicationDTO adopterFullApplicationForm = new AdoptionApplicationDTO();

        adopterFullApplicationForm.setAdopter(adopterRepository.getAdopterById(adopterId));
        adopterFullApplicationForm.setAdopterPets(adopterPetsRepository.getAdopterPetsById(adopterId));
        adopterFullApplicationForm.setHouseholdAdults(householdAdultsRepository.getHouseholdAdultsById(adopterId));

        return ResponseEntity.ok(adopterFullApplicationForm);
    }
    
    // UPDATE (POST) Function. Updates the details of the adopter
    @CrossOrigin(origins = "*")
    @Transactional
    @PostMapping("full-application/update")
    public ResponseEntity<AdoptionApplicationDTO> updateFullAdoptionApplication(@RequestBody AdoptionApplicationDTO updated_fullAdoptionForm) {
        String adopterId = updated_fullAdoptionForm.getAdopter().getAdopterId();
        String adopterAddressId = updated_fullAdoptionForm.getAdopter().getAddressDetails().getAdopterAddressId();

        Spouse tempSpouse = updated_fullAdoptionForm.getAdopter().getSpouse();
        List<AdopterPets> tempAdopterPets = updated_fullAdoptionForm.getAdopterPets();
        List<HouseholdAdults> tempHouseholdAdults = updated_fullAdoptionForm.getHouseholdAdults();

        // Sets spouse to null to rebuild its state
        updated_fullAdoptionForm.getAdopter().setSpouse(null);
        
        // Concurrently update adopter details and adopter home details
        adopterRepository.save(updated_fullAdoptionForm.getAdopter());

        // Clear all previous dependencies: Spouse, Adopter Pets, Household Adults
        spouseRepository.deleteSpouseRecord(adopterId);
        adopterPetsRepository.deleteAdopterPetsRecord(adopterId);
        householdAdultsRepository.deleteHouseholdAdultsRecord(adopterId);
        
        // If user updates spouse with details (not null/empty)
        if(tempSpouse != null) {
            tempSpouse = adoptionFormInsertionLogic.saveSpouse(tempSpouse, adopterId, adopterAddressId);
            updated_fullAdoptionForm.getAdopter().setSpouse(tempSpouse);
        }

        // If user updates adopter pets with details (not null/empty)
        if (tempAdopterPets != null || !tempAdopterPets.isEmpty()) {
            tempAdopterPets = adoptionFormInsertionLogic.saveAdopterPets(tempAdopterPets, adopterId);
        }

        // If user updates household adults with details (not null/empty)
        if (tempHouseholdAdults != null || !tempHouseholdAdults.isEmpty()) {
            tempHouseholdAdults = adoptionFormInsertionLogic.saveHouseholdAdults(tempHouseholdAdults, adopterId);
        }

        // Updates the adoption form to contain the newly updated json with generated id's
        updated_fullAdoptionForm.getAdopter().setSpouse(tempSpouse);
        updated_fullAdoptionForm.setAdopterPets(tempAdopterPets);
        updated_fullAdoptionForm.setHouseholdAdults(tempHouseholdAdults);

        return ResponseEntity.ok(updated_fullAdoptionForm);
    }
    
}
