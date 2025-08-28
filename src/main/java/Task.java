import analyzer.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    String name;
    String url;
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

            try(HttpClient client= HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build()){
                for (int i=0;i<times;i++){
                    try {
                        timer.start();
                        HttpResponse<InputStream> response=client.send(getRequest(),
                                HttpResponse.BodyHandlers.ofInputStream());

                        delayAnalyzer.increment(timer.stopAndGet());
                        counter.increment(response.statusCode());

                        timer.start();
                        var in=response.body();
                        try (in){
                            byte[] b=new byte[10240];
                            int bytesRead;
                            while ((bytesRead=in.read(b))!=-1){
                                speedAnalyzer.increment(bytesRead,timer.getAndReStart());
                            }
                        }

                    } catch (IOException | InterruptedException e) {
                        counter.increment(-1);
                    }
                }
            }
            //show.cancel();
        };
    }

    public HttpRequest getRequest(){
        var builder= HttpRequest.newBuilder()
                .uri(URI.create(url));

        if (body!=null){
            builder.method(method, HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8));
        }else {
            builder.method(method, HttpRequest.BodyPublishers.noBody());
        }

        for (Map.Entry<String,String> entry:headers.entrySet()){
            builder.header(entry.getKey(),entry.getValue());
        }

        return builder.build();
    }






    //setters
    public void setName(String name) {
        this.name = name;
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
