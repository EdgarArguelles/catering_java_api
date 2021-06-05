package com.catering.integration_test.authentications;

import com.catering.integration_test.IntegrationTest;
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("unchecked")
public class CourseTypeAuthenticationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TokenService tokenService;

    private IntegrationTest integrationTest;

    private final String activeCourseTypesQuery = "query {activeCourseTypes {id name activeDishes{id}}}";

    private final String courseTypeQuery = "query {courseType(id: 5) {id name}}";

    @BeforeEach
    public void setup() throws Exception {
        integrationTest = new IntegrationTest(mvc, mapper, tokenService);
    }

    /**
     * Should not get error when not token
     */
    @Test
    public void activeCourseTypesNotToken() throws Exception {
        final Map<String, Object> mapResult = integrationTest.performGraphQL(activeCourseTypesQuery, null);
        final var data = (Map<String, Object>) mapResult.get("data");
        final Object activeCourseTypes = data.get("activeCourseTypes");

        assertNotNull(activeCourseTypes);
        assertNull(mapResult.get("errors"));
    }

    /**
     * Should return an UNAUTHORIZED error response when token invalid
     */
    @Test
    public void activeCourseTypesTokenInvalid() throws Exception {
        integrationTest.failGraphQLTokenInvalid(activeCourseTypesQuery);
    }

    /**
     * Should not get error when not permissions
     */
    @Test
    public void activeCourseTypesNotPermission() throws Exception {
        final Map<String, Object> mapResult = integrationTest.performGraphQL(activeCourseTypesQuery,
                IntegrationTest.NOT_PERMISSION_TOKEN);
        final var data = (Map<String, Object>) mapResult.get("data");
        final Object activeCourseTypes = data.get("activeCourseTypes");

        assertNotNull(activeCourseTypes);
        assertNull(mapResult.get("errors"));
    }

    /**
     * Should return a DontFoundException error response when not token
     */
    @Test
    public void courseTypeNotToken() throws Exception {
        final Map<String, Object> data = integrationTest.failGraphQL(courseTypeQuery, "Data don't found.", null);

        assertNull(data.get("courseType"));
    }

    /**
     * Should return an UNAUTHORIZED error response when token invalid
     */
    @Test
    public void courseTypeTokenInvalid() throws Exception {
        integrationTest.failGraphQLTokenInvalid(courseTypeQuery);
    }

    /**
     * Should return a DontFoundException error response when not permissions
     */
    @Test
    public void courseTypeNotPermission() throws Exception {
        final Map<String, Object> data = integrationTest.failGraphQL(courseTypeQuery, "Data don't found.",
                IntegrationTest.NOT_PERMISSION_TOKEN);

        assertNull(data.get("courseType"));
    }
}