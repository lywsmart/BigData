package com.lcvc.mr.salecount;


import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SaleCountReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {
    // 设置keyOut，valueOut
    Text keyOut = new Text();
    FloatWritable valueOut = new FloatWritable();
    float sum;

    @Override
    protected void reduce(Text key, Iterable<FloatWritable> values, Reducer<Text, FloatWritable, Text, FloatWritable>.Context context) throws IOException, InterruptedException {
        // 每次清空
        sum = 0;

        // 遍历相加
        for (FloatWritable value: values) {
            sum += value.get();
        }
        // 设置输出
        keyOut.set(key);
        valueOut.set(sum);

        // 提交输出
        context.write(keyOut, valueOut);
    }
}
