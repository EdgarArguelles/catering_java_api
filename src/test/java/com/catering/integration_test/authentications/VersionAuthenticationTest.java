package com.catering.integration_test.authentications;

import com.catering.integration_test.IntegrationTest;
import com.catering.repositories.VersionRepository;
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
public class VersionAuthenticationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private VersionRepository versionRepository;

    private IntegrationTest integrationTest;

    private final String versionQuery = "query {version {version}}";

    @Before
    public void setup() throws Exception {
        integrationTest = new IntegrationTest(mvc, mapper, tokenService);

        versionRepository.deleteAll();
    }

    /**
     * Should return a DontFoundException error response when not token
     */
    @Test
    public void versionNotToken() throws Exception {
        final Map data = integrationTest.failGraphQL(versionQuery, "Data don't found.", null);

        assertNull(data.get("version"));
    }

    /**
     * Should return an UNAUTHORIZED error response when token invalid
     */
    @Test
    public void versionTokenInvalid() throws Exception {
        integrationTest.failGraphQLTokenInvalid(versionQuery);
    }

    /**
     * Should return a DontFoundException error response when not permissions
     */
    @Test
    public void versionNotPermission() throws Exception {
        final Map data = integrationTest.failGraphQL(versionQuery, "Data don't found.", IntegrationTest.NOT_PERMISSION_TOKEN);

        assertNull(data.get("version"));
    }
}