package com.consistent.hash;

public class StatisticsData {

    public double sum;
    public double avg;
    public double standardDeviation;
    public double sqrtDeviation;

    @Override
    public String toString() {
        return "StatisticsData{" +
                "sum=" + sum +
                ", avg=" + avg +
                ", standardDeviation=" + standardDeviation +
                ", sqrtDeviation=" + sqrtDeviation +
                '}';
    }
}
