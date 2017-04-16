package com.hackx.spark.streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class SparkStreamDemo {

    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args) throws Exception {

        SparkConf sparkConf = new SparkConf().setMaster("local[2]").setAppName("JavaNetworkWordCount");
        JavaStreamingContext javaStreamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1L));
        javaStreamingContext.checkpoint("/Users/caolei/WorkSpace/hack-child/src/main/resources/checkpoint");


        List<Tuple2<String, Integer>> tuples = Arrays.asList(new Tuple2<>("hello", 1), new Tuple2<>("world", 1));
        JavaPairRDD<String, Integer> initialRDD = javaStreamingContext.sparkContext().parallelizePairs(tuples);

        JavaReceiverInputDStream<String> lines = javaStreamingContext.socketTextStream("localhost", 9999);
        JavaDStream<String> words = lines.flatMap(line -> Arrays.asList(SPACE.split(line)).iterator());
        JavaPairDStream<String, Integer> wordPairs = words.mapToPair(word -> new Tuple2<>(word, 1));

        Function2<Integer, Integer, Integer> reduceFunc = (Integer x1, Integer x2) -> x1 + x2;

        // Reduce last 30 seconds of data, every 10 seconds
        JavaPairDStream<String, Integer> windowedWordCounts = wordPairs.reduceByKeyAndWindow(reduceFunc, Durations.seconds(8), Durations.seconds(4));
        windowedWordCounts.print();

        /*JavaPairDStream<String, Integer> wordCounts = wordPairs.reduceByKey((x, y) -> x + y);
        wordCounts.print();*/

        javaStreamingContext.start();
        javaStreamingContext.awaitTermination();
    }
}
