import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * @author umat
 *	Class to store information about bus schedule presented on MPK Karków website
 */
public class MPKinfo {
	private String html;
	private String streetName;
	private String rawResult;
	private String additionalInfo;
	// storing hours and minutes
	private ArrayList<HourMinute> weekdayList;
	private ArrayList<HourMinute> saturdayList;
	private ArrayList<HourMinute> sundayList;
	
	public MPKinfo(){
		this.weekdayList = new ArrayList<HourMinute>();
		this.saturdayList = new ArrayList<HourMinute>();
		this.sundayList = new ArrayList<HourMinute>();
	}
	public MPKinfo(String html) throws IOException{
		this.html = html;
		this.weekdayList = new ArrayList<HourMinute>();
		this.saturdayList = new ArrayList<HourMinute>();
		this.sundayList = new ArrayList<HourMinute>();
		
		getRawResult(this.html);
		this.count();
		this.streetName = findStreetName();
		this.additionalInfo = findAdditionalInfo();
	}
	
	// add new course to suitable list
	public void addWeekdayCourse(HourMinute time){
		this.weekdayList.add(time);
	}
	public void addSaturdayCourse(HourMinute time){
		this.saturdayList.add(time);
	}
	public void addSundayCourse(HourMinute time){
		this.sundayList.add(time);
	}

	public void setHTML(String html) throws IOException {
		this.html = html;
		getRawResult(this.html);
		this.count();
		this.streetName = findStreetName();
	}
	
	// get desired list
	public ArrayList<HourMinute> getWeekdayList(){
		return this.weekdayList;
	}
	public ArrayList<HourMinute> getSaturdayList(){
		return this.saturdayList;
	}
	public ArrayList<HourMinute> getSundayList(){
		return this.sundayList;
	}
	
	public String getStreetName(){
		return this.streetName;
	}
	public String getAdditionalInfo(){
		return this.additionalInfo;
	}
	
	// methods that count elements in the lists.
	public int getWeekdayCourseCount(){
		return weekdayList.size();
	}
	public int getSaturdayCourseCount(){
		return saturdayList.size();
	}
	public int getSundayCourseCount(){
		return sundayList.size();
	}

	/**
	 * Get information from table posted on MPK site as a string
	 * @param html
	 * @return info table formatted as string where columns are separated with tabulation and rows - with new lines
	 * @throws IOException
	 */
	public String getRawResult(String html) throws IOException{
		Document document = Jsoup.connect(html).get();
        Elements rows = document.select("table[style=' width: 700px; '] table tbody tr");     

		this.rawResult = "";
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
	 * @param html - url Link to MPK site containing desired schedule (eg. http://rozklady.mpk.krakow.pl/?lang=PL&rozklad=20170428&linia=4__2__41)
	 * @throws IOException
	 */
	private void count() throws IOException{
		// clear lists if they are not empty
		this.weekdayList.clear();
		this.saturdayList.clear();
		this.sundayList.clear();
		
        //store information in MPKinfo class
		
		// cut first three and last 2 rows - they contain other informations
        String[] lines = Arrays.copyOfRange(this.rawResult.split("\n"), 3, this.rawResult.split("\n").length-2);
        for(String line : lines){
        	int rowHour=0;
        	line = line.replaceAll("[^\\d^\\s]", "");
        	//get columns
        	String[] columns = line.split("\t");
        	int colIterator = 0;
        	for(String column : columns){
        		String[] minutes = column.split(" ");
        		      		
        		/**
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
		return this.rawResult.split("\n")[0].replaceAll("\t", "");
	}
	private String findAdditionalInfo(){
		String[] lines = this.rawResult.split("\n");
		return lines[lines.length - 1];
	}
	
	public String toString(){
		String result = "";
		result += "Week\tSoboty\tNiedziele\n";
		result += 
				getWeekdayCourseCount() + "\t" +
				getSaturdayCourseCount() + "\t" +
				getSundayCourseCount();
		
		return result;
	}
	
}
