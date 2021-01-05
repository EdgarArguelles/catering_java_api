package com.catering.models;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DishTest {

    /**
     * Should create default constructor
     */
    @Test
    public void constructorDefault() {
        final Dish dish = new Dish();

        assertNull(dish.getId());
        assertNull(dish.getName());
        assertNull(dish.getDescription());
        assertNull(dish.getPicture());
        assertNull(dish.getPrice());
        assertNull(dish.getStatus());
        assertNull(dish.getCourseType());
        assertNull(dish.getCategories());
        assertNull(dish.getCourses());
    }

    /**
     * Should create Id constructor
     */
    @Test
    public void constructorId() {
        final String ID = "ID";
        final Dish dish = new Dish(ID);

        assertSame(ID, dish.getId());
        assertNull(dish.getName());
        assertNull(dish.getDescription());
        assertNull(dish.getPicture());
        assertNull(dish.getPrice());
        assertNull(dish.getStatus());
        assertNull(dish.getCourseType());
        assertNull(dish.getCategories());
        assertNull(dish.getCourses());
    }

    /**
     * Should create complete constructor
     */
    @Test
    public void constructorComplete() {
        final String NAME = "name";
        final String DESCRIPTION = "desc";
        final String PICTURE = "pic";
        final Float PRICE = 50.5F;
        final Integer STATUS = 1;
        final CourseType COURSE_TYPE = new CourseType("CT1");
        final Set<Category> CATEGORIES = Set.of(new Category("C1"), new Category("C2"));
        final Dish dish = new Dish(NAME, DESCRIPTION, PICTURE, PRICE, STATUS, COURSE_TYPE, CATEGORIES);

        assertNull(dish.getId());
        assertSame(NAME, dish.getName());
        assertSame(DESCRIPTION, dish.getDescription());
        assertSame(PICTURE, dish.getPicture());
        assertSame(PRICE, dish.getPrice());
        assertSame(STATUS, dish.getStatus());
        assertSame(COURSE_TYPE, dish.getCourseType());
        assertSame(CATEGORIES, dish.getCategories());
        assertNull(dish.getCourses());
    }

    /**
     * Should set and get id
     */
    @Test
    public void setGetID() {
        final Dish dish = new Dish();
        final String ID = "ID";
        dish.setId(ID);

        assertSame(ID, dish.getId());
    }

    /**
     * Should set and get name
     */
    @Test
    public void setGetName() {
        final Dish dish = new Dish();
        final String NAME = "name";
        dish.setName(NAME);

        assertSame(NAME, dish.getName());
    }

    /**
     * Should set and get description
     */
    @Test
    public void setGetDescription() {
        final Dish dish = new Dish();
        final String DESCRIPTION = "desc";
        dish.setDescription(DESCRIPTION);

        assertSame(DESCRIPTION, dish.getDescription());
    }

    /**
     * Should set and get picture
     */
    @Test
    public void setGetPicture() {
        final Dish dish = new Dish();
        final String PICTURE = "pic";
        dish.setPicture(PICTURE);

        assertSame(PICTURE, dish.getPicture());
    }

    /**
     * Should set and get price
     */
    @Test
    public void setGetPrice() {
        final Dish dish = new Dish();
        final Float PRICE = 50.5F;
        dish.setPrice(PRICE);

        assertSame(PRICE, dish.getPrice());
    }

    /**
     * Should set and get status
     */
    @Test
    public void setGetStatus() {
        final Dish dish = new Dish();
        final Integer STATUS = 1;
        dish.setStatus(STATUS);

        assertSame(STATUS, dish.getStatus());
    }

    /**
     * Should set and get courseType
     */
    @Test
    public void setGetCourseType() {
        final Dish dish = new Dish();
        final CourseType COURSE_TYPE = new CourseType("CT1");
        dish.setCourseType(COURSE_TYPE);

        assertSame(COURSE_TYPE, dish.getCourseType());
    }

    /**
     * Should set and get categories
     */
    @Test
    public void setGetCategories() {
        final Dish dish = new Dish();
        final Set<Category> CATEGORIES = Set.of(new Category("C1"), new Category("C2"));
        dish.setCategories(CATEGORIES);

        assertSame(CATEGORIES, dish.getCategories());
    }

    /**
     * Should set and get courses
     */
    @Test
    public void setGetCourses() {
        final Dish dish = new Dish();
        final List<Course> COURSES = List.of(new Course("C1"), new Course("C2"));
        dish.setCourses(COURSES);

        assertSame(COURSES, dish.getCourses());
    }

    /**
     * Should get toString
     */
    @Test
    public void toStringValid() {
        final String ID = "ID";
        final String NAME = "name";
        final Dish dish = new Dish(ID);
        dish.setName(NAME);

        assertEquals("Dish(super=Model(id=" + ID + "), name=" + NAME + ")", dish.toString());
    }

    /**
     * Should equals instances
     */
    @Test
    public void equalsInstance() {
        final Dish dish = new Dish("ID");

        assertTrue(dish.equals(dish));
        assertFalse(dish.equals(null));
        assertFalse(dish.equals(new String()));
    }

    /**
     * Should fail equals due ID
     */
    @Test
    public void noEqualsID() {
        final Dish dish1 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish1.setId("ID");
        dish1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dish2 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish2.setId("ID2");
        dish2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dishNull = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dishNull.setId(null);
        dishNull.setCourses(List.of(new Course("C1"), new Course("C2")));

        assertNotEquals(dish1, dish2);
        assertNotEquals(dish1, dishNull);
        assertNotEquals(dishNull, dish1);
    }

    /**
     * Should fail equals due name
     */
    @Test
    public void noEqualsName() {
        final Dish dish1 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish1.setId("ID");
        dish1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dish2 = new Dish("N2", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish2.setId("ID");
        dish2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dishNull = new Dish(null, "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dishNull.setId("ID");
        dishNull.setCourses(List.of(new Course("C1"), new Course("C2")));

        assertNotEquals(dish1, dish2);
        assertNotEquals(dish1, dishNull);
        assertNotEquals(dishNull, dish1);
    }

    /**
     * Should fail equals due description
     */
    @Test
    public void noEqualsDescription() {
        final Dish dish1 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish1.setId("ID");
        dish1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dish2 = new Dish("N", "Dec2", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish2.setId("ID");
        dish2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dishNull = new Dish("N", null, "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dishNull.setId("ID");
        dishNull.setCourses(List.of(new Course("C1"), new Course("C2")));

        assertNotEquals(dish1, dish2);
        assertNotEquals(dish1, dishNull);
        assertNotEquals(dishNull, dish1);
    }

    /**
     * Should fail equals due picture
     */
    @Test
    public void noEqualsPicture() {
        final Dish dish1 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish1.setId("ID");
        dish1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dish2 = new Dish("N", "Dec", "Pic2", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish2.setId("ID");
        dish2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dishNull = new Dish("N", "Dec", null, 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dishNull.setId("ID");
        dishNull.setCourses(List.of(new Course("C1"), new Course("C2")));

        assertNotEquals(dish1, dish2);
        assertNotEquals(dish1, dishNull);
        assertNotEquals(dishNull, dish1);
    }

    /**
     * Should fail equals due price
     */
    @Test
    public void noEqualsPrice() {
        final Dish dish1 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish1.setId("ID");
        dish1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dish2 = new Dish("N", "Dec", "Pic", 51.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish2.setId("ID");
        dish2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dishNull = new Dish("N", "Dec", "Pic", null, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dishNull.setId("ID");
        dishNull.setCourses(List.of(new Course("C1"), new Course("C2")));

        assertNotEquals(dish1, dish2);
        assertNotEquals(dish1, dishNull);
        assertNotEquals(dishNull, dish1);
    }

    /**
     * Should fail equals due status
     */
    @Test
    public void noEqualsStatus() {
        final Dish dish1 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish1.setId("ID");
        dish1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dish2 = new Dish("N", "Dec", "Pic", 50.5F, 2, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish2.setId("ID");
        dish2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dishNull = new Dish("N", "Dec", "Pic", 50.5F, null, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dishNull.setId("ID");
        dishNull.setCourses(List.of(new Course("C1"), new Course("C2")));

        assertNotEquals(dish1, dish2);
        assertNotEquals(dish1, dishNull);
        assertNotEquals(dishNull, dish1);
    }

    /**
     * Should fail equals due courseType
     */
    @Test
    public void noEqualsCourseType() {
        final Dish dish1 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish1.setId("ID");
        dish1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dish2 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT2"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish2.setId("ID");
        dish2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dishNull = new Dish("N", "Dec", "Pic", 50.5F, 1, null, Set.of(new Category("Cat1"), new Category("Cat2")));
        dishNull.setId("ID");
        dishNull.setCourses(List.of(new Course("C1"), new Course("C2")));

        assertNotEquals(dish1, dish2);
        assertNotEquals(dish1, dishNull);
        assertNotEquals(dishNull, dish1);
    }

    /**
     * Should fail equals due categories
     */
    @Test
    public void noEqualsCategories() {
        final Dish dish1 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish1.setId("ID");
        dish1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dish2 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1")));
        dish2.setId("ID");
        dish2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dishNull = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), null);
        dishNull.setId("ID");
        dishNull.setCourses(List.of(new Course("C1"), new Course("C2")));

        assertNotEquals(dish1, dish2);
        assertNotEquals(dish1, dishNull);
        assertNotEquals(dishNull, dish1);
    }

    /**
     * Should fail equals due courses
     */
    @Test
    public void noEqualsCourses() {
        final Dish dish1 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish1.setId("ID");
        dish1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dish2 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish2.setId("ID");
        dish2.setCourses(List.of(new Course("C1")));
        final Dish dishNull = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dishNull.setId("ID");
        dishNull.setCourses(null);

        assertNotEquals(dish1, dish2);
        assertNotEquals(dish1, dishNull);
        assertNotEquals(dishNull, dish1);
    }

    /**
     * Should be equals
     */
    @Test
    public void testEquals() {
        final Dish dish1 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish1.setId("ID");
        dish1.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dish2 = new Dish("N", "Dec", "Pic", 50.5F, 1, new CourseType("CT1"), Set.of(new Category("Cat1"), new Category("Cat2")));
        dish2.setId("ID");
        dish2.setCourses(List.of(new Course("C1"), new Course("C2")));
        final Dish dishNull1 = new Dish();
        final Dish dishNull2 = new Dish();

        assertNotSame(dish1, dish2);
        assertEquals(dish1, dish2);
        assertNotSame(dishNull1, dishNull2);
        assertEquals(dishNull1, dishNull2);
    }
}