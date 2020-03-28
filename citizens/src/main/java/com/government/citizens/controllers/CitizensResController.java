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

import java.net.URI;
import java.util.List;
import java.util.Objects;

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

    @PostMapping(path = "/citizens")
    public ResponseEntity<Object> createCitizen(@RequestBody Citizen citizen) {
        Citizen saved = citizensService.createCitizen(citizen);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(path = "/citizens/{id}")
    public ResponseEntity<Object> updateOrCreateCitizen(@PathVariable Long id, @RequestBody Citizen citizen) {
        citizen.setId(id);
        Citizen saved = citizensService.updateOrCreateCitizen(citizen);
        if (!Objects.equals(saved.getId(), citizen.getId())) {
            //created
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(saved.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } else {
            //updated
            return ResponseEntity.ok(saved);
        }
    }

    @DeleteMapping(path = "/citizens/{id}")
    public void deleteCitizenById(@PathVariable Long id) {
        citizensService.deleteById(id);
    }

}
