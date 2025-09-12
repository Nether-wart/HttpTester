package impl.defaults;

import analyzer.Timer;
import impl.Manager;
import impl.runnable.Context;
import impl.runnable.RunnableTaskImpl;

public class RunnableTask implements RunnableTaskImpl {
    @Override
    public void run(Manager manager, Context context) throws InterruptedException {
        try {
            context.semaphore().acquire();
            Timer timer=new Timer();

            for (int i = 0; i < context.tasks().times; i++) {
                timer.start();
                try {
                    manager.requestGenerator().newRunnableRequest(manager,context,i).run(context);
                }catch (Exception e){
                    context.counter().increment(-1);
                }
                long time= timer.stopAndGet();
                if (time<context.tasks().interval){
                    //noinspection BusyWait
                    Thread.sleep(context.tasks().interval-time);
                }
            }
        }finally {
            context.semaphore().release();
        }
    }
}
