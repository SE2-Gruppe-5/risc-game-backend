package com.se2gruppe5.risikobackend.sse.repositories;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.FluxSink;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Repository
public class SseSinkRepositoryImpl implements SseSinkRepository {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final HashMap<UUID, FluxSink<ServerSentEvent<String>>> sinks = new HashMap<>();

    @Override
    public void addSink(UUID uuid, FluxSink<ServerSentEvent<String>> sink) {
        try {
            lock.writeLock().lock();
            sinks.put(uuid, sink);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeSink(UUID uuid) {
        try {
            lock.writeLock().lock();
            sinks.remove(uuid);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean hasSink(UUID uuid) {
        try {
            lock.readLock().lock();
            return sinks.containsKey(uuid);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public FluxSink<ServerSentEvent<String>> getSink(UUID uuid) {
        try {
            lock.readLock().lock();
            FluxSink<ServerSentEvent<String>> sink = sinks.get(uuid);
            if (sink != null) {
                return sink;
            }
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }

    @Override
    public List<FluxSink<ServerSentEvent<String>>> getSinks() {
        try {
            lock.readLock().lock();
            return sinks.values().stream().toList();
        } finally {
            lock.readLock().unlock();
        }
    }


    @Override
    public List<FluxSink<ServerSentEvent<String>>> getSinks(Collection<UUID> uuids) {
        try {
            lock.readLock().lock();
            return sinks.entrySet().stream()
                    .filter(entry -> uuids.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .toList();
        } finally {
            lock.readLock().unlock();
        }
    }
}
