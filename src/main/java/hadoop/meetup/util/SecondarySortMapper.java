package hadoop.meetup.util;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class SecondarySortMapper extends
		Mapper<LongWritable, Text, DateTemperaturePair, Text> {
	NcdcRecord record = new NcdcRecord();
	private Text theTemperature = new Text();
	private DateTemperaturePair pair = new DateTemperaturePair();

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		record.parse(value);
		if (!record.isAirTemperatureMalformed()) {
			pair.setYearMonth(record.getYearMonth());
			pair.setDay(record.getObservationDateString());
			pair.setTemperature(record.getAirTemperature());
			theTemperature.set("" + record.getAirTemperature());
			context.write(pair, theTemperature);
		}
	}

}
