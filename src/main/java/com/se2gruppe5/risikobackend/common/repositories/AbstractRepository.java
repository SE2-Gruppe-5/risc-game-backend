package com.se2gruppe5.risikobackend.common.repositories;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractRepository<K, V> implements Repository<K, V> {
    protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected final HashMap<K, V> map = new HashMap<>();

    @Override
    public void add(K key, V value) {
        try {
            lock.writeLock().lock();
            map.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void remove(K key) {
        try {
            lock.writeLock().lock();
            map.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean has(K key) {
        try {
            lock.readLock().lock();
            return map.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V get(K key) {
        try {
            lock.readLock().lock();
            V sink = map.get(key);
            if (sink != null) {
                return sink;
            }
        } finally {
            lock.readLock().unlock();
        }
        return null;
    }
}
