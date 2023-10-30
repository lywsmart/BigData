package com.lcvc.mr.dedup;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class DedupMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    private static Text field = new Text();
    protected void map(LongWritable key, Text value, Context context)throws IOException, InterruptedException{
        field = value;
        context.write(field, NullWritable.get());
    }
}
