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

    public boolean countdown(int counter){
        while(counter >= 0){
           counter--;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                CompletableFuture.delayedExecutor(1, TimeUnit.SECONDS).execute(() -> {

                });
            }
        }
        return false;
    }
}
