package com.catering.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VersionTest {

    /**
     * Should create default constructor
     */
    @Test
    public void constructorDefault() {
        final Version version = new Version();

        assertNull(version.getVersion());
    }

    /**
     * Should create complete constructor
     */
    @Test
    public void constructorComplete() {
        final Long VERSION = 5L;
        final Version version = new Version(VERSION);

        assertSame(VERSION, version.getVersion());
    }

    /**
     * Should set and get version
     */
    @Test
    public void setGetVersion() {
        final Version version = new Version();
        final Long VERSION = 5L;
        version.setVersion(VERSION);

        assertSame(VERSION, version.getVersion());
    }

    /**
     * Should get toString
     */
    @Test
    public void toStringValid() {
        final Long VERSION = 5L;
        final Version version = new Version(VERSION);

        assertEquals("Version(version=" + VERSION + ")", version.toString());
    }

    /**
     * Should equals instances
     */
    @Test
    public void equalsInstance() {
        final Version version = new Version(5L);

        assertTrue(version.equals(version));
        assertFalse(version.equals(null));
        assertFalse(version.equals(new String()));
    }

    /**
     * Should fail equals due version
     */
    @Test
    public void noEqualsVersion() {
        final Version version1 = new Version(5L);
        final Version version2 = new Version(6L);
        final Version versionNull = new Version();

        assertNotEquals(version1, version2);
        assertNotEquals(version1, versionNull);
        assertNotEquals(versionNull, version1);
    }

    /**
     * Should be equals
     */
    @Test
    public void testEquals() {
        final Version version1 = new Version(5L);
        final Version version2 = new Version(5L);
        final Version versionNull1 = new Version();
        final Version versionNull2 = new Version();

        assertNotSame(version1, version2);
        assertEquals(version1, version2);
        assertNotSame(versionNull1, versionNull2);
        assertEquals(versionNull1, versionNull2);
    }
}