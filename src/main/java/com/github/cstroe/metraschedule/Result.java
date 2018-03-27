package com.github.cstroe.metraschedule;

public class Result<V, E extends Exception> {
    private final V value;
    private final E error;

    public Result(V value) {
        this.value = value;
        this.error = null;
    }

    public Result(E error) {
        this.value = null;
        this.error = error;
    }

    public boolean hasValue() {
        return value != null;
    }

    public boolean hasError() {
        return error != null;
    }

    public V getValue() {
        if(value == null) {
            throw new RuntimeException("Cannot get value from an error result.");
        }
        return value;
    }

    public E getError() {
        if(error == null) {
            throw new RuntimeException("Cannot get error from a value result.");
        }
        return error;
    }
}
