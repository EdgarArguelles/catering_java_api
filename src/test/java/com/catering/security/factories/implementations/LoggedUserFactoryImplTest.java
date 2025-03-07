package com.catering.security.factories.implementations;

import com.catering.exceptions.CateringValidationException;
import com.catering.models.Permission;
import com.catering.models.Person;
import com.catering.models.Role;
import com.catering.security.factories.LoggedUserFactory;
import com.catering.security.pojos.LoggedUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class LoggedUserFactoryImplTest {

    @Autowired
    private LoggedUserFactory loggedUserFactory;

    /**
     * Should throw CateringValidationException when person null
     */
    @Test
    public void loggedUserPersonNull() {
        assertThrows(CateringValidationException.class, () -> loggedUserFactory.loggedUser(null));
    }

    /**
     * Should throw CateringValidationException when person's Roles is null
     */
    @Test
    public void loggedUserRolesNull() {
        final Person person = new Person("P1");
        assertThrows(CateringValidationException.class, () -> loggedUserFactory.loggedUser(person));
    }

    /**
     * Should throw CateringValidationException when person's Roles is empty
     */
    @Test
    public void loggedUserRolesEmpty() {
        final Person person = new Person("P1");
        person.setRoles(Collections.emptySet());
        assertThrows(CateringValidationException.class, () -> loggedUserFactory.loggedUser(person));
    }

    /**
     * Should throw CateringValidationException when requested role id doesn't belong to person
     */
    @Test
    public void loggedUserIncorrectRoleId() {
        final String ROLE_ID = "R0";
        final Person person = new Person("P1");
        person.setRoles(Set.of(new Role("R1"), new Role("R2"), new Role("R3")));

        assertThrows(CateringValidationException.class, () -> loggedUserFactory.loggedUser(person, ROLE_ID));
    }

    /**
     * Should return a LoggedUser with Permissions null
     */
    @Test
    public void loggedUserWithPermissionsNull() {
        final Person person = new Person("P1");
        person.setName("Name");
        person.setLastName("Last Name");
        person.setRoles(Set.of(new Role("R1")));

        final LoggedUser loggedUserExpected = new LoggedUser("P1", "Name Last Name", null, "R1", Collections.emptySet());
        final LoggedUser loggedUserResult = loggedUserFactory.loggedUser(person);

        assertNotSame(loggedUserExpected, loggedUserResult);
        assertEquals(loggedUserExpected, loggedUserResult);
    }

    /**
     * Should return a LoggedUser with Permissions empty
     */
    @Test
    public void loggedUserWithPermissionsEmpty() {
        final Role role = new Role("R1");
        role.setPermissions(Collections.emptySet());
        final Person person = new Person("P1");
        person.setName("Name");
        person.setLastName("Last Name");
        person.setRoles(Set.of(role));

        final LoggedUser loggedUserExpected = new LoggedUser("P1", "Name Last Name", null, "R1", Collections.emptySet());
        final LoggedUser loggedUserResult = loggedUserFactory.loggedUser(person);

        assertNotSame(loggedUserExpected, loggedUserResult);
        assertEquals(loggedUserExpected, loggedUserResult);
    }

    /**
     * Should return a LoggedUser with Permissions
     */
    @Test
    public void loggedUserWithPermissions() {
        final Role role = new Role("R1");
        role.setPermissions(Set.of(new Permission("PP1", "D"), new Permission("PP2", "D2"), new Permission("PP1", "D3")));
        final Person person = new Person("P1");
        person.setName("Name");
        person.setLastName("Last Name");
        person.setRoles(Set.of(role));

        final LoggedUser loggedUserExpected = new LoggedUser("P1", "Name Last Name", null, "R1", Set.of("PP1", "PP2"));
        final LoggedUser loggedUserResult = loggedUserFactory.loggedUser(person);

        assertNotSame(loggedUserExpected, loggedUserResult);
        assertEquals(loggedUserExpected, loggedUserResult);
    }

    /**
     * Should return a LoggedUser with Permissions
     */
    @Test
    public void loggedUserWithRole() {
        final String ROLE_ID = "R1";
        final Role role = new Role("R1");
        role.setPermissions(Set.of(new Permission("PP1", "D"), new Permission("PP2", "D2"), new Permission("PP1", "D3")));
        final Person person = new Person("P1");
        person.setName("Name");
        person.setLastName("Last Name");
        person.setRoles(Set.of(role));

        final LoggedUser loggedUserExpected = new LoggedUser("P1", "Name Last Name", null, "R1", Set.of("PP1", "PP2"));
        final LoggedUser loggedUserResult = loggedUserFactory.loggedUser(person, ROLE_ID);

        assertNotSame(loggedUserExpected, loggedUserResult);
        assertEquals(loggedUserExpected, loggedUserResult);
    }

    /**
     * Should return a LoggedUser with Permissions
     */
    @Test
    public void loggedUserWithImage() {
        final String IMAGE = "image";
        final String ROLE_ID = "R1";
        final Role role = new Role("R1");
        role.setPermissions(Set.of(new Permission("PP1", "D"), new Permission("PP2", "D2"), new Permission("PP1", "D3")));
        final Person person = new Person("P1");
        person.setName("Name");
        person.setLastName("Last Name");
        person.setRoles(Set.of(role));

        final LoggedUser loggedUserExpected = new LoggedUser("P1", "Name Last Name", IMAGE, "R1", Set.of("PP1", "PP2"));
        final LoggedUser loggedUserResult = loggedUserFactory.loggedUser(person, ROLE_ID, IMAGE);

        assertNotSame(loggedUserExpected, loggedUserResult);
        assertEquals(loggedUserExpected, loggedUserResult);
    }
}