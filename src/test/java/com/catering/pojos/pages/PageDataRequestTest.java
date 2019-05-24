package com.catering.pojos.pages;

import com.catering.integration_test.IntegrationTest;
import com.catering.pojos.responses.error.nesteds.NestedError;
import com.catering.pojos.responses.error.nesteds.ValidationNestedError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PageDataRequestTest {

    @Autowired
    private ObjectMapper mapper;

    /**
     * Should create default constructor
     */
    @Test
    public void constructorDefault() {
        final PageDataRequest pageDataRequest = new PageDataRequest();

        assertNull(pageDataRequest.getPage());
        assertNull(pageDataRequest.getSize());
        assertNull(pageDataRequest.getDirection());
        assertNull(pageDataRequest.getSort());
    }

    /**
     * Should create complete constructor
     */
    @Test
    public void constructorComplete() {
        final Integer PAGE = 1;
        final Integer SIZE = 2;
        final PageDataRequest.SORT_DIRECTION DIRECTION = PageDataRequest.SORT_DIRECTION.ASC;
        final List<String> SORT = List.of("S1", "S2");
        final PageDataRequest pageDataRequest = new PageDataRequest(PAGE, SIZE, DIRECTION, SORT);

        assertSame(PAGE, pageDataRequest.getPage());
        assertSame(SIZE, pageDataRequest.getSize());
        assertSame(DIRECTION, pageDataRequest.getDirection());
        assertSame(SORT, pageDataRequest.getSort());
    }

    /**
     * Should serialize and deserialize
     */
    @Test
    public void serialize() throws IOException {
        final Integer PAGE = 1;
        final Integer SIZE = 2;
        final PageDataRequest.SORT_DIRECTION DIRECTION = PageDataRequest.SORT_DIRECTION.ASC;
        final List<String> SORT = List.of("S1", "S2");
        final PageDataRequest pageDataRequestExpected = new PageDataRequest(PAGE, SIZE, DIRECTION, SORT);

        final String json = mapper.writeValueAsString(pageDataRequestExpected);
        final PageDataRequest pageDataRequestResult = mapper.readValue(json, PageDataRequest.class);

        assertNotSame(pageDataRequestExpected, pageDataRequestResult);
        assertEquals(pageDataRequestExpected, pageDataRequestResult);
    }

    /**
     * Should ignore null value on json
     */
    @Test
    public void JsonNotIncludeNull() throws JsonProcessingException {
        final PageDataRequest pageDataRequest = new PageDataRequest(1, 2, null, null);
        final PageDataRequest pageDataRequestFull = new PageDataRequest(1, 2, PageDataRequest.SORT_DIRECTION.ASC, List.of("A"));

        final String json = mapper.writeValueAsString(pageDataRequest);
        final String jsonFull = mapper.writeValueAsString(pageDataRequestFull);

        assertFalse(json.contains("direction"));
        assertFalse(json.contains("sort"));
        assertTrue(jsonFull.contains("direction"));
        assertTrue(jsonFull.contains("sort"));
    }

    /**
     * Should equals instances
     */
    @Test
    public void equalsInstance() {
        final PageDataRequest pageDataRequest = new PageDataRequest(1, 2, PageDataRequest.SORT_DIRECTION.ASC, null);

        assertTrue(pageDataRequest.equals(pageDataRequest));
        assertFalse(pageDataRequest.equals(null));
        assertFalse(pageDataRequest.equals(new String()));
    }

    /**
     * Should fail equals due page
     */
    @Test
    public void noEqualsPage() {
        final PageDataRequest pageDataRequest1 = new PageDataRequest(1, 2, PageDataRequest.SORT_DIRECTION.ASC, List.of("A", "B"));
        final PageDataRequest pageDataRequest2 = new PageDataRequest(11, 2, PageDataRequest.SORT_DIRECTION.ASC, List.of("A", "B"));
        final PageDataRequest pageDataRequestNull = new PageDataRequest(null, 2, PageDataRequest.SORT_DIRECTION.ASC, List.of("A", "B"));

        assertNotEquals(pageDataRequest1, pageDataRequest2);
        assertNotEquals(pageDataRequest1, pageDataRequestNull);
        assertNotEquals(pageDataRequestNull, pageDataRequest1);
    }

    /**
     * Should fail equals due size
     */
    @Test
    public void noEqualsSize() {
        final PageDataRequest pageDataRequest1 = new PageDataRequest(1, 2, PageDataRequest.SORT_DIRECTION.ASC, List.of("A", "B"));
        final PageDataRequest pageDataRequest2 = new PageDataRequest(1, 21, PageDataRequest.SORT_DIRECTION.ASC, List.of("A", "B"));
        final PageDataRequest pageDataRequestNull = new PageDataRequest(1, null, PageDataRequest.SORT_DIRECTION.ASC, List.of("A", "B"));

        assertNotEquals(pageDataRequest1, pageDataRequest2);
        assertNotEquals(pageDataRequest1, pageDataRequestNull);
        assertNotEquals(pageDataRequestNull, pageDataRequest1);
    }

    /**
     * Should fail equals due direction
     */
    @Test
    public void noEqualsDirection() {
        final PageDataRequest pageDataRequest1 = new PageDataRequest(1, 2, PageDataRequest.SORT_DIRECTION.ASC, List.of("A", "B"));
        final PageDataRequest pageDataRequest2 = new PageDataRequest(1, 2, PageDataRequest.SORT_DIRECTION.DESC, List.of("A", "B"));
        final PageDataRequest pageDataRequestNull = new PageDataRequest(1, 2, null, List.of("A", "B"));

        assertNotEquals(pageDataRequest1, pageDataRequest2);
        assertNotEquals(pageDataRequest1, pageDataRequestNull);
        assertNotEquals(pageDataRequestNull, pageDataRequest1);
    }

    /**
     * Should fail equals due sort
     */
    @Test
    public void noEqualsSort() {
        final PageDataRequest pageDataRequest1 = new PageDataRequest(1, 2, PageDataRequest.SORT_DIRECTION.ASC, List.of("A", "B"));
        final PageDataRequest pageDataRequest2 = new PageDataRequest(1, 2, PageDataRequest.SORT_DIRECTION.ASC, List.of("A"));
        final PageDataRequest pageDataRequestNull = new PageDataRequest(1, 2, PageDataRequest.SORT_DIRECTION.ASC, null);

        assertNotEquals(pageDataRequest1, pageDataRequest2);
        assertNotEquals(pageDataRequest1, pageDataRequestNull);
        assertNotEquals(pageDataRequestNull, pageDataRequest1);
    }

    /**
     * Should be equals
     */
    @Test
    public void testEquals() {
        final PageDataRequest pageDataRequest1 = new PageDataRequest(1, 2, PageDataRequest.SORT_DIRECTION.ASC, List.of("A", "B"));
        final PageDataRequest pageDataRequest2 = new PageDataRequest(1, 2, PageDataRequest.SORT_DIRECTION.ASC, List.of("A", "B"));
        final PageDataRequest pageDataRequestNull1 = new PageDataRequest();
        final PageDataRequest pageDataRequestNull2 = new PageDataRequest();

        assertNotSame(pageDataRequest1, pageDataRequest2);
        assertEquals(pageDataRequest1, pageDataRequest2);
        assertNotSame(pageDataRequestNull1, pageDataRequestNull2);
        assertEquals(pageDataRequestNull1, pageDataRequestNull2);
    }

    /**
     * Should get 2 errors when parameters null
     */
    @Test
    public void validateWhenNull() {
        final PageDataRequest p = new PageDataRequest();
        final List<NestedError> nestedErrorsExpected = List.of(
                new ValidationNestedError("page", "must not be null"),
                new ValidationNestedError("size", "must not be null")
        );
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(p);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 1 error when parameters has Max errors
     */
    @Test
    public void validateWhenMax() {
        final PageDataRequest p = new PageDataRequest(0, 6, PageDataRequest.SORT_DIRECTION.ASC, null);
        final List<NestedError> nestedErrorsExpected = List.of(
                new ValidationNestedError("size", "must be less than or equal to 5")
        );
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(p);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 2 errors when parameters has Min errors
     */
    @Test
    public void validateWhenMin() {
        final PageDataRequest p = new PageDataRequest(-1, 0, PageDataRequest.SORT_DIRECTION.ASC, null);
        final List<NestedError> nestedErrorsExpected = List.of(
                new ValidationNestedError("page", "must be greater than or equal to 0"),
                new ValidationNestedError("size", "must be greater than or equal to 1")
        );
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(p);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 0 error when correct
     */
    @Test
    public void validateWhenOK() {
        final PageDataRequest p = new PageDataRequest(0, 1, PageDataRequest.SORT_DIRECTION.ASC, null);
        final List<NestedError> nestedErrorsExpected = Collections.emptyList();
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(p);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }
}