package com.se2gruppe5.risikobackend.sse.repositories;

import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.FluxSink;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SseSinkRepository {
    void addSink(UUID uuid, FluxSink<ServerSentEvent<String>> sink);
    void removeSink(UUID uuid);
    boolean hasSink(UUID uuid);
    FluxSink<ServerSentEvent<String>> getSink(UUID uuid);
    List<FluxSink<ServerSentEvent<String>>> getSinks();
    List<FluxSink<ServerSentEvent<String>>> getSinks(Collection<UUID> uuids);
}
