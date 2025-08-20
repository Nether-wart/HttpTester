import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Throwable{
        ObjectMapper mapper=new ObjectMapper();
        Config config=mapper.readValue(new File("Tasks.json"),Config.class);

        HttpClient client= HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        for (Task task: config.tasks){
            HttpRequest request;
            if (task.body!=null){
                request=HttpRequest.newBuilder()
                        .method(task.getMethod(), HttpRequest.BodyPublishers.ofString(task.getBody()))
                        .uri(URI.create(task.getUrl()))
                        .build();
            }else {
                request=HttpRequest.newBuilder()
                        .method(task.getMethod(), HttpRequest.BodyPublishers.noBody())
                        .uri(URI.create(task.getUrl()))
                        .build();
            }
            HttpResponse<InputStream> response=client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            var in=response.body();
            try (in){
                byte[] b=new byte[1024];
                while (in.read(b)!=-1){}
            }
        }
    }
}
