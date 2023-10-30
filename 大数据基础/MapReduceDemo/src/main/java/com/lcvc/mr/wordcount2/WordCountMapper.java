package com.lcvc.mr.wordcount2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * public class Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
 * 原始数据
 *      KEYIN     LongWritable  文件地址   （Long类型在mapreduce的写法）
 *      VALUEIN   文件文本  TEXT    （String在mapreduce中的写法）
 *
 *  Map过程的输出数据
 *      KEYOUT
 *      VALUEOUT
 *
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    // 声明输出的变量的键和值
    Text keyOut = new Text();
    IntWritable valueOut = new IntWritable();

    // ALT + INSERT
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        /*
            key指的是输入键，对于原始数据是地址
            value指的是输入文本，按行输入
            context 上下文类，用于提交map过程的输出
         */

        // 1.Text转换成String
        String line = value.toString();    // value的读取是按行读取
        // 2.分割单词的方法  split(" ")
        String[] words = line.split(" ");

        // 3.map过程输出
        for (String word: words) {
            // 将文本值包装到Text类  Text.set(String)
            keyOut.set(word);
            // 将整型包装到IntWritable， IntWritable.set(Int)
            valueOut.set(1);
            // 提交输出，提交到Reduce过程
            context.write(keyOut, valueOut);
        }
    }
}
