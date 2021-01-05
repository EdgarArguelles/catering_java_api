package com.catering.models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTypeTest {

    /**
     * Should create default constructor
     */
    @Test
    public void constructorDefault() {
        final CourseType courseType = new CourseType();

        assertNull(courseType.getId());
        assertNull(courseType.getName());
        assertNull(courseType.getPicture());
        assertNull(courseType.getPosition());
        assertNull(courseType.getStatus());
        assertNull(courseType.getCourses());
        assertNull(courseType.getAllowedDishes());
    }

    /**
     * Should create Id constructor
     */
    @Test
    public void constructorId() {
        final String ID = "ID";
        final CourseType courseType = new CourseType(ID);

        assertSame(ID, courseType.getId());
        assertNull(courseType.getName());
        assertNull(courseType.getPicture());
        assertNull(courseType.getPosition());
        assertNull(courseType.getStatus());
        assertNull(courseType.getCourses());
        assertNull(courseType.getAllowedDishes());
    }

    /**
     * Should create complete constructor
     */
    @Test
    public void constructorComplete() {
        final String NAME = "name";
        final String PICTURE = "pic";
        final Integer POSITION = 5;
        final Integer STATUS = 1;
        final CourseType courseType = new CourseType(NAME, PICTURE, POSITION, STATUS);

        assertNull(courseType.getId());
        assertSame(NAME, courseType.getName());
        assertSame(PICTURE, courseType.getPicture());
        assertSame(POSITION, courseType.getPosition());
        assertSame(STATUS, courseType.getStatus());
        assertNull(courseType.getCourses());
        assertNull(courseType.getAllowedDishes());
    }

    /**
     * Should set and get id
     */
    @Test
    public void setGetID() {
        final CourseType courseType = new CourseType();
        final String ID = "ID";
        courseType.setId(ID);

        assertSame(ID, courseType.getId());
    }

    /**
     * Should set and get name
     */
    @Test
    public void setGetName() {
        final CourseType courseType = new CourseType();
        final String NAME = "name";
        courseType.setName(NAME);

        assertSame(NAME, courseType.getName());
    }

    /**
     * Should set and get picture
     */
    @Test
    public void setGetPicture() {
        final CourseType courseType = new CourseType();
        final String PICTURE = "pic";
        courseType.setPicture(PICTURE);

        assertSame(PICTURE, courseType.getPicture());
    }

    /**
     * Should set and get position
     */
    @Test
    public void setGetPosition() {
        final CourseType courseType = new CourseType();
        final Integer POSITION = 5;
        courseType.setPosition(POSITION);

        assertSame(POSITION, courseType.getPosition());
    }

    /**
     * Should set and get status
     */
    @Test
    public void setGetStatus() {
        final CourseType courseType = new CourseType();
        final Integer STATUS = 1;
        courseType.setStatus(STATUS);

        assertSame(STATUS, courseType.getStatus());
    }

    /**
     * Should set and get courses
     */
    @Test
    public void setGetCourses() {
        final CourseType courseType = new CourseType();
        final List<Course> COURSES = List.of(new Course("C1"), new Course("C2"));
        courseType.setCourses(COURSES);

        assertSame(COURSES, courseType.getCourses());
    }

    /**
     * Should set and get allowedDishes
     */
    @Test
    public void setGetAllowedDishes() {
        final CourseType courseType = new CourseType();
        final List<Dish> ALLOWED_DISHES = List.of(new Dish("D1"), new Dish("D2"));
        courseType.setAllowedDishes(ALLOWED_DISHES);

        assertSame(ALLOWED_DISHES, courseType.getAllowedDishes());
    }

    /**
     * Should get toString
     */
    @Test
    public void toStringValid() {
        final String ID = "ID";
        final String NAME = "name";
        final CourseType courseType = new CourseType(ID);
        courseType.setName(NAME);

        assertEquals("CourseType(super=Model(id=" + ID + "), name=" + NAME + ")", courseType.toString());
    }

    /**
     * Should equals instances
     */
    @Test
    public void equalsInstance() {
        final CourseType courseType = new CourseType("ID");

        assertTrue(courseType.equals(courseType));
        assertFalse(courseType.equals(null));
        assertFalse(courseType.equals(new String()));
    }

    /**
     * Should fail equals due ID
     */
    @Test
    public void noEqualsID() {
        final CourseType courseType1 = new CourseType("N", "Pic", 5, 1);
        courseType1.setId("ID");
        courseType1.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType1.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseType2 = new CourseType("N", "Pic", 5, 1);
        courseType2.setId("ID2");
        courseType2.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType2.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseTypeNull = new CourseType("N", "Pic", 5, 1);
        courseTypeNull.setId(null);
        courseTypeNull.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseTypeNull.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));

        assertNotEquals(courseType1, courseType2);
        assertNotEquals(courseType1, courseTypeNull);
        assertNotEquals(courseTypeNull, courseType1);
    }

    /**
     * Should fail equals due name
     */
    @Test
    public void noEqualsName() {
        final CourseType courseType1 = new CourseType("N", "Pic", 5, 1);
        courseType1.setId("ID");
        courseType1.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType1.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseType2 = new CourseType("N2", "Pic", 5, 1);
        courseType2.setId("ID");
        courseType2.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType2.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseTypeNull = new CourseType(null, "Pic", 5, 1);
        courseTypeNull.setId("ID");
        courseTypeNull.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseTypeNull.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));

        assertNotEquals(courseType1, courseType2);
        assertNotEquals(courseType1, courseTypeNull);
        assertNotEquals(courseTypeNull, courseType1);
    }

    /**
     * Should fail equals due picture
     */
    @Test
    public void noEqualsPicture() {
        final CourseType courseType1 = new CourseType("N", "Pic", 5, 1);
        courseType1.setId("ID");
        courseType1.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType1.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseType2 = new CourseType("N", "Pic2", 5, 1);
        courseType2.setId("ID");
        courseType2.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType2.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseTypeNull = new CourseType("N", null, 5, 1);
        courseTypeNull.setId("ID");
        courseTypeNull.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseTypeNull.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));

        assertNotEquals(courseType1, courseType2);
        assertNotEquals(courseType1, courseTypeNull);
        assertNotEquals(courseTypeNull, courseType1);
    }

    /**
     * Should fail equals due position
     */
    @Test
    public void noEqualsPosition() {
        final CourseType courseType1 = new CourseType("N", "Pic", 5, 1);
        courseType1.setId("ID");
        courseType1.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType1.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseType2 = new CourseType("N", "Pic", 6, 1);
        courseType2.setId("ID");
        courseType2.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType2.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseTypeNull = new CourseType("N", "Pic", null, 1);
        courseTypeNull.setId("ID");
        courseTypeNull.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseTypeNull.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));

        assertNotEquals(courseType1, courseType2);
        assertNotEquals(courseType1, courseTypeNull);
        assertNotEquals(courseTypeNull, courseType1);
    }

    /**
     * Should fail equals due status
     */
    @Test
    public void noEqualsStatus() {
        final CourseType courseType1 = new CourseType("N", "Pic", 5, 1);
        courseType1.setId("ID");
        courseType1.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType1.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseType2 = new CourseType("N", "Pic", 5, 2);
        courseType2.setId("ID");
        courseType2.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType2.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseTypeNull = new CourseType("N", "Pic", 5, null);
        courseTypeNull.setId("ID");
        courseTypeNull.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseTypeNull.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));

        assertNotEquals(courseType1, courseType2);
        assertNotEquals(courseType1, courseTypeNull);
        assertNotEquals(courseTypeNull, courseType1);
    }

    /**
     * Should fail equals due courses
     */
    @Test
    public void noEqualsCourses() {
        final CourseType courseType1 = new CourseType("N", "Pic", 5, 1);
        courseType1.setId("ID");
        courseType1.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType1.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseType2 = new CourseType("N", "Pic", 5, 1);
        courseType2.setId("ID");
        courseType2.setCourses(List.of(new Course("C1")));
        courseType2.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseTypeNull = new CourseType("N", "Pic", 5, 1);
        courseTypeNull.setId("ID");
        courseTypeNull.setCourses(null);
        courseTypeNull.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));

        assertNotEquals(courseType1, courseType2);
        assertNotEquals(courseType1, courseTypeNull);
        assertNotEquals(courseTypeNull, courseType1);
    }

    /**
     * Should fail equals due allowedDishes
     */
    @Test
    public void noEqualsAllowedDishes() {
        final CourseType courseType1 = new CourseType("N", "Pic", 5, 1);
        courseType1.setId("ID");
        courseType1.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType1.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseType2 = new CourseType("N", "Pic", 5, 1);
        courseType2.setId("ID");
        courseType2.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType2.setAllowedDishes(List.of(new Dish("D1")));
        final CourseType courseTypeNull = new CourseType("N", "Pic", 5, 1);
        courseTypeNull.setId("ID");
        courseTypeNull.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseTypeNull.setAllowedDishes(null);

        assertNotEquals(courseType1, courseType2);
        assertNotEquals(courseType1, courseTypeNull);
        assertNotEquals(courseTypeNull, courseType1);
    }

    /**
     * Should be equals
     */
    @Test
    public void testEquals() {
        final CourseType courseType1 = new CourseType("N", "Pic", 5, 1);
        courseType1.setId("ID");
        courseType1.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType1.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseType2 = new CourseType("N", "Pic", 5, 1);
        courseType2.setId("ID");
        courseType2.setCourses(List.of(new Course("C1"), new Course("C2")));
        courseType2.setAllowedDishes(List.of(new Dish("D1"), new Dish("D2")));
        final CourseType courseTypeNull1 = new CourseType();
        final CourseType courseTypeNull2 = new CourseType();

        assertNotSame(courseType1, courseType2);
        assertEquals(courseType1, courseType2);
        assertNotSame(courseTypeNull1, courseTypeNull2);
        assertEquals(courseTypeNull1, courseTypeNull2);
    }
}