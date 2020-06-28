package com.consistent.hash;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHash {

    //虚拟节点
    private final SortedMap<Integer, Machine> shards = new TreeMap<>();

    // 真实节点列表
    private final List<Machine> realNodes = new ArrayList<>();

    //服务器ip列表
    private static final String[] servers = {
            "192.168.1.1",
            "192.168.1.2",
            "192.168.1.3",
            "192.168.1.4",
            "192.168.1.5",
            "192.168.1.6",
            "192.168.1.7",
            "192.168.1.8",
            "192.168.1.9",
            "192.168.1.10"
    };

    //hash算法
    private final HashStrategy hashStrategy;

    public ConsistentHash(int virtual_num, HashStrategy hashStrategy) {
        this.hashStrategy = hashStrategy;

        for (String server : servers) {
            realNodes.add(new Machine(server));
        }

        for (Machine node : realNodes) {
            String server = node.getHost();
            String virtualNode = server + "#id_" + 0;
            int hash = hashStrategy.getHash(virtualNode);
            shards.put(hash, node);
        }

        for (Machine node : realNodes) {
            for (int i = 0; i < virtual_num; i++) {
                String server = node.getHost();
                String virtualNode = server + "#id_" + i;
                int hash = hashStrategy.getHash(virtualNode);
                shards.put(hash, node);
            }
        }
    }


    private Machine getMachine(String key) {
        int hash = hashStrategy.getHash(key);

        SortedMap<Integer, Machine> subMachine = shards.tailMap(hash);

        if (subMachine.isEmpty()) {
            return shards.get(shards.lastKey());
        }

        return shards.get(subMachine.firstKey());

    }

    public void put(String key, String value) {
        Machine machine = this.getMachine(key);

        System.out.println("[" + key + "]被路由到结点[" + machine.getHost() + "]");

        machine.add(key, value);
    }

    public StatisticsData standardDeviation() {
        int m = realNodes.size();
        double sum = 0;

        for (int i = 0; i < m; i++) {//求和
            sum += realNodes.get(i).getValueSize();
        }

        double avg = sum / m;//求平均值
        double dVar = 0;

        for (int i = 0; i < m; i++) {//求方差
            dVar += (realNodes.get(i).getValueSize() - avg) * (realNodes.get(i).getValueSize() - avg);
        }

        StatisticsData statisticsData = new StatisticsData();
        statisticsData.sum = sum;
        statisticsData.avg = avg;
        statisticsData.sqrtDeviation = dVar;
        statisticsData.standardDeviation = Math.sqrt(dVar / m);

        return statisticsData;
    }

}
