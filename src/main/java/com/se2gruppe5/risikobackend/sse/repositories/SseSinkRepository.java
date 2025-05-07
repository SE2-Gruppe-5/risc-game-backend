package com.se2gruppe5.risikobackend.sse.repositories;

import reactor.core.publisher.FluxSink;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SseSinkRepository {
    void addSink(UUID uuid, FluxSink<String> sink);
    void removeSink(UUID uuid);
    boolean hasSink(UUID uuid);
    FluxSink<String> getSink(UUID uuid);
    List<FluxSink<String>> getSinks();
    List<FluxSink<String>> getSinks(Collection<UUID> uuids);
}
