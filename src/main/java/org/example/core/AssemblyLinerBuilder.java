package org.example.core;


import java.util.concurrent.TimeUnit;

public interface AssemblyLinerBuilder {
    AssemblyLinerBuilder next(Step step);

    AssemblyLinerBuilder drinkCoffee(long time, TimeUnit unit);

    AssemblyLiner build();
}
