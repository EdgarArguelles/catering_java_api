package com.catering.integration_test;

import com.catering.models.Version;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("unchecked")
public class VersionIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private VersionRepository versionRepository;

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

    private List<Version> dbVersions;

    private IntegrationTest integrationTest;

    @BeforeEach
    public void setup() throws Exception {
        integrationTest = new IntegrationTest(mvc, mapper, tokenService);
        IntegrationTest.cleanAllData(courseRepository, menuRepository, quotationRepository, personRepository,
                roleRepository, dishRepository, courseTypeRepository, categoryRepository);

        dbVersions = List.of(new Version(1L), new Version(2L));
        versionRepository.deleteAll();
        versionRepository.saveAll(dbVersions);
    }

    /**
     * Should return a success response
     */
    @Test
    public void version() throws Exception {
        final String query = "query {version {version}}";
        final Map<String, Object> mapResult = integrationTest.performGraphQL(query, null);
        final var data = (Map<String, Object>) mapResult.get("data");
        final var version = (Map<String, Object>) data.get("version");

        assertNull(mapResult.get("errors"));
        assertEquals(1, version.get("version"));
    }
}