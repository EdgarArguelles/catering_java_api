package com.catering.integration_test;

import com.catering.models.CourseType;
import com.catering.models.Dish;
import com.catering.repositories.*;
import com.catering.security.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("unchecked")
public class DishIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private QuotationRepository quotationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private CourseTypeRepository courseTypeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private List<Dish> dbDishes;

    private IntegrationTest integrationTest;

    @BeforeEach
    public void setup() throws Exception {
        integrationTest = new IntegrationTest(mvc, mapper, tokenService);
        IntegrationTest.cleanAllData(courseRepository, menuRepository, quotationRepository, personRepository,
                roleRepository, dishRepository, courseTypeRepository, categoryRepository);

        final List<CourseType> dbCourseTypes = List.of(new CourseType("N1", "P1", 1, 1));
        courseTypeRepository.saveAll(dbCourseTypes);

        dbDishes = List.of(new Dish("Dish 1", "D1", "P1", 5F, 1, dbCourseTypes.get(0), null));
        dishRepository.saveAll(dbDishes);
    }

    /**
     * Should return an error response
     */
    @Test
    public void validateGraphQLIgnore() throws Exception {
        final String query = "query {dish(id: 1) {id name courseType courses categories{dishes}}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, null);
        final var errors = (List<Map<String, String>>) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("Field 'courseType' in type 'Dish' is undefined"));
        assertTrue(errors.get(1).get("message").contains("Field 'courses' in type 'Dish' is undefined"));
        assertTrue(errors.get(2).get("message").contains("Field 'dishes' in type 'Category' is undefined"));
    }

    /**
     * Should return an error response
     */
    @Test
    public void dishNotID() throws Exception {
        final String query = "query {dish {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, null);
        final var errors = (List<Map<String, String>>) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("Missing field argument id"));
    }

    /**
     * Should return a 404 error response
     */
    @Test
    public void dishNotFound() throws Exception {
        final String query = "query {dish(id: 123456) {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, null);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Object>) errors.get(0).get("extensions");
        final var error = (Map<String, Object>) extensions.get("error");

        assertNull(data.get("dish"));
        assertEquals(404, extensions.get("errorCode"));
        assertEquals("Data don't found.", error.get("message"));
    }

    /**
     * Should return a success response
     */
    @Test
    public void dish() throws Exception {
        final String query = "query {dish(id: " + dbDishes.get(0).getId() + ") {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, null);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var dish = (Map<String, Object>) data.get("dish");

        assertNull(mapResult.get("errors"));
        assertEquals("Dish 1", dish.get("name"));
    }
}