package impl.runnable;

import impl.Manager;

public interface RunnableTaskImpl {
    void run(Manager manager, Context context)throws InterruptedException;
}
