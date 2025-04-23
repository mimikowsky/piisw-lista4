package com.piisw.jpa.repositories;

import com.piisw.jpa.entities.Follower;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Long> {

    @EntityGraph(attributePaths = {"comment", "comment.events"})
    List<FollowerProjection> findAllByUserId(String userId);

}
