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
public class QuotationAuthenticationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TokenService tokenService;

    private IntegrationTest integrationTest;

    private final String quotationPageQuery = "query {quotationPage(pageDataRequest: {}) {totalElements}}";

    private final String quotationQuery = "query {quotation(id: 5) {id name menus{id courses{id}}}}";

    private final String createQuotationQuery = "mutation {createQuotation(quotation: {}) {id name}}";

    private final String updateQuotationQuery = "mutation {updateQuotation(quotation: {}) {id name}}";

    private final String deleteQuotationQuery = "mutation {deleteQuotation(id: 5) {id name}}";

    @Before
    public void setup() throws Exception {
        integrationTest = new IntegrationTest(mvc, mapper, tokenService);
    }

    /**
     * Should return an UNAUTHORIZED error response when not token
     */
    @Test
    public void quotationPageNotToken() throws Exception {
        final Map data = integrationTest.failGraphQLAccessDenied(quotationPageQuery, null);

        assertNull(data.get("quotationPage"));
    }

    /**
     * Should return an UNAUTHORIZED error response when token invalid
     */
    @Test
    public void quotationPageTokenInvalid() throws Exception {
        integrationTest.failGraphQLTokenInvalid(quotationPageQuery);
    }

    /**
     * Should return an UNAUTHORIZED error response when not permissions
     */
    @Test
    public void quotationPageNotPermission() throws Exception {
        final Map data = integrationTest.failGraphQLAccessDenied(quotationPageQuery, IntegrationTest.NOT_PERMISSION_TOKEN);

        assertNull(data.get("quotationPage"));
    }

    /**
     * Should return an UNAUTHORIZED error response when not token
     */
    @Test
    public void quotationNotToken() throws Exception {
        final Map data = integrationTest.failGraphQLAccessDenied(quotationQuery, null);

        assertNull(data.get("quotation"));
    }

    /**
     * Should return an UNAUTHORIZED error response when token invalid
     */
    @Test
    public void quotationTokenInvalid() throws Exception {
        integrationTest.failGraphQLTokenInvalid(quotationQuery);
    }

    /**
     * Should return an UNAUTHORIZED error response when not permissions
     */
    @Test
    public void quotationNotPermission() throws Exception {
        final Map data = integrationTest.failGraphQLAccessDenied(quotationQuery, IntegrationTest.NOT_PERMISSION_TOKEN);

        assertNull(data.get("quotation"));
    }

    /**
     * Should return an UNAUTHORIZED error response when not token
     */
    @Test
    public void createQuotationNotToken() throws Exception {
        final Map data = integrationTest.failGraphQLAccessDenied(createQuotationQuery, null);

        assertNull(data.get("createQuotation"));
    }

    /**
     * Should return an UNAUTHORIZED error response when token invalid
     */
    @Test
    public void createQuotationTokenInvalid() throws Exception {
        integrationTest.failGraphQLTokenInvalid(createQuotationQuery);
    }

    /**
     * Should return an UNAUTHORIZED error response when not permissions
     */
    @Test
    public void createQuotationNotPermission() throws Exception {
        final Map data = integrationTest.failGraphQLAccessDenied(createQuotationQuery, IntegrationTest.NOT_PERMISSION_TOKEN);

        assertNull(data.get("createQuotation"));
    }

    /**
     * Should return an UNAUTHORIZED error response when not token
     */
    @Test
    public void updateQuotationNotToken() throws Exception {
        final Map data = integrationTest.failGraphQLAccessDenied(updateQuotationQuery, null);

        assertNull(data.get("updateQuotation"));
    }

    /**
     * Should return an UNAUTHORIZED error response when token invalid
     */
    @Test
    public void updateQuotationTokenInvalid() throws Exception {
        integrationTest.failGraphQLTokenInvalid(updateQuotationQuery);
    }

    /**
     * Should return an UNAUTHORIZED error response when not permissions
     */
    @Test
    public void updateQuotationNotPermission() throws Exception {
        final Map data = integrationTest.failGraphQLAccessDenied(updateQuotationQuery, IntegrationTest.NOT_PERMISSION_TOKEN);

        assertNull(data.get("updateQuotation"));
    }

    /**
     * Should return an UNAUTHORIZED error response when not token
     */
    @Test
    public void deleteQuotationNotToken() throws Exception {
        final Map data = integrationTest.failGraphQLAccessDenied(deleteQuotationQuery, null);

        assertNull(data.get("deleteQuotation"));
    }

    /**
     * Should return an UNAUTHORIZED error response when token invalid
     */
    @Test
    public void deleteQuotationTokenInvalid() throws Exception {
        integrationTest.failGraphQLTokenInvalid(deleteQuotationQuery);
    }

    /**
     * Should return an UNAUTHORIZED error response when not permissions
     */
    @Test
    public void deleteQuotationNotPermission() throws Exception {
        final Map data = integrationTest.failGraphQLAccessDenied(deleteQuotationQuery, IntegrationTest.NOT_PERMISSION_TOKEN);

        assertNull(data.get("deleteQuotation"));
    }
}