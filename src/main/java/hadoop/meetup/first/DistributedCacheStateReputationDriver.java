package hadoop.meetup.first;

import java.net.URI;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class DistributedCacheStateReputationDriver extends Configured implements
		Tool {

	public int run(String[] args) throws Exception {
		if(args.length !=3){
			System.err.printf("Usage: %s [generic options] <input> <output><distribute cache file>\n",getClass().getSimpleName());
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		Job job = new Job(getConf(),"Sum reputation");
		job.setJarByClass(getClass());
		job.addCacheFile(new URI(args[2]));
		
		FileInputFormat.addInputPath(job,new Path(args[0]));
		FileOutputFormat.setOutputPath(job,new Path(args[1]));
		
		job.setMapperClass(StateReputationDistributedCacheMapper.class);
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
			int exitCode = ToolRunner.run(new DistributedCacheStateReputationDriver(),args);
			System.exit(exitCode);

		}

	}

