package com.catering.models;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "version")
@GraphQLType(description = "Data Base's version")
public class Version {

    @Id
    @GraphQLQuery(description = "Data Base's version")
    private Long version;
}