package com.government.citizens.services;

import com.government.citizens.exceptions.CitizenNotFoundException;
import com.government.citizens.models.Citizen;

import java.util.List;

/**
 * @author Andrii Sysoiev
 */
public interface CitizensService {

    List<Citizen> findAll();

    /**
     * Search a citizen by id.
     *
     * @param id
     * @return
     * @throws CitizenNotFoundException if a citizen was not found
     */
    Citizen findById(Long id);

    /**
     * Delete a citizen by id.
     *
     * @param id
     * @return
     * @throws CitizenNotFoundException if a citizen was not found
     */
    int deleteById(Long id);

}
