package com.government.citizens.controllers;

import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for CitizensRest API.
 * Checks all layers.
 * With JPA implementation of DB layer.
 *
 * @author Andrii Sysoiev
 */
@ActiveProfiles("JPA")
public class CitizensRestControllerJpaTest extends CitizensRestControllerTest {
}
