package hadoop.meetup.first;


import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class StateReputationDistributedCacheMapper extends
		Mapper<LongWritable, Text, Text, IntWritable> {
	private StateMetadata metadata;

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		String[] line = value.toString().split("\",");
		if (line.length > 1) {
			String location = line[1].toUpperCase().replaceAll("\"", "");
			String reputation = line[2].replaceAll("\"", "");
			String state = "";
			if (location.indexOf(',') != -1) {
				state = location.split(",")[1].trim();
			} else {
				state = location.trim();
			}
			if (metadata.containsState(state)) {
				Integer population=metadata.getPopulation(state);
				context.write(new Text(state), new IntWritable(new Integer(
						reputation)/population));
			}

		}

	}

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		URI[] cachedUris = context.getCacheFiles();
		metadata = new StateMetadata();
		if (cachedUris.length != 0) {
			metadata.initialize(new File(cachedUris[0].toString()));
		}

	}
}
