package main;

import analyzer.DelayAnalyzer;
import analyzer.HttpStatusCounter;
import analyzer.SpeedAnalyzer;
import config.Tasks;
import impl.Manager;
import okhttp3.Dns;
import okhttp3.OkHttpClient;
import impl.runnable.Context;
import impl.runnable.RunnableTaskImpl;
import utils.CustomDns;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.Semaphore;

public class Application {
    public static Context run(Tasks tasksConfig, RunnableTaskImpl runnableTask, Manager manager){
        OkHttpClient client=new OkHttpClient().newBuilder()
                .dns(getDns(tasksConfig))
                .followRedirects(true)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpStatusCounter counter=new HttpStatusCounter();
        DelayAnalyzer delayAnalyzer=new DelayAnalyzer();
        SpeedAnalyzer speedAnalyzer=new SpeedAnalyzer();

        Semaphore semaphore=new Semaphore(tasksConfig.threads);
        Context context=new Context(tasksConfig,client,
                counter,delayAnalyzer,speedAnalyzer,
                semaphore);

        for (int i = 0; i < tasksConfig.threads; i++) {
            Thread.ofPlatform().start(()-> {
                try {
                    runnableTask.run(manager,context);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return context;
    }
    private static Dns getDns(Tasks tasksConfig){
        CustomDns dns=new CustomDns();
        if (tasksConfig.forceIP!=null){
            dns.addMapping(URI.create(tasksConfig.url).getHost(),tasksConfig.forceIP);
        }
        return dns;
    }
}
