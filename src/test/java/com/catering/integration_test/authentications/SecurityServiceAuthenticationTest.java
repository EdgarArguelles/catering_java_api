package com.catering.integration_test.authentications;

import com.catering.integration_test.IntegrationTest;
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

import java.util.Map;

import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityServiceAuthenticationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TokenService tokenService;

    private IntegrationTest integrationTest;

    private final String changeRoleQuery = "query {changeRole(roleId: \"55\") {id fullName}}";

    private final String pingQuery = "query {ping {id fullName}}";

    @Before
    public void setup() throws Exception {
        integrationTest = new IntegrationTest(mvc, mapper, tokenService);
    }

    /**
     * Should return a ValidationException error response when not token
     */
    @Test
    public void changeRoleNotToken() throws Exception {
        final Map data = integrationTest.failGraphQL(changeRoleQuery, "There isn't any logged user.", null);

        assertNull(data.get("changeRole"));
    }

    /**
     * Should return an UNAUTHORIZED error response when token invalid
     */
    @Test
    public void changeRoleTokenInvalid() throws Exception {
        integrationTest.failGraphQLTokenInvalid(changeRoleQuery);
    }

    /**
     * Should return a ValidationException error response when not permissions
     */
    @Test
    public void changeRoleNotPermission() throws Exception {
        final Map data = integrationTest.failGraphQL(changeRoleQuery, "The given id must not be null", IntegrationTest.NOT_PERMISSION_TOKEN);

        assertNull(data.get("changeRole"));
    }

    /**
     * Should return ping null when not token
     */
    @Test
    public void pingNotToken() throws Exception {
        final Map mapResult = integrationTest.performGraphQL(pingQuery, null);
        final Map data = (Map) mapResult.get("data");

        assertNull(mapResult.get("errors"));
        assertNull(data.get("ping"));
    }

    /**
     * Should return an UNAUTHORIZED error response when token invalid
     */
    @Test
    public void pingTokenInvalid() throws Exception {
        integrationTest.failGraphQLTokenInvalid(pingQuery);
    }

    /**
     * Should return ping with null data when not permissions
     */
    @Test
    public void pingNotPermission() throws Exception {
        final Map mapResult = integrationTest.performGraphQL(pingQuery, IntegrationTest.NOT_PERMISSION_TOKEN);
        final Map data = (Map) mapResult.get("data");
        final Map ping = (Map) data.get("ping");

        assertNull(mapResult.get("errors"));
        assertNull(ping.get("id"));
        assertNull(ping.get("fullName"));
    }
}