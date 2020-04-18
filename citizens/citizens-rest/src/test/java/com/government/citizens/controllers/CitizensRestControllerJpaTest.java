package com.government.citizens.controllers;

import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

/**
 * Integration tests for CitizensRest API.
 * Checks all layers.
 * With JPA implementation of DB layer.
 *
 * @author Andrii Sysoiev
 */
@ActiveProfiles("JPA")
public class CitizensRestControllerJpaTest extends CitizensRestControllerTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected Long getCitizenId(Map<String, Object> params, Set<String> notNullParams) {
        String sql = String.format("select id from citizen where %s %s",
                mapToStrictWherePairsString(params),
                setToStrictWhereNotNullString(notNullParams));
        Query query = entityManager.createNativeQuery(sql);
        params.forEach(query::setParameter);
        return ((BigInteger) query.getSingleResult()).longValue();
    }

    @Override
    protected int getCitizensCount(Map<String, Object> params, Set<String> notNullParams) {
        String sql = String.format("select count(*) from citizen where %s %s",
                mapToStrictWherePairsString(params),
                setToStrictWhereNotNullString(notNullParams));
        Query query = entityManager.createNativeQuery(sql);
        params.forEach(query::setParameter);
        return ((BigInteger) query.getSingleResult()).intValue();
    }
}
