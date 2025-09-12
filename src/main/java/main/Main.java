package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.Config;
import impl.Manager;
import impl.defaults.Replacer;
import impl.defaults.RequestGenerator;
import impl.defaults.RunnableTask;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Throwable{
        ObjectMapper mapper=new ObjectMapper();
        var config=mapper.readValue(new File("Config.json"), Config.class);

        Manager manager=new Manager(new RequestGenerator(),new Replacer());
        Application.run(config.tasksList.getFirst(),new RunnableTask(),manager).join();

    }
}
