package com.hackx.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.regex.Pattern;

public class SparkStreamDemo {

    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args) throws Exception {

        SparkConf sparkConf = new SparkConf().setAppName("JavaNetworkWordCount").setMaster("local[5]");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(5));
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 9999);

        JavaDStream<String> words = lines.flatMap(line -> Arrays.asList(SPACE.split(line)).iterator());
        JavaPairDStream<String, Integer> wordPairs = words.mapToPair(word -> new Tuple2<>(word, 1));
//        JavaPairDStream<String, Integer> wordCounts = wordPairs.reduceByKey((x, y) -> x + y);
        // Reduce function adding two integers, defined separately for clarity
        Function2<Integer, Integer, Integer> reduceFunc = new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer i1, Integer i2) {
                return i1 + i2;
            }
        };
        // Reduce last 30 seconds of data, every 10 seconds
        JavaPairDStream<String, Integer> windowedWordCounts = wordPairs.reduceByKeyAndWindow(reduceFunc, Durations.seconds(10), Durations.seconds(5));
        windowedWordCounts.print();

        jssc.start();
        jssc.awaitTermination();
    }
}
