import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {
    List<Task> tasks;

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
