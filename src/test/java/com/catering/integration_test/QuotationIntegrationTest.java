package com.catering.integration_test;

import com.catering.models.*;
import com.catering.repositories.*;
import com.catering.security.pojos.LoggedUser;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("unchecked")
public class QuotationIntegrationTest {

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

    private List<Quotation> dbQuotations;

    private List<Menu> dbMenus;

    private List<Course> dbCourses;

    private List<Person> dbPeople;

    private List<CourseType> dbCourseTypes;

    private List<Dish> dbDishes;

    private IntegrationTest integrationTest;

    private final String USER_TOKEN = "user";

    private final String CHEF_TOKEN = "chef";

    @BeforeEach
    public void setup() throws Exception {
        integrationTest = new IntegrationTest(mvc, mapper, tokenService);
        IntegrationTest.cleanAllData(courseRepository, menuRepository, quotationRepository, personRepository,
                roleRepository, dishRepository, courseTypeRepository, categoryRepository);

        dbPeople = List.of(new Person("N1", "LN1", LocalDate.now(), 1, "A", "aa@aa.com", Collections.emptySet()),
                new Person("N2", "LN2", LocalDate.now(), 1, "A", "aa@aa.com", Collections.emptySet()));
        personRepository.saveAll(dbPeople);

        dbCourseTypes = List.of(new CourseType("CT1", "P1", 1, 1));
        courseTypeRepository.saveAll(dbCourseTypes);

        dbQuotations = List.of(new Quotation("Q1", 111F, dbPeople.get(0)), new Quotation("Q2", 222F, dbPeople.get(0)),
                new Quotation("Q3", 333F, dbPeople.get(1)));
        quotationRepository.saveAll(dbQuotations);

        dbMenus = List.of(new Menu("M1", 10, dbQuotations.get(0)), new Menu("M2", 20, dbQuotations.get(0)),
                new Menu("M3", 30, dbQuotations.get(1)));
        dbMenus.get(0).setCourses(List.of(new Course(1, dbCourseTypes.get(0), dbMenus.get(0), Collections.emptySet())));
        dbMenus.get(1).setCourses(List.of(new Course(2, dbCourseTypes.get(0), dbMenus.get(0), Collections.emptySet())));
        dbMenus.get(2).setCourses(List.of(new Course(3, dbCourseTypes.get(0), dbMenus.get(1), Collections.emptySet())));
        menuRepository.saveAll(dbMenus);

        dbDishes = List.of(new Dish("Dish 1", "D1", "P1", 5F, 1, dbCourseTypes.get(0), null));
        dishRepository.saveAll(dbDishes);

        dbCourses = List.of(new Course(1, dbCourseTypes.get(0), dbMenus.get(0), Set.of(dbDishes.get(0))),
                new Course(2, dbCourseTypes.get(0), dbMenus.get(0), Set.of(dbDishes.get(0))),
                new Course(3, dbCourseTypes.get(0), dbMenus.get(1), Set.of(dbDishes.get(0))));
        courseRepository.saveAll(dbCourses);

        given(tokenService.getLoggedUser(USER_TOKEN))
                .willReturn(new LoggedUser(dbPeople.get(0).getId(), null, null, null, Set.of("MY_DATA")));
        given(tokenService.getLoggedUser(CHEF_TOKEN))
                .willReturn(new LoggedUser("123", null, null, null, Set.of("MY_DATA", "VIEW_ALL_DATA")));
    }

    /**
     * Should return an error response
     */
    @Test
    public void validateGraphQLIgnore() throws Exception {
        final String query = "query {quotation(id: 1) {id name person menus{quotation courses{menu}}}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var errors = (List<Map<String, String>>) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("Field 'person' in type 'Quotation' is undefined"));
        assertTrue(errors.get(1).get("message").contains("Field 'quotation' in type 'Menu' is undefined"));
        assertTrue(errors.get(2).get("message").contains("Field 'menu' in type 'Course' is undefined"));
    }

