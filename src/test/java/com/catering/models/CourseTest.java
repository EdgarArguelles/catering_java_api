package com.catering.models;

import com.catering.integration_test.IntegrationTest;
import com.catering.pojos.responses.error.nesteds.NestedError;
import com.catering.pojos.responses.error.nesteds.ValidationNestedError;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    /**
     * Should create default constructor
     */
    @Test
    public void constructorDefault() {
        final Course course = new Course();

        assertNull(course.getId());
        assertNull(course.getPosition());
        assertNull(course.getType());
        assertNull(course.getMenu());
        assertNull(course.getDishes());
    }

    /**
     * Should create Id constructor
     */
    @Test
    public void constructorId() {
        final String ID = "ID";
        final Course course = new Course(ID);

        assertSame(ID, course.getId());
        assertNull(course.getPosition());
        assertNull(course.getType());
        assertNull(course.getMenu());
        assertNull(course.getDishes());
    }

    /**
     * Should create complete constructor
     */
    @Test
    public void constructorComplete() {
        final Integer POSITION = 6;
        final CourseType TYPE = new CourseType("CT1");
        final Menu MENU = new Menu("M1");
        final Set<Dish> DISHES = Set.of(new Dish("D1"), new Dish("D2"));
        final Course course = new Course(POSITION, TYPE, MENU, DISHES);

        assertNull(course.getId());
        assertSame(POSITION, course.getPosition());
        assertSame(TYPE, course.getType());
        assertSame(MENU, course.getMenu());
        assertSame(DISHES, course.getDishes());
    }

    /**
     * Should set and get id
     */
    @Test
    public void setGetID() {
        final Course course = new Course();
        final String ID = "ID";
        course.setId(ID);

        assertSame(ID, course.getId());
    }

    /**
     * Should set and get position
     */
    @Test
    public void setGetPosition() {
        final Course course = new Course();
        final Integer POSITION = 6;
        course.setPosition(POSITION);

        assertSame(POSITION, course.getPosition());
    }

    /**
     * Should set and get type
     */
    @Test
    public void setGetType() {
        final Course course = new Course();
        final CourseType TYPE = new CourseType("CT1");
        course.setType(TYPE);

        assertSame(TYPE, course.getType());
    }

    /**
     * Should set and get menu
     */
    @Test
    public void setGetMenu() {
        final Course course = new Course();
        final Menu MENU = new Menu("M1");
        course.setMenu(MENU);

        assertSame(MENU, course.getMenu());
    }

    /**
     * Should set and get dishes
     */
    @Test
    public void setGetDishes() {
        final Course course = new Course();
        final Set<Dish> DISHES = Set.of(new Dish("D1"), new Dish("D2"));
        course.setDishes(DISHES);

        assertSame(DISHES, course.getDishes());
    }

    /**
     * Should get toString
     */
    @Test
    public void toStringValid() {
        final String ID = "ID";
        final CourseType TYPE = new CourseType("CT1");
        final Course course = new Course(ID);
        course.setType(TYPE);

        assertEquals("Course(super=Model(id=" + ID + "), type=" + TYPE + ")", course.toString());
    }

    /**
     * Should equals instances
     */
    @Test
    public void equalsInstance() {
        final Course course = new Course("ID");

        assertTrue(course.equals(course));
        assertFalse(course.equals(null));
        assertFalse(course.equals(new Object()));
    }

    /**
     * Should fail equals due ID
     */
    @Test
    public void noEqualsID() {
        final Course course1 = new Course(5, new CourseType("CT1"), new Menu("M1"),
                Set.of(new Dish("D1"), new Dish("D2")));
        course1.setId("ID");
        final Course course2 = new Course(5, new CourseType("CT1"), new Menu("M1"),
                Set.of(new Dish("D1"), new Dish("D2")));
        course2.setId("ID2");
        final Course courseNull = new Course(5, new CourseType("CT1"), new Menu("M1"),
                Set.of(new Dish("D1"), new Dish("D2")));
        courseNull.setId(null);

        assertNotEquals(course1, course2);
        assertNotEquals(course1, courseNull);
        assertNotEquals(courseNull, course1);
    }

    /**
     * Should fail equals due position
     */
    @Test
    public void noEqualsPosition() {
        final Course course1 = new Course(5, new CourseType("CT1"), new Menu("M1"),
                Set.of(new Dish("D1"), new Dish("D2")));
        course1.setId("ID");
        final Course course2 = new Course(6, new CourseType("CT1"), new Menu("M1"),
                Set.of(new Dish("D1"), new Dish("D2")));
        course2.setId("ID");
        final Course courseNull = new Course(null, new CourseType("CT1"), new Menu("M1"),
                Set.of(new Dish("D1"), new Dish("D2")));
        courseNull.setId("ID");

        assertNotEquals(course1, course2);
        assertNotEquals(course1, courseNull);
        assertNotEquals(courseNull, course1);
    }

    /**
     * Should fail equals due type
     */
    @Test
    public void noEqualsType() {
        final Course course1 = new Course(5, new CourseType("CT1"), new Menu("M1"),
                Set.of(new Dish("D1"), new Dish("D2")));
        course1.setId("ID");
        final Course course2 = new Course(5, new CourseType("CT2"), new Menu("M1"),
                Set.of(new Dish("D1"), new Dish("D2")));
        course2.setId("ID");
        final Course courseNull = new Course(5, null, new Menu("M1"), Set.of(new Dish("D1"), new Dish("D2")));
        courseNull.setId("ID");

        assertNotEquals(course1, course2);
        assertNotEquals(course1, courseNull);
        assertNotEquals(courseNull, course1);
    }

    /**
     * Should fail equals due menu
     */
    @Test
    public void noEqualsMenu() {
        final Course course1 = new Course(5, new CourseType("CT1"), new Menu("M1"),
                Set.of(new Dish("D1"), new Dish("D2")));
        course1.setId("ID");
        final Course course2 = new Course(5, new CourseType("CT1"), new Menu("M2"),
                Set.of(new Dish("D1"), new Dish("D2")));
        course2.setId("ID");
        final Course courseNull = new Course(5, new CourseType("CT1"), null, Set.of(new Dish("D1"), new Dish("D2")));
        courseNull.setId("ID");

        assertNotEquals(course1, course2);
        assertNotEquals(course1, courseNull);
        assertNotEquals(courseNull, course1);
    }

    /**
     * Should fail equals due dishes
     */
    @Test
    public void noEqualsDishes() {
        final Course course1 = new Course(5, new CourseType("CT1"), new Menu("M1"),
                Set.of(new Dish("D1"), new Dish("D2")));
        course1.setId("ID");
        final Course course2 = new Course(5, new CourseType("CT1"), new Menu("M1"), Set.of(new Dish("D1")));
        course2.setId("ID");
        final Course courseNull = new Course(5, new CourseType("CT1"), new Menu("M1"), null);
        courseNull.setId("ID");

        assertNotEquals(course1, course2);
        assertNotEquals(course1, courseNull);
        assertNotEquals(courseNull, course1);
    }

    /**
     * Should be equals
     */
    @Test
    public void testEquals() {
        final Course course1 = new Course(5, new CourseType("CT1"), new Menu("M1"),
                Set.of(new Dish("D1"), new Dish("D2")));
        course1.setId("ID");
        final Course course2 = new Course(5, new CourseType("CT1"), new Menu("M1"),
                Set.of(new Dish("D1"), new Dish("D2")));
        course2.setId("ID");
        final Course courseNull1 = new Course();
        final Course courseNull2 = new Course();

        assertNotSame(course1, course2);
        assertEquals(course1, course2);
        assertNotSame(courseNull1, courseNull2);
        assertEquals(courseNull1, courseNull2);
    }

    /**
     * Should get 3 errors when parameters null
     */
    @Test
    public void validateWhenNull() {
        final Course c = new Course();
        final List<NestedError> nestedErrorsExpected = List.of(new ValidationNestedError("dishes", "must not be empty"),
                new ValidationNestedError("position", "must not be null"),
                new ValidationNestedError("type", "must not be null"));
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(c);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 1 error when parameters empty
     */
    @Test
    public void validateWhenEmpty() {
        final Course c = new Course(1, new CourseType(), null, Collections.emptySet());
        final List<NestedError> nestedErrorsExpected = List
                .of(new ValidationNestedError("dishes", "must not be empty"));
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(c);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 1 error when parameters are smaller than min
     */
    @Test
    public void validateWhenMin() {
        final Course c = new Course(0, new CourseType(), null, Set.of(new Dish()));
        final List<NestedError> nestedErrorsExpected = List
                .of(new ValidationNestedError("position", "must be greater than or equal to 1"));
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(c);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 0 error when correct
     */
    @Test
    public void validateWhenOK() {
        final Course c = new Course(1, new CourseType(), null, Set.of(new Dish()));
        final List<NestedError> nestedErrorsExpected = Collections.emptyList();
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(c);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }
}