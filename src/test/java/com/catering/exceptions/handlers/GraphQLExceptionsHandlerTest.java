package com.catering.exceptions.handlers;

import com.catering.exceptions.*;
import com.catering.pojos.responses.error.nesteds.NestedError;
import com.catering.pojos.responses.error.nesteds.ValidationNestedError;
import graphql.GraphQLError;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.ExecutionContext;
import graphql.execution.ExecutionPath;
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
        final ExecutionContext executionContextMock = mock(ExecutionContext.class);
        final ExecutionPath pathMock = mock(ExecutionPath.class);
        final Throwable exception = new CateringDontFoundException("error");
        final DataFetcherExceptionHandlerParameters handlerParameters = new DataFetcherExceptionHandlerParameters(executionContextMock, null, null, null, null, pathMock, exception);
        final CateringException exceptionExpected = new CateringDontFoundException("error");
        exceptionExpected.setPath(path);
        when(pathMock.toList()).thenReturn(path);

        graphQLExceptionsHandler.accept(handlerParameters);

        verify(executionContextMock, times(1)).addError(captor.capture());
        captor.getAllValues().forEach(value -> {
            assertNotSame(exceptionExpected, value);
            assertEquals(exceptionExpected, value);
            assertEquals(exceptionExpected.getMessage(), value.getMessage());
            assertEquals(exceptionExpected.getClass().getName(), value.getClass().getName());
        });
    }

    /**
     * Should create a CateringInternalException
     */
    @Test
    public void acceptCateringInternalException() {
        final List<Object> path = List.of("test 1", "test 2");
        final ExecutionContext executionContextMock = mock(ExecutionContext.class);
        final ExecutionPath pathMock = mock(ExecutionPath.class);
        final Throwable exception = new RuntimeException("error");
        final DataFetcherExceptionHandlerParameters handlerParameters = new DataFetcherExceptionHandlerParameters(executionContextMock, null, null, null, null, pathMock, exception);
        final CateringException exceptionExpected = new CateringInternalException("An error has occurred.");
        exceptionExpected.setPath(path);
        exceptionExpected.setDevMessage("error");
        when(pathMock.toList()).thenReturn(path);

        graphQLExceptionsHandler.accept(handlerParameters);

        verify(executionContextMock, times(1)).addError(captor.capture());
        captor.getAllValues().forEach(value -> {
            assertNotSame(exceptionExpected, value);
            assertEquals(exceptionExpected, value);
            assertEquals(exceptionExpected.getMessage(), value.getMessage());
            assertEquals(exceptionExpected.getClass().getName(), value.getClass().getName());
        });
    }

    /**
     * Should create a CateringAuthenticationException
     */
    @Test
    public void acceptCateringAuthenticationException() {
        final List<Object> path = List.of("test 1", "test 2");
        final ExecutionContext executionContextMock = mock(ExecutionContext.class);
        final ExecutionPath pathMock = mock(ExecutionPath.class);
        final Throwable exception = new AccessDeniedException("error");
        final DataFetcherExceptionHandlerParameters handlerParameters = new DataFetcherExceptionHandlerParameters(executionContextMock, null, null, null, null, pathMock, exception);
        final CateringException exceptionExpected = new CateringAuthenticationException("Access is denied.");
        exceptionExpected.setPath(path);
        exceptionExpected.setDevMessage("error");
        when(pathMock.toList()).thenReturn(path);

        graphQLExceptionsHandler.accept(handlerParameters);

        verify(executionContextMock, times(1)).addError(captor.capture());
        captor.getAllValues().forEach(value -> {
            assertNotSame(exceptionExpected, value);
            assertEquals(exceptionExpected, value);
            assertEquals(exceptionExpected.getMessage(), value.getMessage());
            assertEquals(exceptionExpected.getClass().getName(), value.getClass().getName());
        });
    }

    /**
     * Should create a CateringValidationException
     */
    @Test
    public void acceptCateringValidationException() {
        final ConstraintViolation violation1Mock = mock(ConstraintViolation.class);
        final Path path1Mock = mock(Path.class);
        when(path1Mock.toString()).thenReturn("test.test.username");
        when(violation1Mock.getPropertyPath()).thenReturn(path1Mock);
        when(violation1Mock.getMessage()).thenReturn("error 1");

        final ConstraintViolationException exceptionMock = mock(ConstraintViolationException.class);
        when(exceptionMock.getConstraintViolations()).thenReturn(Set.of(violation1Mock));
        when(exceptionMock.getMessage()).thenReturn("error");

        final List<NestedError> nestedErrors = List.of(new ValidationNestedError("username", "error 1"));
        final List<Object> path = List.of("test 1", "test 2");
        final ExecutionContext executionContextMock = mock(ExecutionContext.class);
        final ExecutionPath pathMock = mock(ExecutionPath.class);
        final DataFetcherExceptionHandlerParameters handlerParameters = new DataFetcherExceptionHandlerParameters(executionContextMock, null, null, null, null, pathMock, exceptionMock);
        final CateringException exceptionExpected = new CateringValidationException("Some data aren't valid.", nestedErrors);
        exceptionExpected.setPath(path);
        exceptionExpected.setDevMessage("error");
        when(pathMock.toList()).thenReturn(path);

        graphQLExceptionsHandler.accept(handlerParameters);

        verify(executionContextMock, times(1)).addError(captor.capture());
        captor.getAllValues().forEach(value -> {
            assertNotSame(exceptionExpected, value);
            assertEquals(exceptionExpected, value);
            assertEquals(exceptionExpected.getMessage(), value.getMessage());
            assertEquals(exceptionExpected.getClass().getName(), value.getClass().getName());
        });
    }
}