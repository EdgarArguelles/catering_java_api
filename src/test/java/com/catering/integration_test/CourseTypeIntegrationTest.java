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
public class CourseTypeIntegrationTest {

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

    private List<CourseType> dbCourseTypes;

    private IntegrationTest integrationTest;

    @BeforeEach
    public void setup() throws Exception {
        integrationTest = new IntegrationTest(mvc, mapper, tokenService);
        IntegrationTest.cleanAllData(courseRepository, menuRepository, quotationRepository, personRepository,
                roleRepository, dishRepository, courseTypeRepository, categoryRepository);

        dbCourseTypes = List.of(new CourseType("N0", "P0", 0, 0), new CourseType("N1", "P1", 1, 1),
                new CourseType("N2", "P2", 2, 1));
        courseTypeRepository.saveAll(dbCourseTypes);

        final List<Dish> dbDishes = List.of(new Dish("Dish 1", "D1", "P1", 5F, 1, dbCourseTypes.get(1), null));
        dishRepository.saveAll(dbDishes);
    }

    /**
     * Should return an error response
     */
    @Test
    public void validateGraphQLIgnore() throws Exception {
        final String query = "query {courseType(id: 1) {id name courses allowedDishes}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, null);
        final var errors = (List<Map<String, String>>) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("Field 'courses' in type 'CourseType' is undefined"));
        assertTrue(errors.get(1).get("message").contains("Field 'allowedDishes' in type 'CourseType' is undefined"));
    }

    /**
     * Should return a success response
     */
    @Test
    public void activeCourseTypes() throws Exception {
        final String query = "query {activeCourseTypes {id name activeDishes{id name}}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, null);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var activeCourseTypes = (List<Map<String, Object>>) data.get("activeCourseTypes");
        final var activeDishes = (List<Map<String, Object>>) activeCourseTypes.get(0).get("activeDishes");

        assertNull(mapResult.get("errors"));
        assertEquals(2, activeCourseTypes.size());
        assertEquals("N1", activeCourseTypes.get(0).get("name"));
        assertEquals("N2", activeCourseTypes.get(1).get("name"));
        assertEquals("Dish 1", activeDishes.get(0).get("name"));
    }

    /**
     * Should return an error response
     */
    @Test
    public void courseTypeNotID() throws Exception {
        final String query = "query {courseType {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, null);
        final var errors = (List<Map<String, String>>) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("Missing field argument id"));
    }

    /**
     * Should return a 404 error response
     */
    @Test
    public void courseTypeNotFound() throws Exception {
        final String query = "query {courseType(id: 123456) {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, null);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Object>) errors.get(0).get("extensions");
        final var error = (Map<String, Object>) extensions.get("error");

        assertNull(data.get("courseType"));
        assertEquals(404, extensions.get("errorCode"));
        assertEquals("Data don't found.", error.get("message"));
    }

    /**
     * Should return a success response
     */
    @Test
    public void courseType() throws Exception {
        final String query = "query {courseType(id: " + dbCourseTypes.get(0).getId() + ") {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, null);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var courseType = (Map<String, Object>) data.get("courseType");

        assertNull(mapResult.get("errors"));
        assertEquals("N0", courseType.get("name"));
    }
}