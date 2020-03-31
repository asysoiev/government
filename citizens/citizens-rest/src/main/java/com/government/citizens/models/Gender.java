package com.government.citizens.models;

/**
 * @author Andrii Sysoiev
 */
public enum Gender {
    M,
    F;

    public static Gender fromString(String value) {
        for (Gender gender : values()) {
            if (gender.toString().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        return null;
    }

}
