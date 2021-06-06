package com.catering.models;

import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@ToString(of = "version")
@EqualsAndHashCode()
@Entity
@Table(name = "version")
@GraphQLType(description = "Data Base's version")
public class Version {

    @Id
    @Getter
    @Setter
    @GraphQLQuery(description = "Data Base's version")
    private Long version;

    public Version(Long version) {
        this.version = version;
    }
}