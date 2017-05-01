import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *	Class to store information about bus schedule presented on MPK Karków website
 */
public class MPKinfo extends BusInfo {
	public MPKinfo(){
		this.weekdayList = new ArrayList<HourMinute>();
		this.saturdayList = new ArrayList<HourMinute>();
		this.sundayList = new ArrayList<HourMinute>();
	}
	public MPKinfo(String html) throws IOException{
		this.weekdayList = new ArrayList<HourMinute>();
		this.saturdayList = new ArrayList<HourMinute>();
		this.sundayList = new ArrayList<HourMinute>();

		setHTML(html);
	}

	public void setHTML(String html) throws IOException {
		this.html = html;
		getRawResult(this.html);
		this.count();
		this.streetName = findStreetName();
		this.lineNumber = findLineNumber();
		this.additionalInfo = findAdditionalInfo();
	}

	public String getAdditionalInfo(){
		return this.additionalInfo;
	}

	/**
	 * Get information from table posted on MPK site as a string
	 * @param html page adress from which to get info
	 * @return info table formatted as string where columns are separated with tabulation and rows - with new lines
	 * @throws IOException
	 */
	public String getRawResult(String html) throws IOException{
		this.rawResult = "";
		Document document = Jsoup.connect(html).get();
		//get bus number
		Elements number = document.select("p[style='font-size: 40px;']");
		this.rawResult += number.text() + "\n";

        Elements rows = document.select("table[style=' width: 700px; '] table tbody tr");     

		for (Element row : rows){
			Elements columns = row.getElementsByTag("td");
			for(Element column : columns){
				this.rawResult += column.text();
				this.rawResult += "\t"; // separate each column with tabulation
			}
			//separate each line with newline sign
			this.rawResult += "\n";
		}
		return this.rawResult;
	}
	
	/**
	 * Function to get bus schedule from MPK website.
	 * Saves all found hours to suitable class fields:
	 * * weekdayList
	 * * saturdayList
	 * * sundayList
	 * @throws IOException
	 */
	private void count() throws IOException{
		// clear lists if they are not empty
		this.weekdayList.clear();
		this.saturdayList.clear();
		this.sundayList.clear();
		
        //store information in MPKinfo class

		// cut first 4 and last 2 rows - they contain other informations
        String[] lines = Arrays.copyOfRange(this.rawResult.split("\n"), 4, this.rawResult.split("\n").length-2);
        for(String line : lines){
        	int rowHour=0;
        	line = line.replaceAll("[^\\d^\\s]", "");
        	//get columns
        	String[] columns = line.split("\t");
        	int colIterator = 0;
        	for(String column : columns){
        		String[] minutes = column.split(" ");
        		      		
        		/*
                 * Column 0: Hour
                 * Column 1: Minute (weekday)
                 * Column 2: Minute (Saturday)
                 * Column 3: Minute (Sunday)
                 */
        		switch(colIterator){
        		case 0:
    				if(StringUtils.isNotBlank(column))
        			rowHour = Integer.parseInt(column);
        			break;
        		case 1:
        			for(String minute : minutes){
        				if(StringUtils.isNotBlank(minute))
        				this.addWeekdayCourse(new HourMinute(
        						rowHour, 
        						Integer.parseInt(minute)
        						));
        			}
        			break;
        		case 2:
        			for(String minute : minutes){
        				if(StringUtils.isNotBlank(minute))
        				this.addSaturdayCourse(new HourMinute(
        						rowHour, 
        						Integer.parseInt(minute)
        						));
        			}
        			break;
        		case 3:
        			for(String minute : minutes){
        				if(StringUtils.isNotBlank(minute))
        				this.addSundayCourse(new HourMinute(
        						rowHour, 
        						Integer.parseInt(minute)
        						));
        			}
        			break;
        		}
        		colIterator++;
        	}
        }
	}
	private String findStreetName(){
		return this.rawResult.split("\n")[1].replaceAll("\t", "");
	}
	private int findLineNumber(){
		return Integer.parseInt(this.rawResult.split("\n")[0]);
	}
	private String findAdditionalInfo(){
		String[] lines = this.rawResult.split("\n");
		return lines[lines.length - 1];
	}
}
