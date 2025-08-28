package analyzer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class Show {
    HttpStatusCounter counter;
    DelayAnalyzer delayAnalyzer;
    SpeedAnalyzer speedAnalyzer;
    int threads;
    Timer timer=new Timer();

    public Show(HttpStatusCounter counter, DelayAnalyzer delayAnalyzer, SpeedAnalyzer speedAnalyzer,int threads) {
        this.counter = counter;
        this.delayAnalyzer = delayAnalyzer;
        this.speedAnalyzer = speedAnalyzer;
        this.threads=threads;
    }

    public void start(){
        TimerTask task=new TimerTask() {
            @Override
            public void run() {
                showHttpStatusCounter(5);
                showDelay();
                showSpeed();
                System.out.println();
            }
        };
        timer.schedule(task,10000,10000);
    }

    public void cancel(){
        timer.cancel();
    }

    private void showHttpStatusCounter(int limit){
        var sortedMap = counter.getStats().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1, // 合并函数（避免键冲突）
                        LinkedHashMap::new
                ));
        System.out.println("HTTP状态码:");
        for (Map.Entry<Integer,Long> entry:sortedMap.entrySet()){
            System.out.printf("%d :%d\n",entry.getKey(),entry.getValue());
        }
    }

    private void showDelay(){
        System.out.println("响应时间(min/max/avg)");
        System.out.printf("%d,%d,%d\n",delayAnalyzer.getMin(),delayAnalyzer.getMax(),delayAnalyzer.getAvg());
    }

    private void showSpeed(){
        System.out.printf("下载速度:%.2f MB/S\n",threads*speedAnalyzer.getAvg()*1000D/1024/1024);
    }
}
