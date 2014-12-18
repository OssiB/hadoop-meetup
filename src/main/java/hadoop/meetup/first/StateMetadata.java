package hadoop.meetup.first;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


import org.apache.hadoop.io.IOUtils;

public class StateMetadata {

	private Map<String,Integer> stateIdToPopulation = new HashMap<String, Integer>();
	private static int MILLION = 1000000;
	public void initialize(File file) throws IOException{
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line;
			in.readLine();
			while((line =in.readLine()) != null){
				String[] stateInfo = line.split("\",");
				String state =stateInfo[1].replaceAll("\"", "");
				String population = stateInfo[2];
				stateIdToPopulation.put(state,new Integer(population)/MILLION);
			}
		}
		finally {
			IOUtils.closeStream(in);
		}
	}
	
	public Integer getPopulation(String state){
		return stateIdToPopulation.get(state);
	}
	public boolean containsState(String state){
		return stateIdToPopulation.containsKey(state);
	}
}
