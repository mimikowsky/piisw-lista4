package com.piisw.jpa.services;

import com.piisw.jpa.entities.Server;
import com.piisw.jpa.repositories.ServerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ServerService {

    private final ServerRepository serverRepository;

    public Optional<Server> findByName(String name) {
        return serverRepository.findByName(name);
    }

}
