package com.catering.security.services.implementations;

import com.catering.exceptions.CateringValidationException;
import com.catering.models.Person;
import com.catering.models.Role;
import com.catering.repositories.PersonRepository;
import com.catering.security.factories.LoggedUserFactory;
import com.catering.security.pojos.LoggedUser;
import com.catering.security.services.SecurityService;
import com.catering.security.services.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityServiceImplTest {

    @Autowired
    private SecurityService securityService;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private LoggedUserFactory loggedUserFactory;

    @MockBean
    private TokenService tokenService;

    /**
     * Should throw CateringValidationException when not context
     */
    @Test(expected = CateringValidationException.class)
    public void changeRoleNotContext() throws IOException {
        SecurityContextHolder.getContext().setAuthentication(null);
        final String ROLE_ID = "R1";

        securityService.changeRole(ROLE_ID);
    }

    /**
     * Should return a LoggedUser when correct and person is Null
     */
    @Test
    public void changeRoleCorrectWhenNull() throws IOException {
        final String PERSON_ID = "P1";
        final LoggedUser userMocked = new LoggedUser(PERSON_ID, "ROLE");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userMocked, null));

        final String ROLE_ID = "R2";
        given(personRepository.findById(PERSON_ID)).willReturn(Optional.empty());

        final String TOKEN = "token";
        final LoggedUser loggedUser = new LoggedUser("P1", "Name Last Name", "I", "R2", Collections.emptySet());
        final LoggedUser loggedUserExpected = new LoggedUser("P1", "Name Last Name", "I", "R2", Collections.emptySet());
        loggedUserExpected.setToken(TOKEN);
        given(loggedUserFactory.loggedUser(null, ROLE_ID)).willReturn(loggedUser);
        given(tokenService.createToken(loggedUser)).willReturn(TOKEN);
        given(tokenService.getLoggedUser(TOKEN)).willReturn(loggedUser);

        final LoggedUser loggedUserResult = securityService.changeRole(ROLE_ID);

        assertNotSame(loggedUserExpected, loggedUserResult);
        assertEquals(loggedUserExpected, loggedUserResult);
        verify(personRepository, times(1)).findById(PERSON_ID);
        verify(loggedUserFactory, times(1)).loggedUser(null, ROLE_ID);
        verify(tokenService, times(1)).createToken(loggedUser);
        verify(tokenService, times(1)).getLoggedUser(TOKEN);
    }

    /**
     * Should return a LoggedUser when correct
     */
    @Test
    public void changeRoleCorrect() throws IOException {
        final String PERSON_ID = "P1";
        final LoggedUser userMocked = new LoggedUser(PERSON_ID, "ROLE");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userMocked, null));

        final String ROLE_ID = "R2";
        final Person person = new Person("P1");
        person.setName("Name");
        person.setLastName("Last Name");
        person.setRoles(Set.of(new Role("R1"), new Role("R2"), new Role("R3")));
        given(personRepository.findById(PERSON_ID)).willReturn(Optional.of(person));

        final String TOKEN = "token";
        final LoggedUser loggedUser = new LoggedUser("P1", "Name Last Name", "I", "R2", Collections.emptySet());
        final LoggedUser loggedUserExpected = new LoggedUser("P1", "Name Last Name", "I", "R2", Collections.emptySet());
        loggedUserExpected.setToken(TOKEN);
        given(loggedUserFactory.loggedUser(person, ROLE_ID)).willReturn(loggedUser);
        given(tokenService.createToken(loggedUser)).willReturn(TOKEN);
        given(tokenService.getLoggedUser(TOKEN)).willReturn(loggedUser);

        final LoggedUser loggedUserResult = securityService.changeRole(ROLE_ID);

        assertNotSame(loggedUserExpected, loggedUserResult);
        assertEquals(loggedUserExpected, loggedUserResult);
        verify(personRepository, times(1)).findById(PERSON_ID);
        verify(loggedUserFactory, times(1)).loggedUser(person, ROLE_ID);
        verify(tokenService, times(1)).createToken(loggedUser);
        verify(tokenService, times(1)).getLoggedUser(TOKEN);
    }

    /**
     * Should return null when not context
     */
    @Test
    public void getLoggedUserNotContext() {
        SecurityContextHolder.getContext().setAuthentication(null);

        final LoggedUser userResult = securityService.getLoggedUser();

        assertNull(userResult);
    }

    /**
     * Should return null when context invalid
     */
    @Test
    public void getLoggedUserInvalid() {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(new Person("ID"), null));

        final LoggedUser userResult = securityService.getLoggedUser();

        assertNull(userResult);
    }

    /**
     * Should return a LoggedUser when context valid
     */
    @Test
    public void getLoggedUserCorrect() throws JsonProcessingException {
        final LoggedUser userMocked = new LoggedUser("ID", "ROLE");

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userMocked, null));

        final String TOKEN = "token";
        final LoggedUser userExpected = new LoggedUser("ID", "ROLE");
        userExpected.setToken(TOKEN);
        given(tokenService.createToken(userMocked)).willReturn(TOKEN);

        final LoggedUser userResult = securityService.getLoggedUser();

        assertSame(userMocked, userResult);
        assertNotSame(userExpected, userResult);
        assertEquals(userExpected, userResult);
        verify(tokenService, times(1)).createToken(userMocked);
    }
}