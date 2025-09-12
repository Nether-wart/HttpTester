package impl.runnable;

import analyzer.DelayAnalyzer;
import analyzer.HttpStatusCounter;
import analyzer.SpeedAnalyzer;
import config.Tasks;
import okhttp3.OkHttpClient;

import java.util.concurrent.Semaphore;

public record Context(Tasks tasks, OkHttpClient client,
                      HttpStatusCounter counter, DelayAnalyzer delayAnalyzer, SpeedAnalyzer speedAnalyzer,
                      Semaphore semaphore) {
    public synchronized void join()throws InterruptedException{
        try {
            semaphore.acquire(tasks.threads);
        }finally {
            semaphore.release(tasks.threads);
        }
    }
}