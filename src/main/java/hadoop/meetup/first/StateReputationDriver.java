package hadoop.meetup.first;



import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class StateReputationDriver extends Configured implements Tool {

	public int run(String[] args) throws Exception {
		if(args.length !=2){
			System.err.printf("Usage: %s [generic options] <input> <output>\n",getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		
		Configuration conf = new Configuration();
	    Job job = Job.getInstance(conf, "Sum reputation");
	    
		FileInputFormat.addInputPath(job,new Path(args[0]));
		FileOutputFormat.setOutputPath(job,new Path(args[1]));
		
		job.setMapperClass(StateReputationMapper.class);
		job.setCombinerClass(StateReputationReducer.class);
		job.setReducerClass(StateReputationReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		return job.waitForCompletion(true) ? 0 :1;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new StateReputationDriver(),args);
		System.exit(exitCode);

	}

}
