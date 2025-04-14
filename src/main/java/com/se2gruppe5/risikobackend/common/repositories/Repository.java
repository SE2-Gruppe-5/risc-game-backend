package com.se2gruppe5.risikobackend.common.repositories;

public interface Repository<Key, Value> {
    void add(Key key, Value value);
    void remove(Key key);
    boolean has(Key key);
    Value get(Key key);
}
