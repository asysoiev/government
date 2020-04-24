package com.government.citizens.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;

/**
 * @author Andrii Sysoiev
 */
@Entity
public class Citizen {
    @Id
    @SequenceGenerator(name = "citizen_seq", allocationSize = 1, initialValue = 10000)
    @GeneratedValue(generator = "citizen_seq")
    private Long id;
    @Column(name = "identifier")
    private UUID identifier;
    @NotEmpty(message = "Citizen is invalid: \"name\" field is required!")
    @Column(name = "name")
    private String name;
    @NotEmpty(message = "Citizen is invalid: \"surname\" field is required!")
    @Column(name = "surname")
    private String surname;
    @NotNull(message = "Citizen is invalid: \"birthday\" field is required!")
    @Past(message = "Citizen is invalid: \"birthday\" can not be after current date!")
    @Column(name = "birthday")
    private LocalDate birthday;
    @NotNull(message = "Citizen is invalid: \"gender\" field is required!")
    @Enumerated(STRING)
    @Column(name = "gender")
    private Gender gender;
    //TODO: implement custom validator: must be after or eq birthday
    @Past(message = "Citizen is invalid: \"deathDate\" can not be after current date!")
    @Column(name = "death_date")
    private LocalDate deathDate;
    @Column(name = "comment")
    private String comment;

    public Citizen() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(UUID identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
