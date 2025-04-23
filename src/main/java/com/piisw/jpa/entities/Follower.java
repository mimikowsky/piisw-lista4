package com.piisw.jpa.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@NamedEntityGraph(
        name = "follower-with-comment-and-events",
        attributeNodes = {
                @NamedAttributeNode(value = "comment", subgraph = "comment-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "comment-subgraph",
                        attributeNodes = @NamedAttributeNode(value = "events")
                )
        }
)
public class Follower {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    private LocalDateTime subscriptionDate;

    @PrePersist
    protected void onCreate() {
        this.subscriptionDate = LocalDateTime.now();
    }
}
