package hadoop.meetup.first;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FilterMapper extends
		Mapper<LongWritable,Text,NullWritable,Text> {

	@Override
	protected void map(LongWritable key, Text value,
			Context context)
			throws IOException, InterruptedException {
		List<String> states = Arrays.asList("AZ","FL","CA","IL","IN",
                "MD","MA","GA","MI","MO",
                "NC","NJ","NY","OH","PA",
                "TN","TX","VA","WA","WI");
		String line = value.toString();
		StackExchangeRecord reader = new StackExchangeRecord(line);
		if(states.contains(reader.getState())){
		 context.write(NullWritable.get(),new Text(value.toString()+",\""+reader.getState()+"\""));
		}
	}
	

}
