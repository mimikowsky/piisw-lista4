package com.piisw.jpa.repositories;

import com.piisw.jpa.entities.Server;

public record ServerStatistic(Server server, long count) {
}
