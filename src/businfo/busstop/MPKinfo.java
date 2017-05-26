package businfo.busstop;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 *	Class to store information about bus schedule presented on MPK Cracow website
 */
public class MPKinfo extends BusInfo {
	public MPKinfo(){
		super();
	}
	public MPKinfo(String html) throws IOException {
		super(html);
		checkColumnNames(this.columnNames);
	}

	public String getRawResult(Connection connection) throws IOException{
		ArrayList<String> lines = new ArrayList<>();
		Document document = connection.get();
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

	public String getRawHtml() throws IOException {
		StringBuilder result = new StringBuilder();
		// find print view page (looks nicer)
		Element printLink = this.jsoupConnection.get().select("td[style=' width: 100px; '] a[target='_blank']").first();
		String link = printLink.attr("href");
		// go to print view page
		Connection connection2 = Jsoup.connect(link);
		Document document2 = connection2.get();
		Element table = document2.select("table[style=' width: 700px; ']").first();
		Element head = document2.select("head").first();
		// add style absolute path
		head.append("<link rel='stylesheet' href='http://rozklady.mpk.krakow.pl/widok/GP/CSS/style.css' type='text/css' />");

		// add html tags at the beginning and the end
		result.append("<!DOCTYPE HTML>\n<html>");
		result.append(head.outerHtml()); 		// append body section
		result.append(table.outerHtml());		// append table section
		result.append("</html>");

		return result.toString();
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
		else if (columnNames.size() == 1
				&& columnNames.get(0).equals("Godzina")
				&& columnNames.get(1).equals("Wszystkie dni tygodnia")){
			setSaturdayList(this.getWeekdayList());
			setSundayList(this.getWeekdayList());
			this.warnings.add("Ten sam rozkład dla wszystkich dni tygodnia\t" + this.html);
			return true;
		}
		else{
			this.warnings.add("Niestandardowe nazwy kolumn. Sprawdź przystanek \t" + this.html);
		}
		return false;
	}
}
