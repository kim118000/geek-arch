import com.consistent.hash.*;

import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        String val = "a";

        HashStrategy hashStrategy = new JdkHashStrategy();
        //HashStrategy hashStrategy = new Fnv1HashStrategy();
        //HashStrategy hashStrategy = new MurmurHashStrategy();
        //HashStrategy hashStrategy = new Md5HashStrategy();

        ConsistentHash consistentHash = new ConsistentHash(0, hashStrategy);


        for (int i = 1; i <= 1000000; i++) {
            String uuid = UUID.randomUUID().toString();
            consistentHash.put(uuid, val);
        }

        System.out.println(consistentHash.standardDeviation());

        //采用 JdkHashStrategy

        //10个物理节点
        //StatisticsData{sum=1000000.0, avg=100000.0, standardDeviation=200198.80094945623, sqrtDeviation=4.00795599016E11}

        //10个物理节点 1000个虚拟节点
        //StatisticsData{sum=1000000.0, avg=100000.0, standardDeviation=124276.97744634765, sqrtDeviation=1.54447671232E11}

        //10个物理节点 2000个虚拟节点
        //StatisticsData{sum=1000000.0, avg=100000.0, standardDeviation=91193.09627159285, sqrtDeviation=8.3161808076E10}


        //不同的hash算法=========================

        //采用 Fnv1HashStrategy
        //10个物理节点 2000个虚拟节点
        //StatisticsData{sum=1000000.0, avg=100000.0, standardDeviation=5058.339154307469, sqrtDeviation=2.5586795E8}

        //采用 MurmurHashStrategy
        //10个物理节点 2000个虚拟节点
        //StatisticsData{sum=1000000.0, avg=100000.0, standardDeviation=9644.840496348294, sqrtDeviation=9.30229482E8}

        //采用 Md5HashStrategy
        //10个物理节点 2000个虚拟节点
        //StatisticsData{sum=1000000.0, avg=100000.0, standardDeviation=8029.238170586298, sqrtDeviation=6.44686656E8}
    }
}
