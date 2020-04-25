package com.government.citizens.dao.jdbc;

import com.government.citizens.dao.CitizensDao;
import com.government.citizens.models.Citizen;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrii Sysoiev
 */
@Profile("JDBC")
@Repository
public class CitizensJdbcDao implements CitizensDao {

    private static final Logger logger = getLogger(CitizensJdbcDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Citizen> findAll() {
        return jdbcTemplate.query("select * from citizen", new BeanPropertyRowMapper<>(Citizen.class));
    }

    @Override
    public Optional<Citizen> findById(Long id) {
        Citizen dbResult = null;
        try {
            dbResult = jdbcTemplate.queryForObject("select * from citizen where id=?", new Object[]{id},
                    new BeanPropertyRowMapper<>(Citizen.class)
            );
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Citizen \"{}\" not found", id);
        }
        return Optional.ofNullable(dbResult);
    }

    @Override
    public List<Citizen> findBySurname(String surname) {
        return jdbcTemplate.query("select * from citizen where surname=?",
                new Object[]{surname},
                new BeanPropertyRowMapper<>(Citizen.class));
    }

    @Override
    public Citizen save(Citizen citizen) {
        if (citizen.getId() == null) {
            return insert(citizen);
        } else {
            return update(citizen);
        }
    }

    private Citizen insert(Citizen citizen) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("citizen")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> args = new HashMap<>();
        args.put("name", citizen.getName());
        args.put("surname", citizen.getSurname());
        args.put("identifier", citizen.getIdentifier());
        args.put("birthday", citizen.getBirthday());
        args.put("gender", citizen.getGender().toString());
        args.put("death_date", citizen.getDeathDate());
        args.put("comment", citizen.getComment());
        Long id = (Long) simpleJdbcInsert.executeAndReturnKey(args);
        citizen.setId(id);
        return citizen;
    }

    private Citizen update(Citizen citizen) {
        jdbcTemplate.update("update citizen " +
                        "set name = ?, " +
                        "surname = ?, " +
                        "birthday = ?, " +
                        "gender = ?, " +
                        "death_date = ?, " +
                        "comment = ? " +
                        "where id = ?",
                new Object[]{citizen.getName(),
                        citizen.getSurname(),
                        citizen.getBirthday(),
                        citizen.getGender().toString(),
                        citizen.getDeathDate(), citizen.getComment(),
                        citizen.getId()});
        return citizen;
    }

    @Override
    public void deleteById(Long id) {
        int deletedRecs = jdbcTemplate.update("delete from citizen where id=?", new Object[]{id});
        logger.debug("Deleleted records {} by id {}", deletedRecs, id);
    }
}
