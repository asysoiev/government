package com.government.citizens.dao.jpa;

import com.government.citizens.dao.CitizensDao;
import com.government.citizens.models.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Andrii Sysoiev
 */
@Profile({"JpaRepo"})
@Repository
public class CitizensJpaRepoDaoAdapter implements CitizensDao {

    @Autowired
    private CitizensJpaRepository citizensJpaRepository;

    @Override
    public List<Citizen> findAll() {
        return citizensJpaRepository.findAll();
    }

    @Override
    public Optional<Citizen> findById(Long id) {
        return citizensJpaRepository.findById(id);
    }

    @Override
    public List<Citizen> findBySurname(String surname) {
        Citizen citizen = new Citizen();
        citizen.setSurname(surname);
        Example<Citizen> example = Example.of(citizen);
        return citizensJpaRepository.findAll(example);
    }

    @Override
    public Citizen insert(Citizen citizen) {
        return citizensJpaRepository.save(citizen);
    }

    @Override
    public Citizen update(Citizen citizen) {
        return citizensJpaRepository.save(citizen);
    }

    @Override
    public void delete(Citizen citizen) {
        citizensJpaRepository.delete(citizen);
    }
}
