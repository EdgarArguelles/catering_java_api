package com.catering.integration_test;

import com.catering.models.Person;
import com.catering.models.Role;
import com.catering.repositories.*;
import com.catering.security.pojos.LoggedUser;
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

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("unchecked")
public class SecurityIntegrationTest {

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

    private List<Person> dbPeople;

    private List<Role> dbRoles;

    private IntegrationTest integrationTest;

    private final String TOKEN = "token";

    private final String VIEW_ROLES_TOKEN = "view";

    private final String CHANGE_ROLE_TOKEN = "change";

    @Before
    public void setup() throws Exception {
        integrationTest = new IntegrationTest(mvc, mapper, tokenService);
        IntegrationTest.cleanAllData(courseRepository, menuRepository, quotationRepository, personRepository, roleRepository, dishRepository, courseTypeRepository, categoryRepository);

        dbRoles = List.of(
                new Role("R1", "D1", null),
                new Role("R2", "D2", null)
        );
        roleRepository.saveAll(dbRoles);

        dbPeople = List.of(
                new Person("N1", "LN1", LocalDate.now(), 1, "A", null, Collections.emptySet()),
                new Person("N2", "LN2", LocalDate.now(), 2, "B", null, new HashSet<>(dbRoles))
        );
        personRepository.saveAll(dbPeople);

        given(tokenService.getLoggedUser(VIEW_ROLES_TOKEN)).willReturn(
                new LoggedUser("123", null, null, null, Set.of("VIEW_ROLES")));
        given(tokenService.getLoggedUser(CHANGE_ROLE_TOKEN)).willReturn(
                new LoggedUser(dbPeople.get(1).getId(), null, null, null, Set.of("VIEW_ROLES")));
        given(tokenService.createToken(any(LoggedUser.class))).willReturn(TOKEN);
        given(tokenService.getLoggedUser(TOKEN)).willReturn(new LoggedUser("mockID", "mockRole"));
    }

    /**
     * Should return an error response
     */
    @Test
    public void changeRoleNotRoleId() throws Exception {
        final String query = "query {changeRole {id token}}";
        final Map mapResult = integrationTest.performGraphQL(query, VIEW_ROLES_TOKEN);
        final List<Map<String, String>> errors = (List) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("Missing field argument roleId"));
    }

    /**
     * Should return a 400 error response
     */
    @Test
    public void changeRoleInvalidUser() throws Exception {
        final String query = "query {changeRole(roleId: \"invalid\") {id token}}";
        final Map mapResult = integrationTest.performGraphQL(query, VIEW_ROLES_TOKEN);
        final Map data = (Map) mapResult.get("data");
        final List<Map> errors = (List) mapResult.get("errors");
        final Map extensions = (Map) errors.get(0).get("extensions");
        final Map error = (Map) extensions.get("error");

        assertNull(data.get("changeRole"));
        assertEquals(400, extensions.get("errorCode"));
        assertEquals("User doesn't have personal information associated.", error.get("message"));
    }

    /**
     * Should return a 400 error response
     */
    @Test
    public void changeRoleInvalidRole() throws Exception {
        final String query = "query {changeRole(roleId: \"invalid\") {id token}}";
        final Map mapResult = integrationTest.performGraphQL(query, CHANGE_ROLE_TOKEN);
        final Map data = (Map) mapResult.get("data");
        final List<Map> errors = (List) mapResult.get("errors");
        final Map extensions = (Map) errors.get(0).get("extensions");
        final Map error = (Map) extensions.get("error");

        assertNull(data.get("changeRole"));
        assertEquals(400, extensions.get("errorCode"));
        assertEquals("User doesn't have the requested Role.", error.get("message"));
    }

    /**
     * Should return a success response
     */
    @Test
    public void changeRole() throws Exception {
        final String query = "query {changeRole(roleId: \"" + dbRoles.get(1).getId() + "\") {id token}}";
        final Map mapResult = integrationTest.performGraphQL(query, CHANGE_ROLE_TOKEN);
        final Map<String, Map> data = (Map) mapResult.get("data");
        final Map changeRole = data.get("changeRole");

        assertNull(mapResult.get("errors"));
        assertEquals("mockID", changeRole.get("id"));
        assertEquals(TOKEN, changeRole.get("token"));
    }

    /**
     * Should return a success response
     */
    @Test
    public void ping() throws Exception {
        final String query = "query {ping {id token}}";
        final Map mapResult = integrationTest.performGraphQL(query, VIEW_ROLES_TOKEN);
        final Map<String, Map> data = (Map) mapResult.get("data");
        final Map ping = data.get("ping");

        assertNull(mapResult.get("errors"));
        assertEquals("123", ping.get("id"));
        assertEquals(TOKEN, ping.get("token"));
    }

    /**
     * Should return an error response
     */
    @Test
    public void getAccessCodeNotSocial() throws Exception {
        final String query = "query {getAccessCode}";
        final Map mapResult = integrationTest.performGraphQL(query, TOKEN);
        final List<Map<String, String>> errors = (List) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("Missing field argument social"));
    }

    /**
     * Should return an error response
     */
    @Test
    public void getAccessCodeInvalidSocial() throws Exception {
        final String query = "query {getAccessCode(social: \"invalid\")}";
        final Map mapResult = integrationTest.performGraphQL(query, TOKEN);
        final List<Map<String, String>> errors = (List) mapResult.get("errors");

        assertNull(mapResult.get("data"));
        assertTrue(errors.get(0).get("message").contains("is not a valid 'SOCIAL_MEDIA'"));
    }

    /**
     * Should return null when social is Google
     */
    @Test
    public void getAccessCodeGoogle() throws Exception {
        final String query = "query {getAccessCode(social: GOOGLE)}";
        final Map mapResult = integrationTest.performGraphQL(query, TOKEN);
        final Map<String, String> data = (Map) mapResult.get("data");
        final String getAccessCode = data.get("getAccessCode");

        assertNull(mapResult.get("errors"));
        assertNull(getAccessCode);
    }

    /**
     * Should not return null when social is Facebook
     */
    @Test
    public void getAccessCodeFacebook() throws Exception {
        final String query = "query {getAccessCode(social: FACEBOOK)}";
        final Map mapResult = integrationTest.performGraphQL(query, TOKEN);
        final Map<String, String> data = (Map) mapResult.get("data");
        final String getAccessCode = data.get("getAccessCode");

        assertNull(mapResult.get("errors"));
        assertNotNull(getAccessCode);
    }
}