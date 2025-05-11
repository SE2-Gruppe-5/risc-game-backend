package com.se2gruppe5.risikobackend.sse.repositories;

import com.se2gruppe5.risikobackend.common.repositories.Repository;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.FluxSink;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SseSinkRepository extends Repository<UUID, FluxSink<ServerSentEvent<String>>> {
    List<FluxSink<ServerSentEvent<String>>> getSinks();
    List<FluxSink<ServerSentEvent<String>>> getSinks(Collection<UUID> uuids);

    default void addSink(UUID uuid, FluxSink<ServerSentEvent<String>> sink) {
        add(uuid, sink);
    }

    default void removeSink(UUID uuid) {
        remove(uuid);
    }

    default boolean hasSink(UUID uuid) {
        return has(uuid);
    }

    default FluxSink<ServerSentEvent<String>> getSink(UUID uuid) {
        return get(uuid);
    }
}
