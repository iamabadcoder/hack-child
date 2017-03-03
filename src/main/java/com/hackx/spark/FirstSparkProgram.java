package com.hackx.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class FirstSparkProgram {

    public static void main(String[] args) {

        String logFile = "/Users/caolei/WorkSpace/hack-child/README.md";
        SparkConf conf = new SparkConf().setMaster("local").setAppName("First Spark Program");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.setLogLevel("ERROR");

        JavaRDD<String> logData = jsc.textFile(logFile).cache();
        long numAs = logData.filter(line -> line.contains("a")).count();
        long numBs = logData.filter(line -> line.contains("b")).count();

        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
        jsc.stop();
    }
}
