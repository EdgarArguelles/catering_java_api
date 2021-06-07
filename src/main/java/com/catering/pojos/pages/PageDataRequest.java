package com.catering.pojos.pages;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.leangen.graphql.annotations.GraphQLEnumValue;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Page Request pojo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@GraphQLType(description = "Pagination's data")
public record PageDataRequest(@NotNull @Min(0) @GraphQLQuery(description = "Page's number") Integer page,
        @NotNull @Min(1) @Max(5) @GraphQLQuery(description = "Page's size") Integer size,
        @GraphQLQuery(description = "Page's direction [ASC or DESC]") SORT_DIRECTION direction,
        @GraphQLQuery(description = "Page's sort list") List<String> sort) {

    public enum SORT_DIRECTION {
        @GraphQLEnumValue(description = "Sort in ascendant order")
        ASC, @GraphQLEnumValue(description = "Sort in decedent order")
        DESC
    }

    public PageDataRequest() {
        this(null, null, null, null);
    }
}