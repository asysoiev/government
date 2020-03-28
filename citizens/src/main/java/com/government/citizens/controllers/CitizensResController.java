package com.government.citizens.controllers;

import com.government.citizens.models.Citizen;
import com.government.citizens.services.CitizensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Andrii Sysoiev
 */
@RestController
public class CitizensResController {

    @Autowired
    private CitizensService citizensService;

    @GetMapping(path = "/citizens")
    public List<Citizen> getAll() {
        return citizensService.findAll();
    }

    @GetMapping(path = "/citizens/{id}")
    public Citizen getCitizenById(@PathVariable Long id) {
        return citizensService.findById(id);
    }

    @DeleteMapping(path = "/citizens/{id}")
    public void deleteCitizenById(@PathVariable Long id) {
        citizensService.deleteById(id);
    }

}
