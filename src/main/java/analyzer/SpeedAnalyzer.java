package analyzer;

import java.util.concurrent.atomic.LongAdder;

public class SpeedAnalyzer {
    LongAdder totalBytes=new LongAdder();
    LongAdder totalTime=new LongAdder();

    public void increment(long bytes,long time){
        totalBytes.add(bytes);
        totalTime.add(time);
    }

    public long getAvg(){
        return totalBytes.longValue()/totalTime.longValue();
    }
}
