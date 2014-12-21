package hadoop.meetup.first;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class StateReputationMapper extends
		Mapper<LongWritable, Text, Text, IntWritable> {

	enum Reputation {
		HEADER, NOT_IN_STATES
	}

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		List<String> states = Arrays.asList("AZ", "FL", "CA", "IL", "IN", "MD",
				"MA", "GA", "MI", "MO", "NC", "NJ", "NY", "OH", "PA", "TN",
				"TX", "VA", "WA", "WI");
		String line = value.toString();
		StackExchangeRecord record = new StackExchangeRecord(line);
		String parsedState = record.getState();
		if (states.contains(parsedState)) {
			context.write(new Text(parsedState), new IntWritable(new Integer(
					record.getReputation())));

		} else if (record.isHeader()) {
			context.getCounter(Reputation.HEADER).increment(1);
		} else {
			context.getCounter(Reputation.NOT_IN_STATES).increment(1);
		}
	}
}
