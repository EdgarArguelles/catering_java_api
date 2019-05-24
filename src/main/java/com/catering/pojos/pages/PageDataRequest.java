package com.catering.pojos.pages;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.leangen.graphql.annotations.GraphQLEnumValue;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Page Request pojo
 */
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
@GraphQLType(description = "Pagination's data")
public class PageDataRequest {

    public enum SORT_DIRECTION {
        @GraphQLEnumValue(description = "Sort in ascendant order") ASC,
        @GraphQLEnumValue(description = "Sort in decedent order") DESC
    }

    @NotNull
    @Min(0)
    @Getter
    private Integer page;

    @NotNull
    @Min(1)
    @Max(5)
    @Getter
    private Integer size;

    @Getter
    private SORT_DIRECTION direction;

    @Getter
    private List<String> sort;

    /**
     * Create an instance
     *
     * @param page      current page
     * @param size      page size
     * @param direction page sort direction (could be ASC or DESC)
     * @param sort      page sort values
     */
    public PageDataRequest(Integer page, Integer size, SORT_DIRECTION direction, List<String> sort) {
        this.page = page;
        this.size = size;
        this.direction = direction;
        this.sort = sort;
    }
}