package com.government.citizens.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.Set;

/**
 * Integration tests for CitizensRest API.
 * Checks all layers.
 * With JDBC implementation of DB layer.
 *
 * @author Andrii Sysoiev
 */
@ActiveProfiles("JDBC")
public class CitizensRestControllerJdbcTest extends CitizensRestControllerTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    protected Long getCitizenId(Map<String, Object> params, Set<String> notNullParams) {
        String query = String.format("select id from citizen where %s %s",
                mapToStrictWherePairsString(params),
                setToStrictWhereNotNullString(notNullParams));
        return namedParameterJdbcTemplate.queryForObject(query, params, Long.class);
    }

    @Override
    protected int getCitizensCount(Map<String, Object> params, Set<String> notNullParams) {
        String query = String.format("select count(*) from citizen where %s %s",
                mapToStrictWherePairsString(params),
                setToStrictWhereNotNullString(notNullParams));
        return namedParameterJdbcTemplate.queryForObject(query, params, Integer.class);
    }
}
