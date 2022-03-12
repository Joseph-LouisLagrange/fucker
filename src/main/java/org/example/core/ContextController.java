package org.example.core;

import org.example.core.env.Context;

public abstract class ContextController implements ContextAware,ContextLoader {
    private Context context;

    @Override
    public void contextLoader(Context context) {
        this.context = context;
    }

    @Override
    public Context getContext() {
        return context;
    }
}
