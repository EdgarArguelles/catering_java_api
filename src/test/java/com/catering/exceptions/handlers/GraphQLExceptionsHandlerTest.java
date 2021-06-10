package com.catering.exceptions.handlers;

import com.catering.exceptions.*;
import com.catering.pojos.responses.error.nesteds.NestedError;
import com.catering.pojos.responses.error.nesteds.ValidationNestedError;
import graphql.GraphQLError;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.execution.ResultPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GraphQLExceptionsHandlerTest {

    @Autowired
    private GraphQLExceptionsHandler graphQLExceptionsHandler;

    @Captor
    private ArgumentCaptor<GraphQLError> captor;

    /**
     * Should create a CateringDontFoundException
     */
    @Test
    public void acceptCateringDontFoundException() {
        final List<Object> path = List.of("test 1", "test 2");
        final ResultPath pathMock = mock(ResultPath.class);
        final DataFetcherExceptionHandlerParameters handlerParametersMock = mock(
                DataFetcherExceptionHandlerParameters.class);
        final Throwable exception = new CateringDontFoundException("error");
        final CateringException cateringException = new CateringDontFoundException("error");
        cateringException.setPath(path);
        final List<CateringException> exceptionsExpected = List.of(cateringException);
        when(pathMock.toList()).thenReturn(path);
        when(handlerParametersMock.getException()).thenReturn(exception);
        when(handlerParametersMock.getPath()).thenReturn(pathMock);

        DataFetcherExceptionHandlerResult result = graphQLExceptionsHandler.onException(handlerParametersMock);

        assertNotSame(exceptionsExpected, result.getErrors());
        assertEquals(exceptionsExpected, result.getErrors());
        assertEquals(exceptionsExpected.get(0).getMessage(), cateringException.getMessage());
        assertEquals(exceptionsExpected.get(0).getClass().getName(), cateringException.getClass().getName());
    }

    /**
     * Should create a CateringInternalException
     */
    @Test
    public void acceptCateringInternalException() {
        final List<Object> path = List.of("test 1", "test 2");
        final ResultPath pathMock = mock(ResultPath.class);
        final DataFetcherExceptionHandlerParameters handlerParametersMock = mock(
                DataFetcherExceptionHandlerParameters.class);
        final Throwable exception = new RuntimeException("error");
        final CateringException cateringException = new CateringInternalException("An error has occurred.");
        cateringException.setPath(path);
        cateringException.setDevMessage("error");
        final List<CateringException> exceptionsExpected = List.of(cateringException);
        when(pathMock.toList()).thenReturn(path);
        when(handlerParametersMock.getException()).thenReturn(exception);
        when(handlerParametersMock.getPath()).thenReturn(pathMock);

        DataFetcherExceptionHandlerResult result = graphQLExceptionsHandler.onException(handlerParametersMock);

        assertNotSame(exceptionsExpected, result.getErrors());
        assertEquals(exceptionsExpected, result.getErrors());
        assertEquals(exceptionsExpected.get(0).getMessage(), cateringException.getMessage());
        assertEquals(exceptionsExpected.get(0).getClass().getName(), cateringException.getClass().getName());
    }

    /**
     * Should create a CateringAuthenticationException
     */
    @Test
    public void acceptCateringAuthenticationException() {
        final List<Object> path = List.of("test 1", "test 2");
        final ResultPath pathMock = mock(ResultPath.class);
        final DataFetcherExceptionHandlerParameters handlerParametersMock = mock(
                DataFetcherExceptionHandlerParameters.class);
        final Throwable exception = new AccessDeniedException("error");
        final CateringException cateringException = new CateringAuthenticationException("Access is denied.");
        cateringException.setPath(path);
        cateringException.setDevMessage("error");
        final List<CateringException> exceptionsExpected = List.of(cateringException);
        when(pathMock.toList()).thenReturn(path);
        when(handlerParametersMock.getException()).thenReturn(exception);
        when(handlerParametersMock.getPath()).thenReturn(pathMock);

        DataFetcherExceptionHandlerResult result = graphQLExceptionsHandler.onException(handlerParametersMock);

        assertNotSame(exceptionsExpected, result.getErrors());
        assertEquals(exceptionsExpected, result.getErrors());
        assertEquals(exceptionsExpected.get(0).getMessage(), cateringException.getMessage());
        assertEquals(exceptionsExpected.get(0).getClass().getName(), cateringException.getClass().getName());
    }

    /**
     * Should create a CateringValidationException
     */
    @Test
    public void acceptCateringValidationException() {
        final var violation1Mock = mock(ConstraintViolation.class);
        final Path path1Mock = mock(Path.class);
        final DataFetcherExceptionHandlerParameters handlerParametersMock = mock(
                DataFetcherExceptionHandlerParameters.class);
        when(path1Mock.toString()).thenReturn("test.test.username");
        when(violation1Mock.getPropertyPath()).thenReturn(path1Mock);
        when(violation1Mock.getMessage()).thenReturn("error 1");

        final ConstraintViolationException exceptionMock = mock(ConstraintViolationException.class);
        when(exceptionMock.getConstraintViolations()).thenReturn(Set.of(violation1Mock));
        when(exceptionMock.getMessage()).thenReturn("error");

        final List<NestedError> nestedErrors = List.of(new ValidationNestedError("username", "error 1"));
        final List<Object> path = List.of("test 1", "test 2");
        final ResultPath pathMock = mock(ResultPath.class);
        final CateringException cateringException = new CateringValidationException("Some data aren't valid.",
                nestedErrors);
        cateringException.setPath(path);
        cateringException.setDevMessage("error");
        final List<CateringException> exceptionsExpected = List.of(cateringException);
        when(pathMock.toList()).thenReturn(path);
        when(handlerParametersMock.getException()).thenReturn(exceptionMock);
        when(handlerParametersMock.getPath()).thenReturn(pathMock);

        DataFetcherExceptionHandlerResult result = graphQLExceptionsHandler.onException(handlerParametersMock);

        assertNotSame(exceptionsExpected, result.getErrors());
        assertEquals(exceptionsExpected, result.getErrors());
        assertEquals(exceptionsExpected.get(0).getMessage(), cateringException.getMessage());
        assertEquals(exceptionsExpected.get(0).getClass().getName(), cateringException.getClass().getName());
    }
}