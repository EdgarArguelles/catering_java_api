package com.catering.models;

import com.catering.integration_test.IntegrationTest;
import com.catering.pojos.responses.error.nesteds.NestedError;
import com.catering.pojos.responses.error.nesteds.ValidationNestedError;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class QuotationTest {

    /**
     * Should create default constructor
     */
    @Test
    public void constructorDefault() {
        final Quotation quotation = new Quotation();

        assertNull(quotation.getId());
        assertNull(quotation.getName());
        assertNull(quotation.getPrice());
        assertNull(quotation.getPerson());
        assertNull(quotation.getMenus());
    }

    /**
     * Should create Id constructor
     */
    @Test
    public void constructorId() {
        final String ID = "ID";
        final Quotation quotation = new Quotation(ID);

        assertSame(ID, quotation.getId());
        assertNull(quotation.getName());
        assertNull(quotation.getPrice());
        assertNull(quotation.getPerson());
        assertNull(quotation.getMenus());
    }

    /**
     * Should create complete constructor
     */
    @Test
    public void constructorComplete() {
        final String NAME = "name";
        final Float PRICE = 50.5F;
        final Person PERSON = new Person("P1");
        final Quotation quotation = new Quotation(NAME, PRICE, PERSON);

        assertNull(quotation.getId());
        assertSame(NAME, quotation.getName());
        assertSame(PRICE, quotation.getPrice());
        assertSame(PERSON, quotation.getPerson());
        assertNull(quotation.getMenus());
    }

    /**
     * Should set and get id
     */
    @Test
    public void setGetID() {
        final Quotation quotation = new Quotation();
        final String ID = "ID";
        quotation.setId(ID);

        assertSame(ID, quotation.getId());
    }

    /**
     * Should set and get name
     */
    @Test
    public void setGetName() {
        final Quotation quotation = new Quotation();
        final String NAME = "name";
        quotation.setName(NAME);

        assertSame(NAME, quotation.getName());
    }

    /**
     * Should set and get price
     */
    @Test
    public void setGetPrice() {
        final Quotation quotation = new Quotation();
        final Float PRICE = 50.5F;
        quotation.setPrice(PRICE);

        assertSame(PRICE, quotation.getPrice());
    }

    /**
     * Should set and get person
     */
    @Test
    public void setGetPerson() {
        final Quotation quotation = new Quotation();
        final Person PERSON = new Person("P1");
        quotation.setPerson(PERSON);

        assertSame(PERSON, quotation.getPerson());
    }

    /**
     * Should set and get menus
     */
    @Test
    public void setGetMenus() {
        final Quotation quotation = new Quotation();
        final List<Menu> MENUS = List.of(new Menu("M1"), new Menu("M2"));
        quotation.setMenus(MENUS);

        assertSame(MENUS, quotation.getMenus());
    }

    /**
     * Should get toString
     */
    @Test
    public void toStringValid() {
        final String ID = "ID";
        final String NAME = "name";
        final Quotation quotation = new Quotation(ID);
        quotation.setName(NAME);

        assertEquals("Quotation(super=Model(id=" + ID + "), name=" + NAME + ")", quotation.toString());
    }

    /**
     * Should equals instances
     */
    @Test
    public void equalsInstance() {
        final Quotation quotation = new Quotation("ID");

        assertTrue(quotation.equals(quotation));
        assertFalse(quotation.equals(null));
        assertFalse(quotation.equals(new String()));
    }

    /**
     * Should fail equals due ID
     */
    @Test
    public void noEqualsID() {
        final Quotation quotation1 = new Quotation("N", 5.5F, new Person("P"));
        quotation1.setId("ID");
        quotation1.setMenus(List.of(new Menu("M1"), new Menu("M2")));
        final Quotation quotation2 = new Quotation("N", 5.5F, new Person("P"));
        quotation2.setId("ID2");
        quotation2.setMenus(List.of(new Menu("M1"), new Menu("M2")));
        final Quotation quotationNull = new Quotation("N", 5.5F, new Person("P"));
        quotationNull.setId(null);
        quotationNull.setMenus(List.of(new Menu("M1"), new Menu("M2")));

        assertNotEquals(quotation1, quotation2);
        assertNotEquals(quotation1, quotationNull);
        assertNotEquals(quotationNull, quotation1);
    }

    /**
     * Should fail equals due name
     */
    @Test
    public void noEqualsName() {
        final Quotation quotation1 = new Quotation("N", 5.5F, new Person("P"));
        quotation1.setId("ID");
        quotation1.setMenus(List.of(new Menu("M1"), new Menu("M2")));
        final Quotation quotation2 = new Quotation("N1", 5.5F, new Person("P"));
        quotation2.setId("ID");
        quotation2.setMenus(List.of(new Menu("M1"), new Menu("M2")));
        final Quotation quotationNull = new Quotation(null, 5.5F, new Person("P"));
        quotationNull.setId("ID");
        quotationNull.setMenus(List.of(new Menu("M1"), new Menu("M2")));

        assertNotEquals(quotation1, quotation2);
        assertNotEquals(quotation1, quotationNull);
        assertNotEquals(quotationNull, quotation1);
    }

    /**
     * Should fail equals due price
     */
    @Test
    public void noEqualsPrice() {
        final Quotation quotation1 = new Quotation("N", 5.5F, new Person("P"));
        quotation1.setId("ID");
        quotation1.setMenus(List.of(new Menu("M1"), new Menu("M2")));
        final Quotation quotation2 = new Quotation("N", 5.6F, new Person("P"));
        quotation2.setId("ID");
        quotation2.setMenus(List.of(new Menu("M1"), new Menu("M2")));
        final Quotation quotationNull = new Quotation("N", null, new Person("P"));
        quotationNull.setId("ID");
        quotationNull.setMenus(List.of(new Menu("M1"), new Menu("M2")));

        assertNotEquals(quotation1, quotation2);
        assertNotEquals(quotation1, quotationNull);
        assertNotEquals(quotationNull, quotation1);
    }

    /**
     * Should fail equals due person
     */
    @Test
    public void noEqualsPerson() {
        final Quotation quotation1 = new Quotation("N", 5.5F, new Person("P"));
        quotation1.setId("ID");
        quotation1.setMenus(List.of(new Menu("M1"), new Menu("M2")));
        final Quotation quotation2 = new Quotation("N", 5.5F, new Person("P2"));
        quotation2.setId("ID");
        quotation2.setMenus(List.of(new Menu("M1"), new Menu("M2")));
        final Quotation quotationNull = new Quotation("N", 5.5F, null);
        quotationNull.setId("ID");
        quotationNull.setMenus(List.of(new Menu("M1"), new Menu("M2")));

        assertNotEquals(quotation1, quotation2);
        assertNotEquals(quotation1, quotationNull);
        assertNotEquals(quotationNull, quotation1);
    }

    /**
     * Should fail equals due menus
     */
    @Test
    public void noEqualsMenus() {
        final Quotation quotation1 = new Quotation("N", 5.5F, new Person("P"));
        quotation1.setId("ID");
        quotation1.setMenus(List.of(new Menu("M1"), new Menu("M2")));
        final Quotation quotation2 = new Quotation("N", 5.5F, new Person("P"));
        quotation2.setId("ID");
        quotation2.setMenus(List.of(new Menu("M1")));
        final Quotation quotationNull = new Quotation("N", 5.5F, new Person("P"));
        quotationNull.setId("ID");
        quotationNull.setMenus(null);

        assertNotEquals(quotation1, quotation2);
        assertNotEquals(quotation1, quotationNull);
        assertNotEquals(quotationNull, quotation1);
    }

    /**
     * Should be equals
     */
    @Test
    public void testEquals() {
        final Quotation quotation1 = new Quotation("N", 5.5F, new Person("P"));
        quotation1.setId("ID");
        quotation1.setMenus(List.of(new Menu("M1"), new Menu("M2")));
        final Quotation quotation2 = new Quotation("N", 5.5F, new Person("P"));
        quotation2.setId("ID");
        quotation2.setMenus(List.of(new Menu("M1"), new Menu("M2")));
        final Quotation quotationNull1 = new Quotation();
        final Quotation quotationNull2 = new Quotation();

        assertNotSame(quotation1, quotation2);
        assertEquals(quotation1, quotation2);
        assertNotSame(quotationNull1, quotationNull2);
        assertEquals(quotationNull1, quotationNull2);
    }

    /**
     * Should get 1 error when parameters null
     */
    @Test
    public void validateWhenNull() {
        final Quotation q = new Quotation();
        final List<NestedError> nestedErrorsExpected = List.of(
                new ValidationNestedError("name", "must not be null")
        );
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(q);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 1 error when parameters empty
     */
    @Test
    public void validateWhenEmpty() {
        final Quotation q = new Quotation("", 0F, null);
        final List<NestedError> nestedErrorsExpected = List.of(
                new ValidationNestedError("name", "size must be between 1 and 255")
        );
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(q);

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
        final Quotation q = new Quotation(longText.toString(), 0F, null);
        final List<NestedError> nestedErrorsExpected = List.of(
                new ValidationNestedError("name", "size must be between 1 and 255")
        );
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(q);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 3 errors when Menu is incorrect
     */
    @Test
    public void validateWhenMenuIncorrect() {
        final Quotation q = new Quotation("A", 0F, null);
        q.setMenus(List.of(new Menu()));
        final List<NestedError> nestedErrorsExpected = List.of(
                new ValidationNestedError("menus[0].courses", "must not be empty"),
                new ValidationNestedError("menus[0].name", "must not be null"),
                new ValidationNestedError("menus[0].quantity", "must not be null")
        );
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(q);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }

    /**
     * Should get 0 error when correct
     */
    @Test
    public void validateWhenOK() {
        final Quotation q = new Quotation("A", 0F, null);
        final List<NestedError> nestedErrorsExpected = Collections.emptyList();
        final List<NestedError> nestedErrorsResult = IntegrationTest.getValidationErrors(q);

        assertNotSame(nestedErrorsExpected, nestedErrorsResult);
        assertEquals(nestedErrorsExpected, nestedErrorsResult);
    }
}