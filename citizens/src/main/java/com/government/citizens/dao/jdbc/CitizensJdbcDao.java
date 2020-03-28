package com.government.citizens.dao.jdbc;

import com.government.citizens.dao.CitizensDao;
import com.government.citizens.models.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Andrii Sysoiev
 */
@Repository
public class CitizensJdbcDao implements CitizensDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Citizen> findAll() {
        return jdbcTemplate.query("select * from citizen", new BeanPropertyRowMapper<>(Citizen.class));
    }

    @Override
    public Citizen findById(Long id) {
        try {
            return jdbcTemplate.queryForObject("select * from citizen where id=?", new Object[]{id},
                    new BeanPropertyRowMapper<>(Citizen.class)
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update("delete from citizen where id=?", new Object[]{id});
    }
}
