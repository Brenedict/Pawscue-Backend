package com.T4BAM.PawscuePh.Backend;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.T4BAM.PawscuePh.Backend.TableClasses.Adopter;
import com.T4BAM.PawscuePh.Backend.TableRepositories.AdopterRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class MainController {

    private final AdopterRepository adopterRepository;

    @Autowired
    public MainController(AdopterRepository adopterRepository) {
        this.adopterRepository = adopterRepository;
    }

    @GetMapping
    public List<Adopter> testReturn() {
        return adopterRepository.findAll();
    }

}
