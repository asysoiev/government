package com.government.citizens.controllers;

import com.government.citizens.models.Citizen;
import com.government.citizens.services.CitizensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * @author Andrii Sysoiev
 */
@RestController
public class CitizensRestController {

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

    @PostMapping(path = "/citizens")
    public ResponseEntity<Object> createCitizen(@Valid @RequestBody Citizen citizen) {
        Citizen saved = citizensService.createCitizen(citizen);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "/citizens/{id}")
    public ResponseEntity<Object> updateCitizen(@PathVariable Long id, @Valid @RequestBody Citizen citizen) {
        citizen.setId(id);
        Citizen saved = citizensService.updateCitizen(citizen);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping(path = "/citizens/{id}")
    public void deleteCitizenById(@PathVariable Long id) {
        citizensService.deleteById(id);
    }

}
