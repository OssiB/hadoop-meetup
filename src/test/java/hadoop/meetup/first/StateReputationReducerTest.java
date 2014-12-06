package hadoop.meetup.first;

import java.util.Arrays;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

public class StateReputationReducerTest {
	ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
	@Before
	public void setUp(){
		StateReputationReducer reducer = new StateReputationReducer();
	    reduceDriver = ReduceDriver.newReduceDriver(reducer);
	}
	@Test
	public void testSum(){
		reduceDriver.withInput(new Text("CA"),Arrays.asList(new IntWritable(237),new IntWritable(7856)));
		reduceDriver.withOutput(new Text("CA"),new IntWritable(8093));
	}

}
