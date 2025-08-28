package analyzer;

public class Timer {
    long startTime=0;

    public void start(){
        startTime=System.currentTimeMillis();
    }

    public long getAndReStart(){
        long time=get();
        start();
        return time;
    }

    public long get(){
        return System.currentTimeMillis()-startTime;
    }

    public long stopAndGet(){
        if (startTime==0){
            throw new RuntimeException("Not started");
        }

        long time=System.currentTimeMillis()-startTime;
        startTime=0;
        return time;
    }
}
