package impl.defaults;

import analyzer.Timer;
import impl.Manager;
import impl.RequestGeneratorImpl;
import okhttp3.Response;
import impl.runnable.Context;
import impl.runnable.RunnableRequestImpl;

public class RequestGenerator implements RequestGeneratorImpl {
    private final int buffer=8192;
    @Override
    public RunnableRequestImpl newRunnableRequest(Manager manager, Context context, long i) {

        return (c)->{
            Timer timer=new Timer();
            timer.start();

            try (Response response=c.client().newCall(
                    manager.replacer().newRequest(manager,context.tasks(),i)).execute()){
                c.delayAnalyzer().increment(timer.getAndReStart());
                c.counter().increment(response.code());

                if (response.body()!=null){
                    try (var in=response.body().byteStream()){
                        while (true){
                            timer.start();
                            int bytesRead=in.readNBytes(buffer).length;
                            if (bytesRead==0)break;
                            c.speedAnalyzer().increment(bytesRead,timer.stopAndGet());
                        }

                    }
                }
            }
        };
    }
}
