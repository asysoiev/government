package com.government.citizens.dao.jpa;

import com.government.citizens.dao.CitizensDao;
import com.government.citizens.models.Citizen;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * @author Andrii Sysoiev
 */
@Profile({"EntityManager"})
@Repository
public class CitizenEntityManagerAdapter implements CitizensDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Citizen> findAll() {
        TypedQuery<Citizen> query = entityManager.createNamedQuery("Citizen.findAll", Citizen.class);
        return query.getResultList();
    }

    @Override
    public Optional<Citizen> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Citizen.class, id));
    }

    @Override
    public List<Citizen> findBySurname(String surname) {
        TypedQuery<Citizen> query = entityManager.createNamedQuery("Citizen.findBySurname", Citizen.class);
        query.setParameter("surname", surname);
        return query.getResultList();
    }

    @Override
    public Citizen insert(Citizen citizen) {
        entityManager.persist(citizen);
        return citizen;
    }

    @Override
    public Citizen update(Citizen citizen) {
        entityManager.persist(citizen);
        return citizen;
    }

    @Override
    public void delete(Citizen citizen) {
        entityManager.remove(citizen);
    }
}
