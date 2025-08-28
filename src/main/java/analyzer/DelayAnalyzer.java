package analyzer;

import java.util.concurrent.atomic.LongAdder;

public class DelayAnalyzer {
    private final LongAdder total=new LongAdder();
    private final LongAdder times=new LongAdder();

    private long max=Integer.MIN_VALUE;
    private long min=Integer.MAX_VALUE;

    public void increment(long delay){
        total.add(delay);
        times.increment();

        if (delay>max){
            max=delay;
        } else if (delay<min) {
            min=delay;
        }
    }


    public long getTimes() {
        return times.longValue();
    }

    public long getTotal() {
        return total.longValue();
    }

    public long getMax() {
        return max;
    }

    public long getMin() {
        return min;
    }

    public int getAvg() {
        return (int) (getTotal() / getTimes());
    }
}
