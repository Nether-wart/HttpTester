import analyzer.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import utils.CustomDns;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    String name;
    String url;
    String forceIP;
    String method;
    String body;
    long interval;
    int threads;
    long times;
    Map<String,String> headers;

    transient final HttpStatusCounter counter=new HttpStatusCounter();
    transient final DelayAnalyzer delayAnalyzer=new DelayAnalyzer();
    transient final SpeedAnalyzer speedAnalyzer=new SpeedAnalyzer();

    public List<Runnable> getRunnableList(){
        List<Runnable> runnableList=new ArrayList<>();
        for (int threads=0;threads<this.threads;threads++){
            runnableList.add(newRunnable());
        }
        Show show=new Show(counter,delayAnalyzer,speedAnalyzer,threads);
        show.start();
        return runnableList;
    }

    private Runnable newRunnable(){
        return () -> {
            analyzer.Timer timer=new Timer();

            CustomDns dns=new CustomDns();
            if (forceIP!=null){
                dns.addMapping(URI.create(url).getHost(),forceIP);
            }

            OkHttpClient client=new OkHttpClient().newBuilder()
                    .dns(dns)
                    .followRedirects(true)
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();
            for (int i=0;i<times;i++) {
                timer.start();
                try (Response response = client.newCall(getRequest()).execute()) {
                    delayAnalyzer.increment(timer.stopAndGet());
                    counter.increment(response.code());

                    var in=response.body().byteStream();
                    try (in){
                        byte[] b=new byte[10240];
                        int bytesRead;
                        while ((bytesRead=in.read(b))!=-1){
                            speedAnalyzer.increment(bytesRead,timer.getAndReStart());
                        }
                    }

                }catch (Exception e){
                    counter.increment(-1);
                }
            }

            //show.cancel();
        };
    }

    public Request getRequest(){
        var builder=new Request.Builder()
                .url(url);

        if (body!=null){
            builder.method(method, RequestBody.create(body.getBytes(StandardCharsets.UTF_8)));
        }

        for (Map.Entry<String,String> entry:headers.entrySet()){
            builder.addHeader(entry.getKey(),entry.getValue());
        }

        return builder.build();
    }






    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setForceIP(String forceIP) {
        this.forceIP = forceIP;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public void setHeaders(Map<String,String> headers) {
        this.headers = headers;
    }
}
