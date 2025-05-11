package com.se2gruppe5.risikobackend.common.repositories;

public interface Repository<K, V> {
    void add(K key, V value);
    void remove(K key);
    boolean has(K key);
    V get(K key);
}
