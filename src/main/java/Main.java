import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws Throwable{
        ObjectMapper mapper=new ObjectMapper();
        var config=mapper.readValue(new File("Config.json"),Config.class);
        try (var executors= Executors.newVirtualThreadPerTaskExecutor()){
            for (Task task:config.tasks){
                for (Runnable runnable: task.getRunnableList()){
                    executors.submit(runnable);
                }
            }
        }
    }
}
