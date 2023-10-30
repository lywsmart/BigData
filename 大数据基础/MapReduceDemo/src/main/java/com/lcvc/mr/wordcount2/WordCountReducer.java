package com.lcvc.mr.wordcount2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    /**
     * Reduce过程的输入值是Map过程的输出值，一定要一致
     * 直接去复制Mapper类的KeyOut和ValueOut作为Reducer的KeyIn和ValueIn
     */

    // 创建属性
    // KeyOut
    Text keyOut = new Text();
    // ValueOut
    IntWritable valueOut = new IntWritable();


    // ALT+INSERT

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        // Iterable<IntWritable> 是一个整型迭代器（整型数组）
        // Map阶段 (Hello,1)   (Hello,2)  (Hello,1)  ----》   (Hello,  [1,2,1])

        int sum = 0;

        for (IntWritable value: values) {
            sum += value.get();
        }

        // 设置输出值
        keyOut.set(key);
        valueOut.set(sum);

        // 通过context类提交输出
        context.write(keyOut, valueOut);

    }
}
