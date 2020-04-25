package com.government.citizens.dao;

import com.government.citizens.models.Citizen;

import java.util.List;
import java.util.Optional;

/**
 * @author Andrii Sysoiev
 */
public interface CitizensDao {

    List<Citizen> findAll();

    Optional<Citizen> findById(Long id);

    List<Citizen> findBySurname(String surname);

    Citizen insert(Citizen citizen);

    Citizen update(Citizen citizen);

    void deleteById(Long id);
}
