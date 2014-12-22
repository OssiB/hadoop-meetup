package hadoop.meetup.first;

public class StackExchangeRecord {
	private String reputation = "";
	private String state = "";
	private boolean isHeader = false;
    public StackExchangeRecord(){
    	
    }
	public StackExchangeRecord(String line) {
		this.parse(line);
	}

	public String getState() {
		return state;
	}

	public String getReputation() {
		return reputation;
	}
	public boolean isHeader() {
		return isHeader;
	}
	public void parse(String line){
		String[] lineSplitted = line.toString().split(";");
		if (lineSplitted.length > 1) {
			String location = lineSplitted[1].toUpperCase();
			reputation = lineSplitted[2];
			state = "";
			if (location.indexOf(',') != -1) {
				state = location.split(",")[1].trim();
			} else {
				state = location.trim();
			}
		} else {
			isHeader = true;
		}
	}
	
}
