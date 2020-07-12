import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    static int reqeustCount = 1000;
    static int threadCount = 10;
    static ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

    /*
    平均响应时间160
60%响应时间55.0
70%响应时间57.0
75%响应时间58.0
80%响应时间59.0
85%响应时间61.0
90%响应时间66.0
95%响应时间1092.0499999999993
     */

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Task> tasks = new ArrayList<>();

        for (int i = 1; i <= threadCount; i++){
            tasks.add(new Task(reqeustCount));
        }

        List<Future<long[]>> futures = executorService.invokeAll(tasks);

        long[] res = new long[reqeustCount * threadCount];
        for (int i = 0; i < futures.size(); i++){
            System.arraycopy(futures.get(i).get(), 0, res, i * reqeustCount, reqeustCount);
        }

        long sum = Arrays.stream(res).sum();
        System.out.println("平均响应时间" + sum/(threadCount * reqeustCount));


        Arrays.sort(res);
        System.out.println("60%响应时间" + getPercentile(res, 0.60));
        System.out.println("70%响应时间" + getPercentile(res, 0.70));
        System.out.println("75%响应时间" + getPercentile(res, 0.75));
        System.out.println("80%响应时间" + getPercentile(res, 0.80));
        System.out.println("85%响应时间" + getPercentile(res, 0.85));
        System.out.println("90%响应时间" + getPercentile(res, 0.90));
        System.out.println("95%响应时间" + getPercentile(res, 0.95));

        executorService.shutdown();

    }

    private static double getPercentile(long[] array, double percentile) {
        double x = (array.length - 1) * percentile;
        int i = (int) x;
        double j = x - i;
        return (1 - j) * array[i] + j * array[i + 1];
    }

}

class Task implements Callable<long[]> {
    private final int reqeustCount;
    private final static String url = "http://wwww.baidu.com";

    public Task(int reqeustCount){
        this.reqeustCount = reqeustCount;
    }


    @Override
    public long[] call() throws Exception {
        long[] res = new long[reqeustCount];

        for (int i = 1; i <= reqeustCount; i++){
            long startTime = System.currentTimeMillis();

            try{
                URL httpUrl = new URL(url);
                URLConnection urlConnection = httpUrl.openConnection();
                urlConnection.connect();
            }catch (Exception ex){

            }

            long endTime = System.currentTimeMillis();
            res[i - 1] = endTime - startTime;

            System.out.println("线程" + Thread.currentThread().getName() + "第" + i + "请求耗时" + res[i - 1] + "ms");

        }

        return res;
    }
}
