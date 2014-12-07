package hadoop.meetup.first;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class StateReputationReducer extends
		Reducer<Text,IntWritable,Text,IntWritable> {

	@Override
	protected void reduce(Text state, Iterable<IntWritable> reputations,
			Context context)
			throws IOException, InterruptedException {
		int sum=0;

	}

}
