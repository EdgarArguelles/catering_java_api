package com.catering.models;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CategoryTest {

    /**
     * Should create default constructor
     */
    @Test
    public void constructorDefault() {
        final Category category = new Category();

        assertNull(category.getId());
        assertNull(category.getName());
        assertNull(category.getDishes());
    }

    /**
     * Should create Id constructor
     */
    @Test
    public void constructorId() {
        final String ID = "ID";
        final Category category = new Category();
        category.setId(ID);

        assertSame(ID, category.getId());
        assertNull(category.getName());
        assertNull(category.getDishes());
    }

    /**
     * Should create complete constructor
     */
    @Test
    public void constructorComplete() {
        final String NAME = "name";
        final Category category = new Category(NAME);

        assertNull(category.getId());
        assertSame(NAME, category.getName());
        assertNull(category.getDishes());
    }

    /**
     * Should set and get id
     */
    @Test
    public void setGetID() {
        final Category category = new Category();
        final String ID = "ID";
        category.setId(ID);

        assertSame(ID, category.getId());
    }

    /**
     * Should set and get name
     */
    @Test
    public void setGetName() {
        final Category category = new Category();
        final String NAME = "name";
        category.setName(NAME);

        assertSame(NAME, category.getName());
    }

    /**
     * Should set and get dishes
     */
    @Test
    public void setGetDishes() {
        final Category category = new Category();
        final List<Dish> DISHES = List.of(new Dish("D1"), new Dish("D1"));
        category.setDishes(DISHES);

        assertSame(DISHES, category.getDishes());
    }

    /**
     * Should get toString
     */
    @Test
    public void toStringValid() {
        final String ID = "ID";
        final String NAME = "name";
        final Category category = new Category(NAME);
        category.setId(ID);

        assertEquals("Category(super=Model(id=" + ID + "), name=" + NAME + ")", category.toString());
    }

    /**
     * Should equals instances
     */
    @Test
    public void equalsInstance() {
        final Category category = new Category("ID");

        assertTrue(category.equals(category));
        assertFalse(category.equals(null));
        assertFalse(category.equals(new String()));
    }

    /**
     * Should fail equals due ID
     */
    @Test
    public void noEqualsID() {
        final Category category1 = new Category("N");
        category1.setId("ID");
        category1.setDishes(List.of(new Dish("D1"), new Dish("D2")));
        final Category category2 = new Category("N");
        category2.setId("ID2");
        category2.setDishes(List.of(new Dish("D1"), new Dish("D2")));
        final Category categoryNull = new Category("N");
        categoryNull.setId(null);
        categoryNull.setDishes(List.of(new Dish("D1"), new Dish("D2")));

        assertNotEquals(category1, category2);
        assertNotEquals(category1, categoryNull);
        assertNotEquals(categoryNull, category1);
    }

    /**
     * Should fail equals due name
     */
    @Test
    public void noEqualsName() {
        final Category category1 = new Category("N");
        category1.setId("ID");
        category1.setDishes(List.of(new Dish("D1"), new Dish("D2")));
        final Category category2 = new Category("N2");
        category2.setId("ID");
        category2.setDishes(List.of(new Dish("D1"), new Dish("D2")));
        final Category categoryNull = new Category(null);
        categoryNull.setId("ID");
        categoryNull.setDishes(List.of(new Dish("D1"), new Dish("D2")));

        assertNotEquals(category1, category2);
        assertNotEquals(category1, categoryNull);
        assertNotEquals(categoryNull, category1);
    }

    /**
     * Should fail equals due dishes
     */
    @Test
    public void noEqualsDishes() {
        final Category category1 = new Category("N");
        category1.setId("ID");
        category1.setDishes(List.of(new Dish("D1"), new Dish("D2")));
        final Category category2 = new Category("N");
        category2.setId("ID");
        category2.setDishes(List.of(new Dish("D1")));
        final Category categoryNull = new Category("N");
        categoryNull.setId("ID");
        categoryNull.setDishes(null);

        assertNotEquals(category1, category2);
        assertNotEquals(category1, categoryNull);
        assertNotEquals(categoryNull, category1);
    }

    /**
     * Should be equals
     */
    @Test
    public void testEquals() {
        final Category category1 = new Category("N");
        category1.setId("ID");
        category1.setDishes(List.of(new Dish("D1"), new Dish("D2")));
        final Category category2 = new Category("N");
        category2.setId("ID");
        category2.setDishes(List.of(new Dish("D1"), new Dish("D2")));
        final Category categoryNull1 = new Category();
        final Category categoryNull2 = new Category();

        assertNotSame(category1, category2);
        assertEquals(category1, category2);
        assertNotSame(categoryNull1, categoryNull2);
        assertEquals(categoryNull1, categoryNull2);
    }
}