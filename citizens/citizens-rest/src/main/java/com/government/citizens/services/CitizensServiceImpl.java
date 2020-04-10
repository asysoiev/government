package com.government.citizens.services;

import com.government.citizens.dao.CitizensDao;
import com.government.citizens.exceptions.CitizenNotFoundException;
import com.government.citizens.models.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;
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

    private static void validate(Citizen citizen) {
        if (isEmpty(citizen.getName())) {
            throw new ValidationException("Citizen is invalid: \"name\" field is required!");
        }
        if (isEmpty(citizen.getSurname())) {
            throw new ValidationException("Citizen is invalid: \"surname\" field is required!");
        }
        if (citizen.getBirthday() == null) {
            throw new ValidationException("Citizen is invalid: \"birthday\" field is required!");
        }
        if (citizen.getBirthday().isAfter(now())) {
            throw new ValidationException("Citizen is invalid: \"birthday\" can not be after current date!");
        }
        if (citizen.getGender() == null) {
            throw new ValidationException("Citizen is invalid: \"gender\" field is required!");
        }
        if (citizen.getDeathDate() != null && citizen.getDeathDate().isAfter(now())) {
            throw new ValidationException("Citizen is invalid: \"deathDate\" can not be after current date!");
        }
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        citizensDao.deleteById(id);
    }

    @Override
    public Citizen createCitizen(Citizen citizen) {
        if (isEmpty(citizen.getIdentifier())) {
            citizen.setIdentifier(randomUUID());
        }
        return save(citizen);
    }

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
        return save(dbCitizen);
    }

    //    @Transactional
    private Citizen save(Citizen citizen) {
        validate(citizen);
        return citizensDao.save(citizen);
    }
}
