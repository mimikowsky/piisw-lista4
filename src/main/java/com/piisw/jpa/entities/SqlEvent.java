package com.piisw.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class SqlEvent extends Event {

    @Column(length = 4000, nullable = false)
    private String sqlQuery;

}
