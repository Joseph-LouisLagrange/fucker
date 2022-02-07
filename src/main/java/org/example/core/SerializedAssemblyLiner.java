package org.example.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 串行化实现，着重在 Step 与 Step 之间的顺序
 */
public class SerializedAssemblyLiner implements AssemblyLiner {
    List<Step> steps = new ArrayList<>();
    @Override
    public void addStep(Step step) {
        steps.add(step);
    }

    @Override
    public void run() {
        for (Step step : steps) {
            step.run();
        }
    }
}
