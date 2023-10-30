package com.lcvc.mr.salecount;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class SaleCountDriver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        // 1.获取Configuration
        Configuration configuration = new Configuration();
        // 2.获取Job对象
        Job job = Job.getInstance(configuration);

        // 3.关联主程序
        job.setJarByClass(SaleCountDriver.class);

        // 4.关联Mapper和Reduce
        job.setMapperClass(SaleCountMapper.class);
        job.setReducerClass(SaleCountReducer.class);

        // 5.设置输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        // 6.设置输出路径
        FileInputFormat.setInputPaths(job,"src/main/resources/sale");
        FileOutputFormat.setOutputPath(job,new Path("/outputsales"));

        // 7.提交任务
        boolean result = job.waitForCompletion(true);

        // 退出
        System.exit(result ? 0:1);
    }
}
