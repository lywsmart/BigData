package com.lcvc.mr.salecount;


import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SaleCountMapper extends Mapper<LongWritable, Text, Text, FloatWritable> {

    // KeyOut
    Text keyOut = new Text();
    // ValueOut
    FloatWritable valueOut = new FloatWritable();

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, FloatWritable>.Context context) throws IOException, InterruptedException {
        // 分割语义
        // 商品类,销售量,单价

        String[] temp = value.toString().split(",");

        if (temp.length == 3 && isContainerNull(temp)) {
            // 商品类
            String goodType = temp[0];
            // 销售量
            float volume = Float.parseFloat(temp[1]);
            // 单价
            float unitPrice = Float.parseFloat(temp[2]);
            // 销售额
            float sales = volume * unitPrice;

            // 设置KeyOut, ValueOut
            keyOut.set(goodType);
            valueOut.set(sales);

            // 提交输出
            context.write(keyOut, valueOut);
        }
    }

    /**
     * 判断数组是否包含null
     * @return  true or false
     */
    public boolean isContainerNull(String[] temp) {
        for (String s: temp) {
            if (s.isEmpty()){
                return false;
            }
        }
        return true;    // 遍历后不包含null时返回true
    }
}



