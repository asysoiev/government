package com.government.citizens.dao.jpa;

import com.government.citizens.dao.CitizensDao;
import com.government.citizens.models.Citizen;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Andrii Sysoiev
 */
@Profile("JPA")
@Repository
public interface CitizensJpaRepository extends JpaRepository<Citizen, Long>, CitizensDao {
}
