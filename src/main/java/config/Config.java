package config;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
public class Config {
    @JsonAlias("list")
    public List<Tasks> tasksList;

    public void setTasksList(List<Tasks> tasksList) {
        this.tasksList = tasksList;
    }
}
