package com.catering.integration_test;

import com.catering.models.CourseType;
import com.catering.models.Dish;
import com.catering.repositories.*;
import com.catering.security.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
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

    @Before
    public void setup() throws Exception {
        integrationTest = new IntegrationTest(mvc, mapper, tokenService);
        IntegrationTest.cleanAllData(courseRepository, menuRepository, quotationRepository, personRepository, roleRepository, dishRepository, courseTypeRepository, categoryRepository);

        dbCourseTypes = List.of(
                new CourseType("N0", "P0", 0, 0),
                new CourseType("N1", "P1", 1, 1),
                new CourseType("N2", "P2", 2, 1)
        );
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
        final Map mapResult = integrationTest.performGraphQL(query, null);
        final List<Map<String, String>> errors = (List) mapResult.get("errors");

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
        final Map mapResult = integrationTest.performGraphQL(query, null);
        final Map<String, List> data = (Map) mapResult.get("data");
        final List<Map> activeCourseTypes = data.get("activeCourseTypes");
        final List<Map> activeDishes = (List) activeCourseTypes.get(0).get("activeDishes");

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
        final Map mapResult = integrationTest.performGraphQL(query, null);
        final List<Map<String, String>> errors = (List) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("Missing field argument id"));
    }

    /**
     * Should return a 404 error response
     */
    @Test
    public void courseTypeNotFound() throws Exception {
        final String query = "query {courseType(id: 123456) {id name}}";
        final Map mapResult = integrationTest.performGraphQL(query, null);
        final Map data = (Map) mapResult.get("data");
        final List<Map> errors = (List) mapResult.get("errors");
        final Map extensions = (Map) errors.get(0).get("extensions");
        final Map error = (Map) extensions.get("error");

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
        final Map mapResult = integrationTest.performGraphQL(query, null);
        final Map<String, Map> data = (Map) mapResult.get("data");
        final Map courseType = data.get("courseType");

        assertNull(mapResult.get("errors"));
        assertEquals("N0", courseType.get("name"));
    }
}