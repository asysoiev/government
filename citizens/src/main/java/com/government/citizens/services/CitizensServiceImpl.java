package com.government.citizens.services;

import com.government.citizens.dao.CitizensDao;
import com.government.citizens.exceptions.CitizenNotFoundException;
import com.government.citizens.models.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Citizen result = citizensDao.findById(id);
        if (result == null) {
            throw new CitizenNotFoundException(id);
        }
        return result;
    }

    @Override
    public int deleteById(Long id) {
        int rows = citizensDao.deleteById(id);
        if (rows == 0) {
            throw new CitizenNotFoundException(id);
        }
        return rows;
    }
}
