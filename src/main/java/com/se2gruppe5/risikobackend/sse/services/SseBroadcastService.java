package com.se2gruppe5.risikobackend.sse.services;

import com.se2gruppe5.risikobackend.sse.repositories.SseSinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;

import java.util.Collection;
import java.util.UUID;

@Service
public class SseBroadcastService {
    private final SseSinkRepository sinkRepository;

    @Autowired
    public SseBroadcastService(SseSinkRepository sinkRepository) {
        this.sinkRepository = sinkRepository;
    }

    private UUID generateUUID() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (sinkRepository.hasSink(uuid));
        return uuid;
    }

    public UUID addSink(FluxSink<String> sink) {
        return addSink(generateUUID(), sink);
    }

    public UUID addSink(UUID uuid, FluxSink<String> sink) {
        sinkRepository.addSink(uuid, sink);
        return uuid;
    }

    public void removeSink(UUID uuid) {
        sinkRepository.removeSink(uuid);
    }

    public void send(UUID uuid, String message) {
        FluxSink<String> sink = sinkRepository.getSink(uuid);
        if (sink != null) {
            sink.next(message);
        }
    }

    public void broadcast(String message) {
        for (FluxSink<String> sink : sinkRepository.getSinks()) {
            sink.next(message);
        }
    }

    public void broadcast(Collection<UUID> uuids, String message) {
        for (FluxSink<String> sink : sinkRepository.getSinks(uuids)) {
            sink.next(message);
        }
    }
}
