package com.github.dreambrother.jpjq.matcher;

import org.hamcrest.Matcher;
import org.mockito.internal.matchers.GreaterThan;

public class MatcherUtils {

    private MatcherUtils() {}

    public static <T extends Comparable<T>> Matcher<T> greaterThan(T value) {
        return new GreaterThan<>(value);
    }
}
