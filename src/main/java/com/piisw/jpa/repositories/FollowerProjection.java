package com.piisw.jpa.repositories;

import java.time.LocalDateTime;
import java.util.List;

public interface FollowerProjection {
    String getUserId();
    LocalDateTime getSubscriptionDate();
    CommentView getComment();

    interface CommentView {
        String getContent();
        List<EventView> getEvents();

        interface EventView {
            LocalDateTime getTime();
            boolean isAnalysisRequired();
        }
    }
}
