package com.hackx.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;

public class GroupByTest {

    public static void main(String[] args) {
        int numMappers = 100, numReducers = 36, numKVPairs = 10000, valSize = 1000;
        Integer[] initArr = new Integer[numMappers];
        for (int i = 0; i < numMappers; i++) {
            initArr[i] = i;
        }
        SparkConf sparkConf = new SparkConf().setMaster("local[2]").setAppName("GroupByTest");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        JavaRDD<Integer> initRDD = jsc.parallelize(Arrays.asList(initArr), 2);
//        JavaPairRDD<Integer, Integer> pairRDD = initRDD.flatMap()


    }
}
