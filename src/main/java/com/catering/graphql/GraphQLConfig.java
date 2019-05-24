package com.catering.graphql;

import com.catering.exceptions.handlers.GraphQLExceptionsHandler;
import com.catering.graphql.instrumentations.CustomMaxQueryDepthInstrumentation;
import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.AsyncSerialExecutionStrategy;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.execution.complexity.ComplexityAnalysisInstrumentation;
import io.leangen.graphql.execution.complexity.JavaScriptEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configure Custom GraphQL
 */
@Configuration
public class GraphQLConfig {

    private final Integer MAX_COMPLEXITY = 200;

    private final Integer MAX_DEPTH = 5;

    @Bean
    public GraphQLSchema graphQLSchema(GraphQLSchemaGenerator schemaGenerator) {
        return schemaGenerator.generate();
    }

    @Bean
    public GraphQL graphQL(GraphQLSchema schema, GraphQLExceptionsHandler graphQLExceptionsHandler) {
        List<Instrumentation> instrumentations = List.of(new ComplexityAnalysisInstrumentation(new JavaScriptEvaluator(), MAX_COMPLEXITY), new CustomMaxQueryDepthInstrumentation(MAX_DEPTH));

        return GraphQL.newGraphQL(schema)
                .queryExecutionStrategy(new AsyncExecutionStrategy(graphQLExceptionsHandler))
                .mutationExecutionStrategy(new AsyncSerialExecutionStrategy(graphQLExceptionsHandler))
                .instrumentation(new ChainedInstrumentation(instrumentations))
                .build();
    }
}