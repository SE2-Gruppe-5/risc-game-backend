package com.se2gruppe5.risikobackend.common.repositories;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractRepository<Key, Value> implements Repository<Key, Value> {
    protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected final HashMap<Key, Value> map = new HashMap<>();

    @Override
    public void add(Key key, Value value) {
        try {
            lock.writeLock().lock();
            map.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void remove(Key key) {
        try {
            lock.writeLock().lock();
            map.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean has(Key key) {
        try {
            lock.readLock().lock();
            return map.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Value get(Key key) {
        try {
            lock.readLock().lock();
            Value sink = map.get(key);
            if (sink != null) {
                return sink;
            }
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }
}
