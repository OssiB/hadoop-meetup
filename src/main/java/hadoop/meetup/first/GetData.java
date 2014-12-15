package hadoop.meetup.first;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class GetData {

	public static void main(String[] args) throws IOException {
		String states[] = args[2].split(",");
		for (int i = 0; i < states.length; i++) {
			URL url = new URL(args[1] + states[i]);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			InputStream connStream = conn.getInputStream();
			FileSystem hdfs = FileSystem.get(new Configuration());
			FSDataOutputStream outStream = hdfs.create(new Path(args[0],
					states[i] + ".csv"));
			IOUtils.copy(connStream, outStream);
			outStream.close();
			connStream.close();
			conn.disconnect();
		}
	}
}
