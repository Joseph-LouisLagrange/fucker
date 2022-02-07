package org.example.core;

import java.util.concurrent.TimeUnit;

public class DefaultAssemblyLinerBuilder implements AssemblyLinerBuilder {

    private AssemblyLiner assemblyLiner;

    public DefaultAssemblyLinerBuilder(AssemblyLiner assemblyLiner){
        this.assemblyLiner = assemblyLiner;
    }

    public DefaultAssemblyLinerBuilder(){
        this(new SerializedAssemblyLiner());
    }

    @Override
    public AssemblyLinerBuilder next(Step step) {
        assemblyLiner.addStep(step);
        return this;
    }

    @Override
    public AssemblyLinerBuilder drinkCoffee(long time, TimeUnit unit) {
        assemblyLiner.addStep(new WaitStep(time,unit));
        return this;
    }

    @Override
    public AssemblyLiner build() {
        return assemblyLiner;
    }

}
