package com.lcvc.mr.wordcount2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


import java.io.FileOutputStream;
import java.io.IOException;

public class WordCountDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // 1.创建configuration类
        Configuration configuration = new Configuration();
        // 2.配置通信地址
        // configuration.set("fs.defaultFS", "hdfs://master:9000");
        // 3.配置MapReduce运行模型
        // configuration.set("mapreduce.framework.name", "yarn");  // 集群运行模式

        System.setProperty("HADOOP_USER_NAME", "root");
        configuration.set("mapreduce.framework.name", "local");  // 本地运行模式


        // 4.创建Job类
        Job job = Job.getInstance(configuration);

        /**
         *  job要关联所有的类
         */

        // 5.job关联主程序类
        job.setJarByClass(WordCountDriver.class);

        // 6.job关联Mapper类和Reduce
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        // 7.job设置输出类型
        job.setOutputKeyClass(Text.class);    // 设置输出值的键类型
        job.setOutputValueClass(IntWritable.class);  // 设置输出值的值类型


        // 8.设置输入输出路径
        FileInputFormat.setInputPaths(job, "src/main/resources/mytest");
        FileOutputFormat.setOutputPath(job, new Path("/output1234/"));

        // 9.提交任务
        boolean result = job.waitForCompletion(true);  // 成功返回ture， 失败返回false


        // 10.当任务结束时退出程序
        System.exit(result ? 0 : 1);  // 失败返回1
    }
}
