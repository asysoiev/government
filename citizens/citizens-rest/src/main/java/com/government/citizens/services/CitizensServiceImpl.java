package com.government.citizens.services;

import com.government.citizens.dao.CitizensDao;
import com.government.citizens.exceptions.CitizenNotFoundException;
import com.government.citizens.models.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * @author Andrii Sysoiev
 */
@Service
public class CitizensServiceImpl implements CitizensService {

    @Autowired
    private CitizensDao citizensDao;

    @Override
    public List<Citizen> findAll() {
        return citizensDao.findAll();
    }

    @Override
    public Citizen findById(Long id) {
        Optional<Citizen> result = citizensDao.findById(id);
        if (result.isEmpty()) {
            throw new CitizenNotFoundException(id);
        }
        return result.get();
    }

    @Override
    public List<Citizen> findBySurname(String surname) {
        return citizensDao.findBySurname(surname);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Citizen citizen = findById(id);
        citizensDao.delete(citizen);
    }

    @Transactional
    @Override
    public Citizen createCitizen(Citizen citizen) {
        if (isEmpty(citizen.getIdentifier())) {
            citizen.setIdentifier(randomUUID());
        }
        return citizensDao.insert(citizen);
    }

    @Transactional
    @Override
    public Citizen updateCitizen(Citizen citizen) {
        Citizen dbCitizen = findById(citizen.getId());
        if (citizen.getName() != null) {
            dbCitizen.setName(citizen.getName());
        }
        if (citizen.getSurname() != null) {
            dbCitizen.setSurname(citizen.getSurname());
        }
        if (citizen.getBirthday() != null) {
            dbCitizen.setBirthday(citizen.getBirthday());
        }
        if (citizen.getGender() != null) {
            dbCitizen.setGender(citizen.getGender());
        }
        dbCitizen.setDeathDate(citizen.getDeathDate());
        dbCitizen.setComment(citizen.getComment());
        return citizensDao.update(dbCitizen);
    }
}
