package com.lcvc.mr.wordcountonline;



import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WordCountOnlineDriver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 1.获取配置信息以及获取Job对象
        Configuration conf = new Configuration();
        // 添加hdfs通信地址
        conf.set("fs.defaultFS","hdfs://master:9000");
        //配置MR运行模式，使用yarn作为集群模式
        conf.set("mapreduce.framework.name","yarn");
        //指定MR可以在远程集群运行
        conf.set("mapreduce.app-submission.cross-platform", "true");
        //指定yarn resourceManager的位置
        conf.set("yarn.resourcemanager.hostname", "master");
        // 获取Job对象
        Job job = Job.getInstance(conf);

        // 2.关联本Driver程序的Jar
        job.setJarByClass(WordCountOnlineDriver.class);

        // 3.关联Mapper的Jar和Reduce的Jar
        job.setMapperClass(WordCountOnlineMapper.class);
        job.setReducerClass(WordCountOnlineReducer.class);

        // 4.设置Mapper的输出的key和Value的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 5.设置最终输出的key和value的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 6.设置输入和输出路径

        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 7.提交任务
        boolean result = job.waitForCompletion(true);

        // 自动退出任务
        System.exit(result ? 0 : 1); //0表示正常退出，其他值表示非正常退出。
    }

}
