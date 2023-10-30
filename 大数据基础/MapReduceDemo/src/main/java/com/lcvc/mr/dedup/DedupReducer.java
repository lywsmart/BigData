package com.lcvc.mr.dedup;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
public class DedupReducer extends Reducer<Text, NullWritable, Text, NullWritable> {
    protected void reduce(Text key, Iterable<NullWritable>values, Context context)throws IOException,
            InterruptedException{
        context.write(key, NullWritable.get());
    }
}
