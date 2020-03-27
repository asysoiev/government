package com.government.citizens.controllers;

import com.government.citizens.dao.CitizensDao;
import com.government.citizens.models.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Andrii Sysoiev
 */
@RestController
public class CitizensResController {

    @Autowired
    private CitizensDao citizensDao;

    @GetMapping(path = "/citizens")
    public List<Citizen> getAll() {
        return citizensDao.findAll();
    }

}
