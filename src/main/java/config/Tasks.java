package config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tasks {
    public String name;
    public String url;
    public String forceIP;
    public String method;
    public String body;
    public long interval;
    public int threads;
    public long times;
    public Map<String,String> headers;


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
