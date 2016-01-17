package hadoop.meetup.util;

import java.text.*;


import org.apache.hadoop.io.Text;

public class NcdcRecord {

	private static final int MISSING_TEMPERATURE = 9999;

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmm");

	private String stationId;
	private String observationDateString;
	private String year;
	private String month;
	private String airTemperatureString;
	private int airTemperature;
	private boolean airTemperatureMalformed;
	private String quality;
	public NcdcRecord(){
		
	}
	public NcdcRecord(String rawData){
		this.parse(rawData);
	}
	public static int getMissingTemperature() {
		return MISSING_TEMPERATURE;
	}

	public static DateFormat getDateFormat() {
		return DATE_FORMAT;
	}

	public String getStationId() {
		return stationId;
	}

	public String getObservationDateString() {
		return observationDateString;
	}

	public String getYear() {
		return year;
	}

	public String getAirTemperatureString() {
		return airTemperatureString;
	}

	public int getAirTemperature() {
		return airTemperature;
	}

	public boolean isAirTemperatureMalformed() {
		return airTemperatureMalformed;
	}

	public String getQuality() {
		return quality;
	}

	public String getYearMonth(){
		return year+"-"+month;
	}

	public void parse(String record) {
		stationId = record.substring(4, 10) + "-" + record.substring(10, 15);
		observationDateString = record.substring(15, 27);
		year = record.substring(15, 19);
		month = record.substring(19,21);
		airTemperatureMalformed = false;
		// Remove leading plus sign as parseInt doesn't like them (pre-Java 7)
		if (record.charAt(87) == '+') {
			airTemperatureString = record.substring(88, 92);
			airTemperature = Integer.parseInt(airTemperatureString);
		} else if (record.charAt(87) == '-') {
			airTemperatureString = record.substring(87, 92);
			airTemperature = Integer.parseInt(airTemperatureString);
		} else {
			airTemperatureMalformed = true;
		}
		airTemperature = Integer.parseInt(airTemperatureString);
		quality = record.substring(92, 93);
	}

	public void parse(Text record) {
		parse(record.toString());
	}
}
