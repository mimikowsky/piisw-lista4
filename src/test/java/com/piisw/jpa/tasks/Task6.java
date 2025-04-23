package com.piisw.jpa.tasks;

import com.piisw.jpa.entities.*;
import com.piisw.jpa.repositories.FollowerProjection;
import com.piisw.jpa.repositories.FollowerRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
class Task6 {

    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldFindEventInfoByUserId() {
        // given
        Server server = createAndPersistServer();
        Event event = createAndPersistEvent(server);
        Comment comment = createAndPersistComment(event);
        Follower follower = createAndPersistFollower(comment, "user123");

        // when
        List<FollowerProjection> results = followerRepository.findAllByUserId("user123");

        // then
        assertThat(results).hasSize(1);
        FollowerProjection followerProjection = results.get(0);
        assertThat(followerProjection.getSubscriptionDate()).isNotNull();
    }

    @Test
    void shouldFindMultipleEventsForSingleFollower() {
        // given
        Server server = createAndPersistServer();
        Event event1 = createAndPersistEvent(server);
        Event event2 = createAndPersistEvent(server);
        Event event3 = createAndPersistEvent(server);
        Comment comment = createAndPersistComment(event1, event2, event3);
        Follower follower = createAndPersistFollower(comment, "user456");

        // when
        List<FollowerProjection> results = followerRepository.findAllByUserId("user456");

        // then
        assertThat(results).hasSize(1);

        FollowerProjection followerProjection = results.get(0);
        assertThat(followerProjection.getUserId()).isEqualTo("user456");
        assertThat(followerProjection.getSubscriptionDate()).isNotNull();

        FollowerProjection.CommentView commentView = followerProjection.getComment();
        assertThat(commentView).isNotNull();
        assertThat(commentView.getContent()).isEqualTo("Test comment");

        List<FollowerProjection.CommentView.EventView> eventViews = commentView.getEvents();
        assertThat(eventViews).isNotNull();
        assertThat(eventViews).hasSize(3);

        FollowerProjection.CommentView.EventView eventView1 = eventViews.get(0);
        assertThat(eventView1).isNotNull();
        assertThat(eventView1.isAnalysisRequired()).isFalse();

        assertThat(eventViews).extracting(FollowerProjection.CommentView.EventView::getTime)
                .allMatch(java.util.Objects::nonNull);
    }

    @Test
    void shouldReturnEmptyListWhenNoFollowerFound() {
        // when
        List<FollowerProjection> results = followerRepository.findAllByUserId("nonexistent");

        // then
        assertThat(results).isEmpty();
    }

    private Event createAndPersistEvent(Server server) {
        RequestEvent event = new RequestEvent();
        event.setTime(LocalDateTime.now());
        event.setDuration(100);
        event.setThreadId("thread1");
        event.setUserId("testUser");
        event.setServer(server);
        event.setAnalysisRequired(false);
        event.setMethod(HttpMethod.GET);
        entityManager.persist(event);
        return event;
    }

    private Server createAndPersistServer() {
        Server server = new Server();
        server.setName("TestServer");
        server.setIp("127.0.0.1");
        entityManager.persist(server);
        return server;
    }

    private Comment createAndPersistComment(Event... events) {
        Comment comment = new Comment();
        comment.setContent("Test comment");
        entityManager.persist(comment);

        if (comment.getEvents() == null) {
            comment.setEvents(new ArrayList<>());
        }

        Arrays.stream(events).forEach(event -> {
            comment.getEvents().add(event);
            if (event.getComments() == null) {
                event.setComments(new ArrayList<>());
            }
            event.getComments().add(comment);
        });

        entityManager.flush();
        return comment;
    }

    private Follower createAndPersistFollower(Comment comment, String userId) {
        Follower follower = new Follower();
        follower.setUserId(userId);
        follower.setComment(comment);
        entityManager.persist(follower);
        return follower;
    }

}
