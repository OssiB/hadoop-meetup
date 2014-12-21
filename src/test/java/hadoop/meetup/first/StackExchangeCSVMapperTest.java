package hadoop.meetup.first;

import static org.junit.Assert.*;
import hadoop.meetup.first.StateReputationMapper.Reputation;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;

public class StackExchangeCSVMapperTest {
	MapDriver<LongWritable, Text,NullWritable,Text> mapDriver;
	@Before
	public void setUp(){
		StackExchangeCSVMapper mapper = new StackExchangeCSVMapper();
		mapDriver = MapDriver.newMapDriver(mapper);
	}
	@Test
	public void processValidRecord() throws IOException{
		Text value = new Text("\"165216\",\"Austin, TX\",\"26364\",\"1951\",\"0.0012963222586704\"");
		mapDriver.withInput(new LongWritable(),value);
		mapDriver.withOutput(NullWritable.get(),new Text("165216;Austin, TX;26364;1951;0.0012963222586704"));
		mapDriver.runTest();
	}

}
