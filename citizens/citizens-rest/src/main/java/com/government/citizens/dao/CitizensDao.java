package com.government.citizens.dao;

import com.government.citizens.models.Citizen;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

/**
 * @author Andrii Sysoiev
 */
public interface CitizensDao {

    List<Citizen> findAll();

    Optional<Citizen> findById(Long id);

    /**
     * Stores citizen.
     *
     * @param citizen
     * @return
     * @throws ValidationException if citizen is invalid.
     */
    Citizen save(Citizen citizen);

    int deleteById(Long id);
}
