package com.lcvc.mr.invertedlndex;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import java.io.IOException;
public class InvertedlndexMapper extends Mapper<LongWritable, Text, Text, Text> {
    private static Text keyInfo = new Text();
    private static final Text valueInfo = new Text("1");
    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException{
        String line = value.toString();
        String[] fields = StringUtils.split(line," ");
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        String fileName = fileSplit.getPath().getName();
        for (String field : fields){
            keyInfo.set(field + ":" + fileName);
            context.write(keyInfo, valueInfo);
        }
    }
}
