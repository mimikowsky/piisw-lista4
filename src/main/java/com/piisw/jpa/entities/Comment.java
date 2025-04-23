package com.piisw.jpa.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Comment {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String content;

    @ManyToMany
    @JoinTable(
            name = "comment_event",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private List<Follower> followers;
}
