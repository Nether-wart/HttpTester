package analyzer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * 线程安全的HTTP状态码统计器
 * 使用Java 21的并发特性实现高效的状态码计数
 */
public class HttpStatusCounter {
    // 使用ConcurrentHashMap保证线程安全，LongAdder用于高性能计数
    private final ConcurrentHashMap<Integer, LongAdder> counterMap = new ConcurrentHashMap<>();

    /**
     * 增加指定状态码的计数（线程安全）
     * @param statusCode HTTP状态码
     */
    public void increment(int statusCode) {
        // 如果状态码不存在，则初始化一个LongAdder计数器
        // 然后增加计数（原子操作）
        counterMap.computeIfAbsent(statusCode, k -> new LongAdder()).increment();
    }

    /**
     * 获取当前状态码统计结果的快照（线程安全）
     * @return 包含状态码和对应计数的Map
     */
    public Map<Integer, Long> getStats() {
        // 使用流API创建统计快照，避免直接暴露内部数据结构
        return counterMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().longValue()
                ));
    }

    /**
     * 重置所有统计计数（线程安全）
     */
    public void reset() {
        counterMap.clear();
    }

    /**
     * 获取特定状态码的计数
     * @param statusCode HTTP状态码
     * @return 该状态码的出现次数，如果未出现过则返回0
     */
    public long getCount(int statusCode) {
        LongAdder adder = counterMap.get(statusCode);
        return adder == null ? 0 : adder.longValue();
    }

    /**
     * 获取总请求数（线程安全）
     * @return 所有状态码的计数总和
     */
    public long getTotalRequests() {
        return counterMap.values()
                .stream()
                .mapToLong(LongAdder::longValue)
                .sum();
    }
}