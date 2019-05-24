package com.catering.exceptions.handlers;

import com.catering.exceptions.CateringAuthenticationException;
import com.catering.exceptions.CateringException;
import com.catering.exceptions.CateringInternalException;
import com.catering.exceptions.CateringValidationException;
import com.catering.pojos.responses.error.nesteds.NestedError;
import com.catering.pojos.responses.error.nesteds.ValidationNestedError;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GraphQLExceptionsHandler implements DataFetcherExceptionHandler {

    @Override
    public void accept(DataFetcherExceptionHandlerParameters handlerParameters) {
        Throwable exception = handlerParameters.getException();

        CateringException cateringException;
        if (exception instanceof CateringException) {
            cateringException = (CateringException) exception;
        } else {
            cateringException = new CateringInternalException("An error has occurred.");
            if (exception instanceof AccessDeniedException) {
                cateringException = new CateringAuthenticationException("Access is denied.");
            }
            if (exception instanceof ConstraintViolationException) {
                List<NestedError> nestedErrors = ((ConstraintViolationException) exception).getConstraintViolations().stream()
                        .map(violation -> {
                            String field = violation.getPropertyPath().toString();
                            field = field.substring(field.lastIndexOf(".") + 1);
                            return new ValidationNestedError(field, violation.getMessage());
                        })
                        .collect(Collectors.toList());
                cateringException = new CateringValidationException("Some data aren't valid.", nestedErrors);
            }

            cateringException.setDevMessage(exception.getMessage());
        }

        cateringException.setPath(handlerParameters.getPath().toList());
        handlerParameters.getExecutionContext().addError(cateringException);
    }
}