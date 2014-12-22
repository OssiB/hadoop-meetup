package hadoop.meetup.first;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ReputationMapper extends Mapper<LongWritable,Text,IntWritable,Text> {
	@Override
	protected void map(LongWritable key, Text value,
			Context context)
			throws IOException, InterruptedException {
		String[] data = value.toString().split(";");
		context.write(new IntWritable(new Integer(data[2])),value);
	}
}
