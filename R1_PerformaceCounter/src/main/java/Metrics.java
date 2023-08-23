import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Metrics {
    // Map的key是接口名称，value对应接口请求的响应时间或时间戳；
    private final Map<String, List<Double>> responseTimes = new HashMap<>();
    private final Map<String, List<Double>> timestamps = new HashMap<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    //记录接口响应时间
    public void recordResponseTime(String apiName, double responseTime) {
        responseTimes.putIfAbsent(apiName, new ArrayList<>());
        responseTimes.get(apiName).add(responseTime);
    }

    //记录接口的时间戳
    public void recordTimestamp(String apiName, double timestamp) {
        timestamps.putIfAbsent(apiName, new ArrayList<>());
        timestamps.get(apiName).add(timestamp);
    }

    //将统计结果以Json的格式输出到命令行
    public void startRepeatedReport(long period, TimeUnit unit) {
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //统计结果Map
                Map<String, Map<String, Double>> stats = new HashMap<>();

                for (Map.Entry<String, List<Double>> entry : responseTimes.entrySet()) {
                    String apiName = entry.getKey();
                    List<Double> apiRespTimes = entry.getValue();
                    stats.putIfAbsent(apiName, new HashMap<>());
                    stats.get(apiName).put("max", max(apiRespTimes));
                    stats.get(apiName).put("avg", avg(apiRespTimes));
                }

                for (Map.Entry<String, List<Double>> entry : timestamps.entrySet()) {
                    String apiName = entry.getKey();
                    List<Double> apiTimestamps = entry.getValue();
                    stats.putIfAbsent(apiName, new HashMap<>());
                    stats.get(apiName).put("count", (double) timestamps.size());
                }

                System.out.println(new Gson().toJson(stats));
            }
        }, 0, period, unit);
    }

    private double max(List<Double> dataset) {
        double maxValue = 0;
        for (Double value : dataset) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    private double avg(List<Double> dataset) {
        double sumValue = 0;
        for (Double value : dataset) {
            sumValue += value;
        }
        return sumValue / dataset.size();
    }
}













