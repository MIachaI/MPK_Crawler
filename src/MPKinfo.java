import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 *	Class to store information about bus schedule presented on MPK Karków website
 */
public class MPKinfo extends BusInfo {
	public MPKinfo(){
		super();
	}
	public MPKinfo(String html) throws IOException {
		super(html);
	}

	public String getRawResult(String html) throws IOException{
		ArrayList<String> lines = new ArrayList<>();
		Document document = Jsoup.connect(html).get();
		//get bus number
		Elements number = document.select("p[style='font-size: 40px;']");
		lines.add(number.text());

        Elements rows = document.select("table[style=' width: 700px; '] table tbody tr");     

		for (Element row : rows){
			Elements columns = row.getElementsByTag("td");
			String colText = "";
			for(Element column : columns){
				colText += column.text() + "\t"; // separate each column with tabulation
			}
			lines.add(colText);
		}
		// this gives lines array where 3rd line is empty and 2 last lines are duplicated

		lines.set(2,lines.get(1).replaceAll("\t", "")); // save street name to third line (copy second line to third)
		if(Integer.parseInt(lines.get(0)) > 100) {
			lines.set(1, "bus");
		} else {
			lines.set(1,"tram");
		}
		lines.remove(lines.size() - 2);

		// now save all lines to rawResult
		String rawResult = "";
		for(String line : lines){
			rawResult += line + "\n"; // separate each line with newline sign
		}

		return rawResult;
	}
}
