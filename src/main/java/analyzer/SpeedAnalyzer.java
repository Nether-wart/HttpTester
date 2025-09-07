package analyzer;

import java.util.concurrent.atomic.LongAdder;

public class SpeedAnalyzer {
    LongAdder totalBytes=new LongAdder();
    LongAdder totalTime=new LongAdder();

    public void increment(long bytes,long time){
        totalBytes.add(bytes);
        totalTime.add(time);
    }

    public double getAvg(){
        return (double) totalBytes.longValue()/totalTime.longValue();
    }
}