    /**
     * Should return an error response
     */
    // @Test
    public void validateComplexity() throws Exception {
        final String query = "query {quotationPage(pageDataRequest: {page: 0, size: 1}) {totalElements content{id menus{id}}}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertEquals("Requested operation exceeds the permitted complexity limit: 203 > 200",
                errors.get(0).get("message"));
    }

    /**
     * Should return an error response
     */
    @Test
    public void quotationPageNotData() throws Exception {
        final String query = "query {quotationPage {totalElements}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var errors = (List<Map<String, String>>) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("Missing field argument pageDataRequest"));
    }

    /**
     * Should return a 400 error response
     */
    @Test
    public void quotationPageInvalid() throws Exception {
        final String query = "query {quotationPage(pageDataRequest: {}) {totalElements}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Object>) errors.get(0).get("extensions");
        final var error = (Map<String, Object>) extensions.get("error");

        assertNull(data.get("quotationPage"));
        assertEquals(400, extensions.get("errorCode"));
        assertEquals("Some data aren't valid.", error.get("message"));
    }

    /**
     * Should return a success response
     */
    @Test
    public void quotationPage() throws Exception {
        final String query = "query {quotationPage(pageDataRequest: {page: 0, size: 1}) {totalElements content{id}}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var quotationPage = (Map<String, Object>) data.get("quotationPage");
        final var quotations = (List<Map<String, Object>>) quotationPage.get("content");

        assertNull(mapResult.get("errors"));
        assertEquals(2, quotationPage.get("totalElements"));
        assertEquals(1, quotations.size());
    }

    /**
     * Should return a success response
     */
    @Test
    public void quotationPageChefToken() throws Exception {
        final String query = "query {quotationPage(pageDataRequest: {page: 0, size: 1}) {totalElements content{id}}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, CHEF_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var quotationPage = (Map<String, Object>) data.get("quotationPage");
        final var quotations = (List<Map<String, Object>>) quotationPage.get("content");

        assertNull(mapResult.get("errors"));
        assertEquals(3, quotationPage.get("totalElements"));
        assertEquals(1, quotations.size());
    }

    /**
     * Should return an error response
     */
    @Test
    public void quotationNotID() throws Exception {
        final String query = "query {quotation {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var errors = (List<Map<String, String>>) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("Missing field argument id"));
    }

    /**
     * Should return a 404 error response
     */
    @Test
    public void quotationNotFound() throws Exception {
        final String query = "query {quotation(id: " + dbQuotations.get(2).getId() + ") {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Object>) errors.get(0).get("extensions");
        final var error = (Map<String, Object>) extensions.get("error");

        assertNull(data.get("quotation"));
        assertEquals(404, extensions.get("errorCode"));
        assertEquals("Data don't found.", error.get("message"));
    }

    /**
     * Should return a success response
     */
    @Test
    public void quotation() throws Exception {
        final String query = "query {quotation(id: " + dbQuotations.get(0).getId()
                + ") {id name menus{name courses{position}}}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var quotation = (Map<String, Object>) data.get("quotation");
        final var menus = (List<Map<String, Object>>) quotation.get("menus");
        final var courses = (List<Map<String, Object>>) menus.get(0).get("courses");

        assertNull(mapResult.get("errors"));
        assertEquals("Q1", quotation.get("name"));
        assertEquals(2, menus.size());
        assertEquals("M1", menus.get(0).get("name"));
        assertEquals("M2", menus.get(1).get("name"));
        assertEquals(2, courses.size());
        assertEquals(1, courses.get(0).get("position"));
        assertEquals(2, courses.get(1).get("position"));
    }

    /**
     * Should return a 404 error response
     */
    @Test
    public void quotationChefTokenNotFound() throws Exception {
        final String query = "query {quotation(id: 123) {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, CHEF_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Object>) errors.get(0).get("extensions");
        final var error = (Map<String, Object>) extensions.get("error");

        assertNull(data.get("quotation"));
        assertEquals(404, extensions.get("errorCode"));
        assertEquals("Data don't found.", error.get("message"));
    }

    /**
     * Should return a success response
     */
    @Test
    public void quotationChefToken() throws Exception {
        final String query = "query {quotation(id: " + dbQuotations.get(2).getId()
                + ") {id name menus{name courses{position}}}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, CHEF_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var quotation = (Map<String, Object>) data.get("quotation");
        final var menus = (List<Map<String, Object>>) quotation.get("menus");

        assertNull(mapResult.get("errors"));
        assertEquals("Q3", quotation.get("name"));
        assertEquals(0, menus.size());
    }

    /**
     * Should return an error response
     */
    @Test
    public void createQuotationNotData() throws Exception {
        final String query = "mutation {createQuotation {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var errors = (List<Map<String, String>>) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("Missing field argument quotation"));

        // not inserted in data base
        assertEquals(quotationRepository.count(), 3);
        assertEquals(menuRepository.count(), 3);
        assertEquals(courseRepository.count(), 3);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);
    }

    /**
     * Should return a 400 error response
     */
    @Test
    public void createQuotationInvalid() throws Exception {
        final String query = "mutation {createQuotation(quotation: {}) {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Object>) errors.get(0).get("extensions");
        final var error = (Map<String, Object>) extensions.get("error");

        assertNull(data.get("createQuotation"));
        assertEquals(400, extensions.get("errorCode"));
        assertEquals("Some data aren't valid.", error.get("message"));

        // not inserted in data base
        assertEquals(quotationRepository.count(), 3);
        assertEquals(menuRepository.count(), 3);
        assertEquals(courseRepository.count(), 3);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);
    }

    /**
     * Should return a 500 error response
     */
    @Test
    public void createQuotationInvalidTypeID() throws Exception {
        final String course1 = "{position: 1 type: {id: \"invalid\"} dishes: [{id: \"" + dbDishes.get(0).getId()
                + "\"}]}";
        final String menu1 = "{name: \"M4\" quantity: 10 courses: [" + course1 + "]}";

        final String query = "mutation {createQuotation(quotation: {name: \"Q4\" price: 444 menus: [" + menu1
                + "]}) {id name price}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Map<String, String>>) errors.get(0).get("extensions");

        assertNull(data.get("createQuotation"));
        assertEquals("INTERNAL_SERVER_ERROR", extensions.get("errorType"));
        assertEquals(500, extensions.get("errorCode"));
        assertTrue(extensions.get("error").get("devMessage").contains("could not execute statement"));

        // not inserted in data base
        assertEquals(quotationRepository.count(), 3);
        assertEquals(menuRepository.count(), 3);
        assertEquals(courseRepository.count(), 3);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);
    }

    /**
     * Should return a 500 error response
     */
    @Test
    public void createQuotationNotDishID() throws Exception {
        final String course1 = "{position: 1 type: {id: \"" + dbCourseTypes.get(0).getId()
                + "\"} dishes: [{name: \"123\"}]}";
        final String menu1 = "{name: \"M4\" quantity: 10 courses: [" + course1 + "]}";

        final String query = "mutation {createQuotation(quotation: {name: \"Q4\" price: 444 menus: [" + menu1
                + "]}) {id name price}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Map<String, String>>) errors.get(0).get("extensions");

        assertNull(data.get("createQuotation"));
        assertEquals("INTERNAL_SERVER_ERROR", extensions.get("errorType"));
        assertEquals(500, extensions.get("errorCode"));
        assertTrue(extensions.get("error").get("devMessage").contains("The given id must not be null!"));

        // not inserted in data base
        assertEquals(quotationRepository.count(), 3);
        assertEquals(menuRepository.count(), 3);
        assertEquals(courseRepository.count(), 3);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);
    }

    /**
     * Should return a 500 error response
     */
    @Test
    public void createQuotationNullMenus() throws Exception {
        final String query = "mutation {createQuotation(quotation: {name: \"Q4\"}) {id name price}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Object>) errors.get(0).get("extensions");
        final var error = (Map<String, Object>) extensions.get("error");

        assertNull(data.get("createQuotation"));
        assertEquals(500, extensions.get("errorCode"));
        assertEquals("An error has occurred.", error.get("message"));

        // not inserted in data base
        assertEquals(quotationRepository.count(), 3);
        assertEquals(menuRepository.count(), 3);
        assertEquals(courseRepository.count(), 3);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);
    }

    /**
     * Should return a success response
     */
    @Test
    public void createQuotationEmptyMenus() throws Exception {
        final String query = "mutation {createQuotation(quotation: {name: \"Q4\" price: 444 menus: []}) {id name price}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var quotation = (Map<String, Object>) data.get("createQuotation");

        assertNull(mapResult.get("errors"));
        assertEquals("Q4", quotation.get("name"));
        assertEquals(0D, quotation.get("price"));

        // inserted in data base
        assertEquals(quotationRepository.count(), 4);
        assertEquals(menuRepository.count(), 3);
        assertEquals(courseRepository.count(), 3);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);
    }

    /**
     * Should return a success response
     */
    @Test
    public void createQuotation() throws Exception {
        final String course1 = "{position: 1 type: {id: \"" + dbCourseTypes.get(0).getId() + "\"} dishes: [{id: \""
                + dbDishes.get(0).getId() + "\"}]}";
        final String course2 = "{position: 2 type: {id: \"" + dbCourseTypes.get(0).getId() + "\"} dishes: [{id: \""
                + dbDishes.get(0).getId() + "\"}]}";
        final String menu1 = "{name: \"M4\" quantity: 10 courses: [" + course1 + ", " + course2 + "]}";
        final String menu2 = "{name: \"M5\" quantity: 20 courses: [" + course1 + ", " + course2 + "]}";

        final String query = "mutation {createQuotation(quotation: {name: \"Q4\" price: 444 menus: [" + menu1 + ", "
                + menu2 + "]}) {id name price}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var quotation = (Map<String, Object>) data.get("createQuotation");

        assertNull(mapResult.get("errors"));
        assertEquals("Q4", quotation.get("name"));
        assertEquals(300D, quotation.get("price"));

        // inserted in data base
        assertEquals(quotationRepository.count(), 4);
        assertEquals(menuRepository.count(), 5);
        assertEquals(courseRepository.count(), 7);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);
    }

    /**
     * Should return an error response
     */
    @Test
    public void updateQuotationNotData() throws Exception {
        final String query = "mutation {updateQuotation {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var errors = (List<Map<String, String>>) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("Missing field argument quotation"));

        // not updated in data base
        validateQuotationNotEdited(dbQuotations.get(0).getId());
    }

    /**
     * Should return a 400 error response
     */
    @Test
    public void updateQuotationInvalid() throws Exception {
        final String query = "mutation {updateQuotation(quotation: {}) {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Object>) errors.get(0).get("extensions");
        final var error = (Map<String, Object>) extensions.get("error");

        assertNull(data.get("updateQuotation"));
        assertEquals(400, extensions.get("errorCode"));
        assertEquals("Some data aren't valid.", error.get("message"));

        // not updated in data base
        validateQuotationNotEdited(dbQuotations.get(0).getId());
    }

    /**
     * Should return a 404 error response
     */
    @Test
    public void updateQuotationNotFound() throws Exception {
        final String query = "mutation {updateQuotation(quotation: {id: \"" + dbQuotations.get(2).getId()
                + "\" name: \"Q11\"}) {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Object>) errors.get(0).get("extensions");
        final var error = (Map<String, Object>) extensions.get("error");

        assertNull(data.get("updateQuotation"));
        assertEquals(404, extensions.get("errorCode"));
        assertEquals("Data don't found.", error.get("message"));

        // not updated in data base
        validateQuotationNotEdited(dbQuotations.get(0).getId());
    }

    /**
     * Should return a 404 error response
     */
    @Test
    public void updateQuotationChefTokenNotFound() throws Exception {
        final String query = "mutation {updateQuotation(quotation: {id: \"123456\" name: \"Q11\"}) {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, CHEF_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Object>) errors.get(0).get("extensions");
        final var error = (Map<String, Object>) extensions.get("error");

        assertNull(data.get("updateQuotation"));
        assertEquals(404, extensions.get("errorCode"));
        assertEquals("Data don't found.", error.get("message"));

        // not updated in data base
        validateQuotationNotEdited(dbQuotations.get(0).getId());
    }

    /**
     * Should return a 500 error response
     */
    @Test
    public void updateQuotationInvalidTypeID() throws Exception {
        final String course0 = "{id: \"" + dbCourses.get(0).getId()
                + "\" position: 11 type: {id: \"invalid\"} dishes: [{id: \"" + dbDishes.get(0).getId() + "\"}]}";
        final String course1 = "{position: 1 type: {id: \"invalid\"} dishes: [{id: \"" + dbDishes.get(0).getId()
                + "\"}]}";
        final String course2 = "{position: 2 type: {id: \"" + dbCourseTypes.get(0).getId() + "\"} dishes: [{id: \""
                + dbDishes.get(0).getId() + "\"}]}";

        final String menu0 = "{id: \"" + dbMenus.get(0).getId() + "\" name: \"M11\" quantity: 110 courses: [" + course0
                + ", " + course1 + ", " + course2 + "]}";
        final String menu1 = "{name: \"M4\" quantity: 10 courses: [" + course1 + ", " + course2 + "]}";
        final String menu2 = "{name: \"M5\" quantity: 20 courses: [" + course1 + "]}";
        final String menus = "[" + menu0 + ", " + menu1 + ", " + menu2 + "]";
        final String query = "mutation {updateQuotation(quotation: {id: \"" + dbQuotations.get(0).getId()
                + "\" name: \"Q11\" price: 888 menus: " + menus + "}) {id name price}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Map<String, String>>) errors.get(0).get("extensions");

        assertNull(data.get("updateQuotation"));
        assertEquals("INTERNAL_SERVER_ERROR", extensions.get("errorType"));
        assertEquals(500, extensions.get("errorCode"));
        assertTrue(extensions.get("error").get("devMessage").contains("could not execute statement"));

        // not updated in data base
        validateQuotationNotEdited(dbQuotations.get(0).getId());
    }

    /**
     * Should return a 500 error response
     */
    @Test
    public void updateQuotationNotDishID() throws Exception {
        final String course0 = "{id: \"" + dbCourses.get(0).getId()
                + "\" position: 11 type: {id: \"invalid\"} dishes: [{id: \"" + dbDishes.get(0).getId() + "\"}]}";
        final String course1 = "{position: 1 type: {id: \"" + dbCourseTypes.get(0).getId()
                + "\"} dishes: [{name: \"name\"}]}";
        final String course2 = "{position: 2 type: {id: \"" + dbCourseTypes.get(0).getId() + "\"} dishes: [{id: \""
                + dbDishes.get(0).getId() + "\"}]}";

        final String menu0 = "{id: \"" + dbMenus.get(0).getId() + "\" name: \"M11\" quantity: 110 courses: [" + course0
                + ", " + course1 + ", " + course2 + "]}";
        final String menu1 = "{name: \"M4\" quantity: 10 courses: [" + course1 + ", " + course2 + "]}";
        final String menu2 = "{name: \"M5\" quantity: 20 courses: [" + course1 + "]}";
        final String menus = "[" + menu0 + ", " + menu1 + ", " + menu2 + "]";
        final String query = "mutation {updateQuotation(quotation: {id: \"" + dbQuotations.get(0).getId()
                + "\" name: \"Q11\" price: 888 menus: " + menus + "}) {id name price}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Map<String, String>>) errors.get(0).get("extensions");

        assertNull(data.get("updateQuotation"));
        assertEquals("INTERNAL_SERVER_ERROR", extensions.get("errorType"));
        assertEquals(500, extensions.get("errorCode"));
        assertTrue(extensions.get("error").get("devMessage").contains("The given id must not be null!"));

        // not updated in data base
        validateQuotationNotEdited(dbQuotations.get(0).getId());
    }

    /**
     * Should return a 500 error response
     */
    @Test
    public void updateQuotationNullMenus() throws Exception {
        final String query = "mutation {updateQuotation(quotation: {id: \"" + dbQuotations.get(0).getId()
                + "\" name: \"Q11\" price: 888}) {id name price}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Object>) errors.get(0).get("extensions");
        final var error = (Map<String, Object>) extensions.get("error");

        assertNull(data.get("updateQuotation"));
        assertEquals(500, extensions.get("errorCode"));
        assertEquals("An error has occurred.", error.get("message"));

        // not updated in data base
        validateQuotationNotEdited(dbQuotations.get(0).getId());
    }

    /**
     * Should return a success response
     */
    @Test
    public void updateQuotationEmptyMenus() throws Exception {
        final String query = "mutation {updateQuotation(quotation: {id: \"" + dbQuotations.get(0).getId()
                + "\" name: \"Q11\" price: 888 menus: []}) {id name price}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var quotation = (Map<String, Object>) data.get("updateQuotation");

        assertNull(mapResult.get("errors"));
        assertEquals(dbQuotations.get(0).getId(), quotation.get("id"));
        assertEquals("Q11", quotation.get("name"));
        assertEquals(0D, quotation.get("price"));

        // updated in data base
        assertEquals(quotationRepository.count(), 3);
        assertEquals(menuRepository.count(), 1);
        assertEquals(courseRepository.count(), 0);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);
    }

    /**
     * Should return a success response
     */
    @Test
    public void updateQuotation() throws Exception {
        final String course0 = "{id: \"" + dbCourses.get(0).getId()
                + "\" position: 11 type: {id: \"invalid\"} dishes: [{id: \"" + dbDishes.get(0).getId() + "\"}]}";
        final String course1 = "{position: 1 type: {id: \"" + dbCourseTypes.get(0).getId() + "\"} dishes: [{id: \""
                + dbDishes.get(0).getId() + "\"}]}";
        final String course2 = "{position: 2 type: {id: \"" + dbCourseTypes.get(0).getId() + "\"} dishes: [{id: \""
                + dbDishes.get(0).getId() + "\"}]}";

        final String menu0 = "{id: \"" + dbMenus.get(0).getId() + "\" name: \"M11\" quantity: 110 courses: [" + course0
                + ", " + course1 + ", " + course2 + "]}";
        final String menu1 = "{name: \"M4\" quantity: 10 courses: [" + course1 + ", " + course2 + "]}";
        final String menu2 = "{name: \"M5\" quantity: 20 courses: [" + course1 + "]}";
        final String menus = "[" + menu0 + ", " + menu1 + ", " + menu2 + "]";
        final String query = "mutation {updateQuotation(quotation: {id: \"" + dbQuotations.get(0).getId()
                + "\" name: \"Q11\" price: 888 menus: " + menus + "}) {id name price}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var quotation = (Map<String, Object>) data.get("updateQuotation");

        assertNull(mapResult.get("errors"));
        assertEquals(dbQuotations.get(0).getId(), quotation.get("id"));
        assertEquals("Q11", quotation.get("name"));
        assertEquals(1850D, quotation.get("price"));

        // updated in data base
        validateQuotationEdited((String) quotation.get("id"));
    }

    /**
     * Should return an error response
     */
    @Test
    public void deleteQuotationNotID() throws Exception {
        final String query = "mutation {deleteQuotation {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var errors = (List<Map<String, String>>) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("Missing field argument id"));

        // not deleted in data base
        assertEquals(quotationRepository.count(), 3);
        assertEquals(menuRepository.count(), 3);
        assertEquals(courseRepository.count(), 3);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);
    }

    /**
     * Should return a 404 error response
     */
    @Test
    public void deleteQuotationNotFound() throws Exception {
        final String query = "mutation {deleteQuotation(id: " + dbQuotations.get(2).getId() + ") {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Object>) errors.get(0).get("extensions");
        final var error = (Map<String, Object>) extensions.get("error");

        assertNull(data.get("deleteQuotation"));
        assertEquals(404, extensions.get("errorCode"));
        assertEquals("Data don't found.", error.get("message"));

        // not deleted in data base
        assertEquals(quotationRepository.count(), 3);
        assertEquals(menuRepository.count(), 3);
        assertEquals(courseRepository.count(), 3);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);
    }

    /**
     * Should return a 404 error response
     */
    @Test
    public void deleteQuotationChefTokenNotFound() throws Exception {
        final String query = "mutation {deleteQuotation(id: 123456) {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, CHEF_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var errors = (List<Map<String, Object>>) mapResult.get("errors");
        final var extensions = (Map<String, Object>) errors.get(0).get("extensions");
        final var error = (Map<String, Object>) extensions.get("error");

        assertNull(data.get("deleteQuotation"));
        assertEquals(404, extensions.get("errorCode"));
        assertEquals("Data don't found.", error.get("message"));

        // not deleted in data base
        assertEquals(quotationRepository.count(), 3);
        assertEquals(menuRepository.count(), 3);
        assertEquals(courseRepository.count(), 3);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);
    }

    /**
     * Should return a success response
     */
    @Test
    public void deleteQuotation() throws Exception {
        final String query = "mutation {deleteQuotation(id: " + dbQuotations.get(0).getId() + ") {id name}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, USER_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var quotation = (Map<String, Object>) data.get("deleteQuotation");

        assertNull(mapResult.get("errors"));
        assertEquals("Q1", quotation.get("name"));

        // deleted in data base
        assertEquals(quotationRepository.count(), 2);
        assertEquals(menuRepository.count(), 1);
        assertEquals(courseRepository.count(), 0);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);
    }

    private void validateQuotationEdited(String id) {
        final Quotation quotationResult = quotationRepository.findById(id).orElse(null);
        quotationResult.setPerson(null);
        quotationResult.setMenus(menuRepository.findByQuotation(quotationResult));
        quotationResult.getMenus().forEach(menu -> {
            menu.setQuotation(null);
            menu.setCourses(courseRepository.findByMenu(menu));
            menu.getCourses().forEach(course -> {
                course.setType(null);
                course.setDishes(null);
                course.setMenu(null);
            });
        });

        final Menu menuExpected1 = new Menu("M11", 110, null);
        final Menu menuExpected2 = new Menu("M4", 10, null);
        final Menu menuExpected3 = new Menu("M5", 20, null);

        final Course courseExpected1 = new Course(dbCourses.get(0).getId());
        courseExpected1.setPosition(11);
        final Course courseExpected2 = new Course(quotationResult.getMenus().get(0).getCourses().get(1).getId());
        courseExpected2.setPosition(1);
        final Course courseExpected3 = new Course(quotationResult.getMenus().get(0).getCourses().get(2).getId());
        courseExpected3.setPosition(2);
        menuExpected1.setId(dbMenus.get(0).getId());
        menuExpected1.setCourses(List.of(courseExpected1, courseExpected2, courseExpected3));

        final Course courseExpected4 = new Course(quotationResult.getMenus().get(1).getCourses().get(0).getId());
        courseExpected4.setPosition(1);
        final Course courseExpected5 = new Course(quotationResult.getMenus().get(1).getCourses().get(1).getId());
        courseExpected5.setPosition(2);
        menuExpected2.setId(quotationResult.getMenus().get(1).getId());
        menuExpected2.setCourses(List.of(courseExpected4, courseExpected5));

        final Course courseExpected6 = new Course(quotationResult.getMenus().get(2).getCourses().get(0).getId());
        courseExpected6.setPosition(1);
        menuExpected3.setId(quotationResult.getMenus().get(2).getId());
        menuExpected3.setCourses(List.of(courseExpected6));

        final Quotation quotationExpected = new Quotation("Q11", 1850F, null);
        quotationExpected.setId(id);
        quotationExpected.setMenus(List.of(menuExpected1, menuExpected2, menuExpected3));

        assertNotSame(quotationExpected, quotationResult);
        assertEquals(quotationExpected, quotationResult);

        assertEquals(quotationRepository.count(), 3);
        assertEquals(menuRepository.count(), 4);
        assertEquals(courseRepository.count(), 6);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);

        assertNull(menuRepository.findById(dbMenus.get(1).getId()).orElse(null));
        assertNull(courseRepository.findById(dbCourses.get(1).getId()).orElse(null));
        assertNull(courseRepository.findById(dbCourses.get(2).getId()).orElse(null));
    }

    private void validateQuotationNotEdited(String id) {
        final Quotation quotationResult = quotationRepository.findById(id).orElse(null);
        quotationResult.setPerson(null);
        quotationResult.setMenus(menuRepository.findByQuotation(quotationResult));
        quotationResult.getMenus().forEach(menu -> {
            menu.setQuotation(null);
            menu.setCourses(courseRepository.findByMenu(menu));
            menu.getCourses().forEach(course -> {
                course.setType(null);
                course.setDishes(null);
                course.setMenu(null);
            });
        });

        final Menu menuExpected1 = new Menu("M1", 10, null);
        final Menu menuExpected2 = new Menu("M2", 20, null);

        final Course courseExpected1 = new Course(dbCourses.get(0).getId());
        courseExpected1.setPosition(1);
        final Course courseExpected2 = new Course(dbCourses.get(1).getId());
        courseExpected2.setPosition(2);
        menuExpected1.setId(dbMenus.get(0).getId());
        menuExpected1.setCourses(List.of(courseExpected1, courseExpected2));

        final Course courseExpected3 = new Course(dbCourses.get(2).getId());
        courseExpected3.setPosition(3);
        menuExpected2.setId(dbMenus.get(1).getId());
        menuExpected2.setCourses(List.of(courseExpected3));

        final Quotation quotationExpected = new Quotation("Q1", 111F, null);
        quotationExpected.setId(id);
        quotationExpected.setMenus(List.of(menuExpected1, menuExpected2));

        assertNotSame(quotationExpected, quotationResult);
        assertEquals(quotationExpected, quotationResult);

        assertEquals(quotationRepository.count(), 3);
        assertEquals(menuRepository.count(), 3);
        assertEquals(courseRepository.count(), 3);
        assertEquals(courseTypeRepository.count(), 1);
        assertEquals(dishRepository.count(), 1);
    }
}