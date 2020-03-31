package com.government.citizens.services;

import com.government.citizens.dao.CitizensDao;
import com.government.citizens.exceptions.CitizenNotFoundException;
import com.government.citizens.models.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public int deleteById(Long id) {
        int rows = citizensDao.deleteById(id);
        if (rows == 0) {
            throw new CitizenNotFoundException(id);
        }
        return rows;
    }

    @Override
    public Citizen createCitizen(Citizen citizen) {
        return citizensDao.save(citizen);
    }

    @Override
    public Citizen updateOrCreateCitizen(Citizen citizen) {
        Optional<Citizen> optional = citizensDao.findById(citizen.getId());
        if (optional.isPresent()) {
            Citizen updatedCitizen = optional.map(rec -> {
                if (citizen.getName() != null) {
                    rec.setName(citizen.getName());
                }
                if (citizen.getSurname() != null) {
                    rec.setSurname(citizen.getSurname());
                }
                if (citizen.getBirthday() != null) {
                    rec.setBirthday(citizen.getBirthday());
                }
                if (citizen.getGender() != null) {
                    rec.setGender(citizen.getGender());
                }
                rec.setDeathDate(citizen.getDeathDate());
                rec.setComment(citizen.getComment());
                return rec;
            }).get();
            return citizensDao.save(updatedCitizen);
        } else {
            return createCitizen(citizen);
        }
    }
}
