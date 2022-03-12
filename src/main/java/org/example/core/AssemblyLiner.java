package org.example.core;

import org.example.core.step.Step;

public interface AssemblyLiner extends Runnable {
     void addStep(Step step);
}
