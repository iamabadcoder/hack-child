package com.hackx.spark;

import org.apache.spark.sql.SparkSession;


public class SparkDataFrameDemo {

    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSession.builder().appName("SparkDataFrameDemo")
                                         .master("local").getOrCreate();

        /*Dataset<Row> peopleDF = spark.read().json("src/main/resources/people.text");

        peopleDF.write().parquet("people.parquet");

        Dataset<Row> parquetFileDF = spark.read().parquet("people.parquet");

        parquetFileDF.createOrReplaceTempView("parquetFile");
        Dataset<Row> namesDF = spark.sql("SELECT name FROM parquetFile WHERE age BETWEEN 13 AND 19");
        Dataset<String> namesDS = namesDF.map(new MapFunction<Row, String>() {
            public String call(Row row) {
                return "Name: " + row.getString(0);
            }
        }, Encoders.STRING());

        namesDS.show();*/


        /*JavaRDD<Person> peopleRDD = sparkSession.read().textFile("src/main/java/com/hackx/spark/people.txt")
                                                .javaRDD().map(new Function<String, Person>() {
                    public Person call(String line) throws Exception {
                        String[] parts = line.split(",");
                        Person person = new Person();
                        person.setName(parts[0]);
                        person.setAge(Integer.parseInt(parts[1].trim()));
                        return person;
                    }
                });

        Dataset<Row> peopleDF = sparkSession.createDataFrame(peopleRDD, Person.class);*/

        /*SparkSession spark = SparkSession.builder().appName("SparkDataFrameDemo")
                                         .master("local").getOrCreate();
        Dataset<Row> df = spark.read().json("src/main/java/com/hackx/spark/people.json");
        df.createOrReplaceTempView("people");
        Dataset<Row> sqlDF = spark.sql("SELECT * FROM people");
        sqlDF.show();*/

    }
}
