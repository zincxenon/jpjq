package com.github.dreambrother.jpjq.generator;

public interface ValueGenerator<T, R> {

    R generate(T obj);
}
