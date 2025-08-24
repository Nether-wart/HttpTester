import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


    public List<Runnable> getRunnableList(){
        List<Runnable> runnableList=new ArrayList<>();
        for (int threads=0;threads<this.threads;threads++){
            runnableList.add(newRunnable());
        }
        return runnableList;
    }

    private Runnable newRunnable(){
        return () -> {
            try(HttpClient client= HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build()){
                for (int i=0;i<times;i++){
                    HttpResponse<InputStream> response=client.send(getRequest(),
                            HttpResponse.BodyHandlers.ofInputStream());
                    var in=response.body();
                    try (in){
                        byte[] b=new byte[1024];
                        while (in.read(b)!=-1){}
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        };
    }

    public HttpRequest getRequest(){
        var builder= HttpRequest.newBuilder()
                .uri(URI.create(url))
                .method(method, HttpRequest.BodyPublishers.noBody());

        for (Map.Entry<String,String> entry:headers.entrySet()){
            builder.header(entry.getKey(),entry.getValue());
        }

        return builder.build();
    }


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
