package com.government.citizens.dao;

import com.government.citizens.models.Citizen;

import java.util.List;

/**
 * @author Andrii Sysoiev
 */
public interface CitizensDao {

    List<Citizen> findAll();
}
