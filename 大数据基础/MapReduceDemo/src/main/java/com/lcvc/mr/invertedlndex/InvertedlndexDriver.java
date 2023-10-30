package com.lcvc.mr.invertedlndex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class InvertedlndexDriver {
    public static void main(String[] args)
            throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("mapreduce.framework.name","yarn");
        Job job = Job.getInstance(conf);
        job.setJarByClass(InvertedlndexDriver.class);
        job.setMapperClass(InvertedlndexMapper.class);
        job.setCombinerClass(InvertedlndexCombiner.class);
        job.setReducerClass(InvertedlndexReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
