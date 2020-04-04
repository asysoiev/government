package com.government.citizens.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static java.time.Month.AUGUST;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for CitizensRest API.
 * Checks all layers.
 *
 * @author Andrii Sysoiev
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CitizensRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testGetAllCitizens() throws Exception {
        mockMvc.perform(get("/citizens"))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(6)));
    }

    /**
     * Checks successful citizen creation.
     * Identifier must be generated.
     */
    @Test
    void testCreateCitizen() throws Exception {
        String name = "Eric";
        String surname = "Amber";
        String gender = "M";
        LocalDate birthday = of(now().minusYears(400).getYear(), AUGUST, 2);

        String content = "{\n" +
                "        \"name\": \"" + name + "\",\n" +
                "        \"surname\": \"" + surname + "\",\n" +
                "        \"birthday\": \"" + birthday.toString() + "\",\n" +
                "        \"gender\": \"" + gender + "\"\n" +
                " }";
        MockHttpServletRequestBuilder request = post("/citizens")
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
//                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());

        String query = "select count(*) " +
                "from citizen " +
                "where name=? and surname=? and gender=? and birthday=? and identifier is not null";
        Object[] params = {name, surname, gender, birthday};
        Integer count = jdbcTemplate.queryForObject(query, params, Integer.class);
        assertEquals(1, count);
    }

    /**
     * Checks successful citizen deletion.
     */
    @Test
    void testDeleteCitizen() throws Exception {
        String name = "Oberon";
        Object[] params = {name};
        Long id = jdbcTemplate.queryForObject("select id from citizen where name=?", params, Long.class);

        MockHttpServletRequestBuilder request = delete("/citizens/{1}", id);
        mockMvc.perform(request)
                .andExpect(status().isOk());

        int count = jdbcTemplate.queryForObject("select count(*) from citizen where name=?", params, Integer.class);
        assertEquals(0, count);
    }
}
