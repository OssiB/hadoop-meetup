package hadoop.meetup.second;

import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import au.com.bytecode.opencsv.CSVWriter;

public class HousePrice {

	public static void main(String[] args) throws Exception {
		String[] cities = "Helsinki:Espoo:Vantaa:Tampere:Turku:Kuopio:Oulu"
				.split(":");
		String baseUrl = "http://asuntojen.hintatiedot.fi/haku/?search=1&cr=1";
		String cityPart = "&c=";
		CSVWriter writer = new CSVWriter(new FileWriter("houseprice2015.csv"), ';');
		boolean hasMore = true;
		String next = "&z=";
		String room = "&r=";
		int roomSize = 1;
		for (int cityIndex = 0; cityIndex < cities.length; cityIndex++) {
			cityPart = "&c=" + cities[cityIndex];
			for (int i = roomSize; i < 5; i++) {
				int z = 1;
				hasMore = true;
				String url = baseUrl + cityPart + room + i;
				int count = 0;
				while (hasMore) {
					hasMore = false;
					url = baseUrl + cityPart + room + i + next + z;
					Document doc = Jsoup.connect(url).get();
					Elements rows = doc.select("tr");
					for (Element row : rows) {
						if (row.children().size() > 10) {
							Elements tableData = row.children();
							String kaupunginosa = tableData.get(0).text();
							if (!kaupunginosa.startsWith("Kaupunginosa")) {
								hasMore = true;
								String data = parse(tableData);
								data += cities[cityIndex] + ";" + count++;
								
								writer.writeNext(data.toString().split(";"));
							}

						}
					}
					z++;
				}

			}
		}
		writer.close();
	}

	public static String parse(Elements tableData) {
		StringBuffer data = new StringBuffer("");
		for (int i = 0; i < 10; i++) {
			data.append(tableData.get(i).text());
			data.append(";");
		}
		return data.toString();
	}

}
