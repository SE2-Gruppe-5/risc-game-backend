package com.se2gruppe5.risikobackend.sse.repositories;

import com.se2gruppe5.risikobackend.common.repositories.AbstractRepository;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.FluxSink;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class SseSinkRepositoryImpl extends AbstractRepository<UUID, FluxSink<ServerSentEvent<String>>> implements SseSinkRepository {
    @Override
    public List<FluxSink<ServerSentEvent<String>>> getSinks() {
        try {
            lock.readLock().lock();
            return super.map.values().stream().toList();
        } finally {
            lock.readLock().unlock();
        }
    }


    @Override
    public List<FluxSink<ServerSentEvent<String>>> getSinks(Collection<UUID> uuids) {
        try {
            lock.readLock().lock();
            return super.map.entrySet().stream()
                    .filter(entry -> uuids.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .toList();
        } finally {
            lock.readLock().unlock();
        }
    }
}
