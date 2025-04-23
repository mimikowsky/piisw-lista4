package com.piisw.jpa.tasks;

import com.piisw.jpa.entities.Event;
import com.piisw.jpa.entities.RequestEvent;
import com.piisw.jpa.repositories.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@DataJpaTest
class Task3 {

    @Autowired
    EntityManager entityManager;

    @Autowired
    EventRepository repository;

    @Test
    void shouldDeleteInBulkEventsOlderThan() throws Exception {
        // given
        LocalDateTime givenDate = LocalDateTime.of(2017, 12, 31, 0, 0);

        // when
        repository.deleteInBulkBeforDate(givenDate);

        // then
        assertThat(new SimpleJpaRepository<Event, Long>(Event.class, entityManager).findAll(), hasSize(32));
    }

    @Test
    void shouldUpdateEventsLongerThanInBulk() throws Exception {
        // given
        int threshold = 1000;
        Class<RequestEvent> clazz = RequestEvent.class;

        // when
		repository.updateInBulkToBeAnalyzedByType(clazz, threshold);

        // then
        assertThat(new SimpleJpaRepository<Event, Long>(Event.class, entityManager).findAll().stream()//
                .filter(e -> e.getDuration() > threshold)//
                .filter(Event::isAnalysisRequired)//
                .count(), is(3L));
    }

}
