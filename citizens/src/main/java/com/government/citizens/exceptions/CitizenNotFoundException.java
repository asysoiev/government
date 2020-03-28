package com.government.citizens.exceptions;

/**
 * @author Andrii Sysoiev
 */
public class CitizenNotFoundException extends RuntimeException {

    private static final String CITIZEN_NOT_FOUND_MSG = "Citizen: \"%s\" not found.";

    public CitizenNotFoundException(Long citizenId) {
        super(String.format(CITIZEN_NOT_FOUND_MSG, citizenId));
    }
}
