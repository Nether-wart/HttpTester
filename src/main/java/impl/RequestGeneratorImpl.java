package impl;

import impl.runnable.Context;
import impl.runnable.RunnableRequestImpl;

public interface RequestGeneratorImpl {
    RunnableRequestImpl newRunnableRequest(Manager manager, Context context, long i);
}
