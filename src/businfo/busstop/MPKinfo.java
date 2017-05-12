package businfo.busstop;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 *	Class to store information about bus schedule presented on MPK Karków website
 */
public class MPKinfo extends BusInfo {
	public MPKinfo(){
		super();
	}
	public MPKinfo(String html) throws IOException {
		super(html);
		checkColumnNames(this.columnNames);
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
			StringBuilder colText = new StringBuilder();
			for(Element column : columns){
				colText.append(column.text()).append("\t"); // separate each column with tabulation
			}
			lines.add(colText.toString());
		}
		// this gives lines array where 3rd line is empty and 2 last lines are duplicated

		lines.set(2,lines.get(1).replaceAll("\t", "")); // save street name to third line (copy second line to third)
		if(Integer.parseInt(lines.get(0)) > 100) {
			lines.set(1, "Bus");
		} else {
			lines.set(1,"Light train");
		}
		lines.remove(lines.size() - 2);

		// now save all lines to rawResult
		StringBuilder rawResult = new StringBuilder();
		for(String line : lines){
			rawResult.append(line).append("\n"); // separate each line with newline sign
		}
		return rawResult.toString();
	}

	public boolean checkColumnNames(ArrayList<String> columnNames){
		if(
				columnNames.size() == 4
				&& Objects.equals(columnNames.get(0), "Godzina")
				&& Objects.equals(columnNames.get(1), "Dzień powszedni")
				&& Objects.equals(columnNames.get(2), "Soboty")
				&& Objects.equals(columnNames.get(3), "Święta")
				){
			return true;
		}
		else{
			this.warnings.add("Niestandardowe nazwy kolumn. Sprawdź przystanek " + this.html);
		}
		if(columnNames.size() != 4){
			this.warnings.add("Niestandardowa ilość kolumn");
		}
		return false;
	}
}
