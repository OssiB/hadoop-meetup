package hadoop.meetup.first;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class StackExchangeCSVMapper extends
		Mapper<LongWritable, Text,NullWritable,Text> {

	@Override
	protected void map(LongWritable key, Text value,
			Context context)
			throws IOException, InterruptedException {
		String line= value.toString();
		String[] record=line.split("\",");
		StringBuffer valueString = new StringBuffer("");
		for(int i = 0;i<record.length;i++){
			valueString.append(record[i].replaceAll(";|\"",""));
			if(i<record.length-1){
				valueString.append(";");
			}
		}
		context.write(NullWritable.get(),new Text(valueString.toString()));
		
		
	}

}
