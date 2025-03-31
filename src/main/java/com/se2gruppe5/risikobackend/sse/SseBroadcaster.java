package com.se2gruppe5.risikobackend.sse;

import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SseBroadcaster {
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ArrayList<FluxSink<String>> sinks = new ArrayList<>();

    public static void addSink(FluxSink<String> sink) {
        try {
            lock.writeLock().lock();
            sinks.add(sink);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void broadcast(String message) {
        try {
            lock.readLock().lock();
            for (FluxSink<String> sink : sinks) {
                sink.next(message);
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}
