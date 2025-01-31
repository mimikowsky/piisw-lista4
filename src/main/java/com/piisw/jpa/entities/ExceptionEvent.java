package com.piisw.jpa.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class ExceptionEvent extends Event {

    private String exceptionName;

    private String occuranceClass;

    private String occuranceMethod;

    private Integer occuranceLine;

}
