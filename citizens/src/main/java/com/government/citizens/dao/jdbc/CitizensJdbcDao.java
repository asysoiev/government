package com.government.citizens.dao.jdbc;

import com.government.citizens.dao.CitizensDao;
import com.government.citizens.models.Citizen;
import com.government.citizens.models.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.time.LocalDate.now;
import static org.springframework.util.StringUtils.isEmpty;

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

    private static void validate(Citizen citizen) {
        if (isEmpty(citizen.getName())) {
            throw new ValidationException("Citizen is invalid: \"name\" field is required!");
        }
        if (isEmpty(citizen.getSurname())) {
            throw new ValidationException("Citizen is invalid: \"surname\" field is required!");
        }
        if (citizen.getBirthday() == null) {
            throw new ValidationException("Citizen is invalid: \"birthday\" field is required!");
        }
        if (citizen.getBirthday().isAfter(now())) {
            throw new ValidationException("Citizen is invalid: \"birthday\" can not be after current date!");
        }
        if (isEmpty(citizen.getGender())) {
            throw new ValidationException("Citizen is invalid: \"gender\" field is required!");
        }
        Gender gender = Gender.fromString(citizen.getGender());
        if (gender == null) {
            String msg = String.format("Citizen is invalid: \"gender\" must be one of: %s!", Arrays.toString(Gender.values()));
            throw new ValidationException(msg);
        }
        if (citizen.getDeathDate() != null && citizen.getDeathDate().isAfter(now())) {
            throw new ValidationException("Citizen is invalid: \"deathDate\" can not be after current date!");
        }
    }

    @Override
    public Optional<Citizen> findById(Long id) {
        try {
            Citizen dbResult = jdbcTemplate.queryForObject("select * from citizen where id=?", new Object[]{id},
                    new BeanPropertyRowMapper<>(Citizen.class)
            );
            return Optional.ofNullable(dbResult);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Citizen save(Citizen citizen) {
        validate(citizen);
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
        args.put("birthday", citizen.getBirthday());
        args.put("gender", citizen.getGender());
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
                        "comment = ? ",
                new Object[]{citizen.getName(),
                        citizen.getSurname(),
                        citizen.getBirthday(),
                        citizen.getGender(),
                        citizen.getDeathDate(), citizen.getComment()});
        return citizen;
    }

    @Override
    public int deleteById(Long id) {
        return jdbcTemplate.update("delete from citizen where id=?", new Object[]{id});
    }
}
