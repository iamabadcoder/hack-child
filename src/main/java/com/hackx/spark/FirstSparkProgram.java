package com.hackx.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class FirstSparkProgram {

    public static void main(String[] args) {

        String logFile = "/Users/caolei/WorkSpace/hack-child/README.md";
        SparkConf conf = new SparkConf().setMaster("local").setAppName("First Spark Program");
        JavaSparkContext jsc = new JavaSparkContext(conf);
        jsc.setLogLevel("OFF");
        JavaRDD<String> logRDD = jsc.textFile(logFile);
        JavaPairRDD<String, Integer> linePairRDD = logRDD.mapToPair(line -> new Tuple2(line, 1));
        JavaPairRDD<String, Integer> lineCountPairRDD = linePairRDD.reduceByKey((a, b) -> a + b);
        System.out.println(lineCountPairRDD.collectAsMap());




        /*JavaRDD<String> logData = jsc.textFile(logFile).cache();
        int wordsCount = logData.map(line -> line.split(" ").length).reduce((a, b) -> a + b);
        long numAs = logData.filter(line -> line.contains("a")).count();
        long numBs = logData.filter(line -> line.contains("b")).count();
        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs + ", total words:" + wordsCount);*/

        /*List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        JavaRDD<Integer> distData = jsc.parallelize(data, 2);
        long sum = distData.reduce((a, b) -> a + b);
        System.out.println("Ths adds result is :" + sum);
        System.out.println(distData.collect());*/


        jsc.stop();
    }
}
