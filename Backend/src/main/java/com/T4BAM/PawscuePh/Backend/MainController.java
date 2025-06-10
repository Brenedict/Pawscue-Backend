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

    // Deletes adopter record, {adopterId} specifies which adopter to delete 
    @CrossOrigin(origins = "*")
    @Transactional
    @DeleteMapping(path = "/adopter/{adopterId}/delete-record")
    public void deleteRecord(@PathVariable String adopterId) {
        // Queries the adopterAddressId from adopter record since home details is not affected by cascade deletion
        String adopterAddressId = adopterRepository.getAdopterById(adopterId).getAddressDetails().getAdopterAddressId();
        
        adopterRepository.deleteAdopterRecord(adopterId);
        adopterHomeDetailsRepository.deleteAdopterHomeDetailsRecord(adopterAddressId);
    }

    // Test function for quick clearing of database. WARNING: DELETES ALL RECORDS
    @CrossOrigin(origins = "*")
    @Transactional
    @DeleteMapping(path = "/delete-all-records")
    public void deleteAllRecords() {
        adopterRepository.deleteAllAdopterRecords();
        adopterHomeDetailsRepository.deleteAllAdopterHomeDetailsRecords();
    }

    public Adopter save_Adopter_and_HomeDetails_and_Spouse(Adopter adopter, String adopterId) {
        String adopterAddressId = idGeneration.generateAdopterAddressId();
        String spouseId = idGeneration.generateSpouseId();

        adopter.setAdopterId(adopterId);
        
        AdopterHomeDetails tempAdopterHomeDetails = adopter.getAddressDetails();
        adopter.setAddressDetails(null);

        Spouse tempSpouse = adopter.getSpouse();
        adopter.setSpouse(null);

        // Inserts the JSON into the DBMS without the Adopter class FK's: AdopterAddressId and SpouseId due to their parent and children relation
        adopterRepository.save(adopter);

        // Attaches the adopterAddressId to the AdopterHomeDetails Class
        tempAdopterHomeDetails.setAdopterAddressId(adopterAddressId);
        
        // Saves the home details to its respective relation
        adopterHomeDetailsRepository.save(tempAdopterHomeDetails);

        // Handles logic when the adopter has no spouse
        if(tempSpouse != null) {

            // Attaches the spouseId to the Spouse Class
            tempSpouse.setSpouseId(spouseId);
            
            // Attaches the adopterId to the Spouse Class that references the adopter
            tempSpouse.setAdopterId(adopterId);   
            
            spouseRepository.save(tempSpouse);
        } else {
            spouseId = null;
        }

        // Updates the adopter's foreign keys to correspond to the spouse and adopter home details entities
        adopterRepository.updateAdopterForeignKeys(adopterAddressId, spouseId, adopterId);
        
        // Properly sets the adopter entity to contain it's spouse and home details which was previously removed
        adopter.setAddressDetails(tempAdopterHomeDetails);
        adopter.setSpouse(tempSpouse);

        return adopter;
    }

    public List<AdopterPets> saveAdopterPets(List<AdopterPets> adopterPets, String adopterId) {
        for(int i = 0 ; i < adopterPets.size() ; i++) {
            String petId = idGeneration.generateAdopterPetsId();
            
            adopterPets.get(i).setPetid(petId);
            adopterPets.get(i).setAdopterid(adopterId);

            adopterPetsRepository.save(adopterPets.get(i));
        }

        return adopterPets;
    }

    public List<HouseholdAdults> saveHouseholdAdults(List<HouseholdAdults> householdAdults, String adopterId) {
        for(int i = 0 ; i < householdAdults.size() ; i++) {
            String householdAdultsId = idGeneration.generateHouseholdAdultsId();
            
            householdAdults.get(i).setHouseholdadultid(householdAdultsId);
            householdAdults.get(i).setAdopterid(adopterId);

            householdAdultsRepository.save(householdAdults.get(i));
        }

        return householdAdults;
    }

    // POST Function. Saves adoption record by API requests with the use of JSON.
    @CrossOrigin(origins = "*")
    @Transactional
    @PostMapping("/full-application/save")
    public ResponseEntity<AdoptionApplicationDTO> postFullAdoptionApplication(@RequestBody AdoptionApplicationDTO fullAdoptionForm) {
        // Generates the main adopterId
        String adopterId = idGeneration.generateAdopterId();

        // Handles adopter, spouse, home details primary key IDs, foreign keys logic, and insertion
        fullAdoptionForm.setAdopter(save_Adopter_and_HomeDetails_and_Spouse(fullAdoptionForm.getAdopter(), adopterId));
        
        // Handles single/multiple adopter pets id generation, foreign key logic and insertion
        fullAdoptionForm.setAdopterPets(saveAdopterPets(fullAdoptionForm.getAdopterPets(), adopterId));
        
        // Handles single/multiple household adults id generation, foreign key logic and insertion
        fullAdoptionForm.setHouseholdAdults(saveHouseholdAdults(fullAdoptionForm.getHouseholdAdults(), adopterId));
    
        return ResponseEntity.ok(fullAdoptionForm);
    }

    // GET Function. Gets the record of an adopter specified by {adopterId}
    @GetMapping("/full-application/{adopterId}")
    public ResponseEntity<AdoptionApplicationDTO> getFullAdoptionApplication(@PathVariable String adopterId) {
        AdoptionApplicationDTO adopterFullApplicationForm = new AdoptionApplicationDTO();

        adopterFullApplicationForm.setAdopter(adopterRepository.getAdopterById(adopterId));
        adopterFullApplicationForm.setAdopterPets(adopterPetsRepository.getAdopterPetsById(adopterId));
        adopterFullApplicationForm.setHouseholdAdults(householdAdultsRepository.getHouseholdAdultsById(adopterId));

        return ResponseEntity.ok(adopterFullApplicationForm);
    }
    
    // INCOMPLETE DO NOT TOUCH
    @CrossOrigin(origins = "*")
    @Transactional
    @PostMapping("full-application/update")
    public ResponseEntity<AdoptionApplicationDTO> updateFullAdoptionApplication(@RequestBody AdoptionApplicationDTO updated_fullAdoptionForm) {
        String adopterId = updated_fullAdoptionForm.getAdopter().getAdopterId();

        // When adopter updates spouse to be null (removes spouse)
        if(updated_fullAdoptionForm.getAdopter().getSpouse() == null) {
            spouseRepository.deleteSpouseRecord(adopterId);
        }

        // When adopter updates adopter pets to be null (removes all adopter pets)
        if(updated_fullAdoptionForm.getAdopterPets() == null) {
            adopterPetsRepository.deleteAdopterPetsRecord(adopterId);
        }

        // When adopter updates household adults to be null (removes all household adults)
        if(updated_fullAdoptionForm.getAdopterPets() == null) {
            householdAdultsRepository.deleteHouseholdAdultsRecord(adopterId);
        }
        
        adopterRepository.save(updated_fullAdoptionForm.getAdopter());
        
        return ResponseEntity.ok(updated_fullAdoptionForm);
    }
    
}
