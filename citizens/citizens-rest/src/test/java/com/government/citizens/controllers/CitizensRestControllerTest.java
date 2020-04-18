package com.government.citizens.controllers;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static java.time.Month.AUGUST;
import static java.util.stream.Collectors.joining;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Base class of integration tests for CitizensRest API.
 * Checks all layers.
 *
 * @author Andrii Sysoiev
 */
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public abstract class CitizensRestControllerTest {

    private static final String VALIDATION_ERR_MESSAGE = "Validation error!";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllCitizens() throws Exception {
        mockMvc.perform(get("/citizens"))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(6)));
    }

    protected static String mapToStrictWherePairsString(Map<String, Object> params) {
        return params.entrySet()
                .stream()
                .map(e -> e.getKey() + "=:" + e.getKey())
                .collect(joining(" and "));
    }

    protected static String setToStrictWhereNotNullString(Set<String> notNullParams) {
        if (CollectionUtils.isEmpty(notNullParams)) {
            return "";
        }
        String notNullPattern = "%s is not null";
        return " and " + notNullParams.stream().map(param -> String.format(notNullPattern, param)).collect(joining(" and "));
    }

    @Test
    void testCreateCitizen_NameIsRequired() throws Exception {
        String surname = "Amber";
        String gender = "M";
        LocalDate birthday = of(now().minusYears(400).getYear(), AUGUST, 2);

        String content = "{\n" +
                "        \"surname\": \"" + surname + "\",\n" +
                "        \"birthday\": \"" + birthday.toString() + "\",\n" +
                "        \"gender\": \"" + gender + "\"\n" +
                " }";
        MockHttpServletRequestBuilder request = post("/citizens")
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(VALIDATION_ERR_MESSAGE)))
                .andExpect(jsonPath("$.details", is(getRequiredFieldMessage("name"))));
    }

    @Test
    void testCreateCitizen_WrongGenderValue() throws Exception {
        String surname = "Amber";
        String gender = "S";

        String content = "{\n" +
                "        \"surname\": \"" + surname + "\",\n" +
                "        \"gender\": \"" + gender + "\"\n" +
                " }";
        MockHttpServletRequestBuilder request = post("/citizens")
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(VALIDATION_ERR_MESSAGE)))
                .andExpect(jsonPath("$.details", containsString("\"S\": not one of the values accepted for Enum class: [M, F];")));
    }

    @Test
    void testCreateCitizen_SurnameIsRequired() throws Exception {
        String name = "Eric";
        String gender = "M";
        LocalDate birthday = of(now().minusYears(400).getYear(), AUGUST, 2);

        String content = "{\n" +
                "        \"name\": \"" + name + "\",\n" +
                "        \"birthday\": \"" + birthday.toString() + "\",\n" +
                "        \"gender\": \"" + gender + "\"\n" +
                " }";
        MockHttpServletRequestBuilder request = post("/citizens")
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(VALIDATION_ERR_MESSAGE)))
                .andExpect(jsonPath("$.details", is(getRequiredFieldMessage("surname"))));
    }

    private static String getRequiredFieldMessage(String field) {
        return String.format("Citizen is invalid: \"%s\" field is required!", field);
    }

    @Test
    void testGetCitizen_NotFound() throws Exception {
        //check before state
        long id = 888L;
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        int count = getCitizensCount(params);
        assertEquals(0, count);

        //call
        MockHttpServletRequestBuilder request = get("/citizens/{1}", id);
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(getCitizenNotFoundMessage(id))));
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

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("surname", surname);
        params.put("gender", gender);
        params.put("birthday", birthday);
        Set<String> notNullParams = new HashSet<>();
        notNullParams.add("identifier");
        int count = getCitizensCount(params, notNullParams);
        assertEquals(1, count);
    }

    @Test
    void testUpdateCitizen() throws Exception {
        //prepare data
        String name = "Oberon";
        LocalDate deathDate = of(now().minusYears(100).getYear(), AUGUST, 2);
        String comment = "Dead";

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        Long id = getCitizenId(params);
        assertNotNull(id);
        //fields are null
        params = new HashMap<>();
        params.put("name", name);
        params.put("comment", comment);
        params.put("death_date", deathDate);
        int count = getCitizensCount(params);
        assertEquals(0, count);

        //request
        String content = "{\n" +
                "        \"deathDate\": \"" + deathDate + "\",\n" +
                "        \"comment\": \"" + comment + "\"\n" +
                " }";
        MockHttpServletRequestBuilder request = put("/citizens/{1}", id)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .content(content);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.deathDate", is(deathDate.toString())))
                .andExpect(jsonPath("$.comment", is(comment)));

        //check result
        count = getCitizensCount(params);
        assertEquals(1, count);
    }

    private static String getCitizenNotFoundMessage(long id) {
        return String.format("Citizen: \"%d\" not found.", id);
    }


    //region Should be refactored.
    /*
    Have not found how to check jpa db changes by jdbctemplate.
    JpaRepository does not flush changes until transaction is committed,
    so jdbctemplate does not return expected values and we have to check changes by EntityManager.
    See testCreateCitizen, testUpdateCitizen, testDeleteCitizen.
     */

    /**
     * Checks successful citizen deletion.
     */
    @Test
    void testDeleteCitizen() throws Exception {
        //prepare test data
        String name = "Oberon";
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        Long id = getCitizenId(params);

        //call
        MockHttpServletRequestBuilder request = delete("/citizens/{1}", id);
        mockMvc.perform(request)
                .andExpect(status().isOk());

        //check
        int count = getCitizensCount(params);
        assertEquals(0, count);
    }

    @Test
    void testDeleteCitizen_NotFound() throws Exception {
        //check db state
        long id = 777L;
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        int count = getCitizensCount(params);
        assertEquals(0, count);

        //call
        MockHttpServletRequestBuilder request = delete("/citizens/{1}", id);
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(getCitizenNotFoundMessage(id))));
    }

    protected Long getCitizenId(Map<String, Object> params) {
        return getCitizenId(params, new HashSet<>());
    }

    protected abstract Long getCitizenId(Map<String, Object> params, Set<String> notNullParams);

    protected int getCitizensCount(Map<String, Object> params) {
        return getCitizensCount(params, new HashSet<>());
    }

    protected abstract int getCitizensCount(Map<String, Object> params, Set<String> notNullParams);
    //endregion
}
