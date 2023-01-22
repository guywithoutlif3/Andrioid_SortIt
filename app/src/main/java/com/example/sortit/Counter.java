package com.example.sortit;

import android.os.Build;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Counter {
    private int counter;

    public Counter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }


}
