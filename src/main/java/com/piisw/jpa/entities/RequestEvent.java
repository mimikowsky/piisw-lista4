package com.piisw.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RequestEvent extends Event {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HttpMethod method;

}
