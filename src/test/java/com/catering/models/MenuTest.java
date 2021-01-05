package com.catering.models;

import com.catering.integration_test.IntegrationTest;
import com.catering.pojos.responses.error.nesteds.NestedError;
import com.catering.pojos.responses.error.nesteds.ValidationNestedError;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class MenuTest {

    /**
     * Should create default constructor
     */
    @Test
    public void constructorDefault() {
        final Menu menu = new Menu();

        assertNull(menu.getId());
        assertNull(menu.getName());
        assertNull(menu.getQuantity());
        assertNull(menu.getQuotation());
        assertNull(menu.getCourses());
    }

    /**
     * Should create Id constructor
     */
    @Test
    public void constructorId() {
        final String ID = "ID";
        final Menu menu = new Menu(ID);

        assertSame(ID, menu.getId());
        assertNull(menu.getName());
        assertNull(menu.getQuantity());
        assertNull(menu.getQuotation());
        assertNull(menu.getCourses());
    }

    /**
     * Should create complete constructor
     */
    @Test
    public void constructorComplete() {
        final String NAME = "name";
        final Integer QUANTITY = 10;
        final Quotation QUOTATION = new Quotation("Q1");
        final Menu menu = new Menu(NAME, QUANTITY, QUOTATION);

        assertNull(menu.getId());
        assertSame(NAME, menu.getName());
        assertSame(QUANTITY, menu.getQuantity());
        assertSame(QUOTATION, menu.getQuotation());
        assertNull(menu.getCourses());
    }

    /**
     * Should set and get id
     */
    @Test
    public void setGetID() {
        final Menu menu = new Menu();
        final String ID = "ID";
        menu.setId(ID);

        assertSame(ID, menu.getId());
    }

    /**
     * Should set and get name
     */
    @Test
    public void setGetName() {
        final Menu menu = new Menu();
        final String NAME = "name";
        menu.setName(NAME);

        assertSame(NAME, menu.getName());
    }

    /**
     * Should set and get quantity
     */
    @Test
    public void setGetQuantity() {
        final Menu menu = new Menu();
        final Integer QUANTITY = 10;
        menu.setQuantity(QUANTITY);

        assertSame(QUANTITY, menu.getQuantity());
    }

    /**
     * Should set and get quotation
     */
    @Test
    public void setGetQuotation() {
        final Menu menu = new Menu();
        final Quotation QUOTATION = new Quotation("Q1");
        menu.setQuotation(QUOTATION);

        assertSame(QUOTATION, menu.getQuotation());
    }

    /**
     * Should set and get courses
     */
    @Test
    public void setGetCourses() {
        final Menu menu = new Menu();
        final List<Course> COURSES = List.of(new Course("C1"), new Course("C2"));
        menu.setCourses(COURSES);

        assertSame(COURSES, menu.getCourses());
    }

    /**
     * Should get toString
     */
    @Test
    public void toStringValid() {
        final String ID = "ID";
        final String NAME = "name";
        final Menu menu = new Menu(ID);
        menu.setName(NAME);

        assertEquals("Menu(super=Model(id=" + ID + "), name=" + NAME + ")", menu.toString());
    }

    /**
     * Should equals instances
     */
    @Test
    public void equalsInstance() {
        final Menu menu = new Menu("ID");

        assertTrue(menu.equals(menu));
        assertFalse(menu.equals(null));
        assertFalse(menu.equals(new String()));
    }

    /**
     * Should fail equals due ID
     */
    @Test
    public void noEqualsID() {
        final Menu menu1 = new Menu("N", 10, new Quotation("Q"));
        menu1.setId("ID");
        menu1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Menu menu2 = new Menu("N", 10, new Quotation("Q"));
        menu2.setId("ID2");
        menu2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Menu menuNull = new Menu("N", 10, new Quotation("Q"));
        menuNull.setId(null);
        menuNull.setCourses(List.of(new Course("C1"), new Course("C2")));

        assertNotEquals(menu1, menu2);
        assertNotEquals(menu1, menuNull);
        assertNotEquals(menuNull, menu1);
    }

    /**
     * Should fail equals due name
     */
    @Test
    public void noEqualsName() {
        final Menu menu1 = new Menu("N", 10, new Quotation("Q"));
        menu1.setId("ID");
        menu1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Menu menu2 = new Menu("N2", 10, new Quotation("Q"));
        menu2.setId("ID");
        menu2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Menu menuNull = new Menu(null, 10, new Quotation("Q"));
        menuNull.setId("ID");
        menuNull.setCourses(List.of(new Course("C1"), new Course("C2")));

        assertNotEquals(menu1, menu2);
        assertNotEquals(menu1, menuNull);
        assertNotEquals(menuNull, menu1);
    }

    /**
     * Should fail equals due quantity
     */
    @Test
    public void noEqualsQuantity() {
        final Menu menu1 = new Menu("N", 10, new Quotation("Q"));
        menu1.setId("ID");
        menu1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Menu menu2 = new Menu("N", 11, new Quotation("Q"));
        menu2.setId("ID");
        menu2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Menu menuNull = new Menu("N", null, new Quotation("Q"));
        menuNull.setId("ID");
        menuNull.setCourses(List.of(new Course("C1"), new Course("C2")));

        assertNotEquals(menu1, menu2);
        assertNotEquals(menu1, menuNull);
        assertNotEquals(menuNull, menu1);
    }

    /**
     * Should fail equals due quotation
     */
    @Test
    public void noEqualsQuotation() {
        final Menu menu1 = new Menu("N", 10, new Quotation("Q"));
        menu1.setId("ID");
        menu1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Menu menu2 = new Menu("N", 10, new Quotation("Q2"));
        menu2.setId("ID");
        menu2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Menu menuNull = new Menu("N", 10, null);
        menuNull.setId("ID");
        menuNull.setCourses(List.of(new Course("C1"), new Course("C2")));

        assertNotEquals(menu1, menu2);
        assertNotEquals(menu1, menuNull);
        assertNotEquals(menuNull, menu1);
    }

    /**
     * Should fail equals due courses
     */
    @Test
    public void noEqualsCourses() {
        final Menu menu1 = new Menu("N", 10, new Quotation("Q"));
        menu1.setId("ID");
        menu1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Menu menu2 = new Menu("N", 10, new Quotation("Q"));
        menu2.setId("ID");
        menu2.setCourses(List.of(new Course("C1")));
        final Menu menuNull = new Menu("N", 10, new Quotation("Q"));
        menuNull.setId("ID");
        menuNull.setCourses(null);

        assertNotEquals(menu1, menu2);
        assertNotEquals(menu1, menuNull);
        assertNotEquals(menuNull, menu1);
    }

    /**
     * Should be equals
     */
    @Test
    public void testEquals() {
        final Menu menu1 = new Menu("N", 10, new Quotation("Q"));
        menu1.setId("ID");
        menu1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Menu menu2 = new Menu("N", 10, new Quotation("Q"));
        menu2.setId("ID");
        menu2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Menu menuNull1 = new Menu();
        final Menu menuNull2 = new Menu();

        assertNotSame(menu1, menu2);
        assertEquals(menu1, menu2);
        assertNotSame(menuNull1, menuNull2);
        assertEquals(menuNull1, menuNull2);
    }

    /**
     * Should get 3 errors when parameters null
     */
    @Test
    public void validateWhenNull() {
        final Menu m = new Menu();
        final List<NestedError> nestedErrorsExpected = List.of(
                new ValidationNestedError("courses", "must not be empty"),
                new ValidationNestedError("name", "must not be null"),
                new ValidationNestedError("quantity", "must not be null")
        );
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(m);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 2 errors when parameters empty
     */
    @Test
    public void validateWhenEmpty() {
        final Menu m = new Menu("", 1, null);
        m.setCourses(Collections.emptyList());
        final List<NestedError> nestedErrorsExpected = List.of(
                new ValidationNestedError("courses", "must not be empty"),
                new ValidationNestedError("name", "size must be between 1 and 255")
        );
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(m);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 1 error when parameters are bigger than max
     */
    @Test
    public void validateWhenMax() {
        final StringBuffer longText = new StringBuffer();
        IntStream.range(0, 256).forEach(i -> longText.append("a"));
        final Menu m = new Menu(longText.toString(), 1, null);
        m.setCourses(List.of(new Course(1, new CourseType(), null, Set.of(new Dish()))));
        final List<NestedError> nestedErrorsExpected = List.of(
                new ValidationNestedError("name", "size must be between 1 and 255")
        );
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(m);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 1 error when parameters are smaller than min
     */
    @Test
    public void validateWhenMin() {
        final Menu m = new Menu("a", 0, null);
        m.setCourses(List.of(new Course(1, new CourseType(), null, Set.of(new Dish()))));
        final List<NestedError> nestedErrorsExpected = List.of(
                new ValidationNestedError("quantity", "must be greater than or equal to 1")
        );
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(m);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 3 errors when Course is invalid
     */
    @Test
    public void validateWhenCourseIncorrect() {
        final Menu m = new Menu("a", 1, null);
        m.setCourses(List.of(new Course()));
        final List<NestedError> nestedErrorsExpected = List.of(
                new ValidationNestedError("courses[0].dishes", "must not be empty"),
                new ValidationNestedError("courses[0].position", "must not be null"),
                new ValidationNestedError("courses[0].type", "must not be null")
        );
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(m);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 0 error when correct
     */
    @Test
    public void validateWhenOK() {
        final Menu m = new Menu("A", 1, null);
        m.setCourses(List.of(new Course(1, new CourseType(), null, Set.of(new Dish()))));
        final List<NestedError> nestedErrorsExpected = Collections.emptyList();
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(m);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }
}