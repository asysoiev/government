package com.government.citizens.controllers;

import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for CitizensRest API.
 * Checks all layers.
 * With JDBC implementation of DB layer.
 *
 * @author Andrii Sysoiev
 */
@ActiveProfiles("JDBC")
public class CitizensRestControllerJdbcTest extends CitizensRestControllerTest {
}
