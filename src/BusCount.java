import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class BusCount {
	
	public static class HourMinute{
		private int hour;
		private int minute;
		
		public HourMinute(int hour, int minute){
			this.hour = hour;
			this.minute = minute;
		}
		public HourMinute(){
			new HourMinute(0,0);
		}
		
		public int getHour(){
			return this.hour;
		}
		public int getMinute(){
			return this.minute;
		}
		
		public String toString(){
			return this.hour+":"+this.minute;
		}
	}
	
	// storing hours and minutes
	private ArrayList<HourMinute> weekdayList;
	private ArrayList<HourMinute> saturdayList;
	private ArrayList<HourMinute> sundayList;
	private String exceptionString;
	
	public BusCount(){
		this.weekdayList = new ArrayList<HourMinute>();
		this.saturdayList = new ArrayList<HourMinute>();
		this.sundayList = new ArrayList<HourMinute>();
	}
	
	public void addWeekdayCourse(HourMinute time){
		this.weekdayList.add(time);
	}
	public void addSaturdayCourse(HourMinute time){
		this.saturdayList.add(time);
	}
	public void addSundayCourse(HourMinute time){
		this.sundayList.add(time);
	}

	public ArrayList<HourMinute> getWeekdayList(){
		return this.weekdayList;
	}
	public ArrayList<HourMinute> getSaturdayList(){
		return this.saturdayList;
	}
	public ArrayList<HourMinute> getSundayList(){
		return this.sundayList;
	}
	
	
	// method that count elements in the lists.
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
	 * Function to get bus schedule from MPK website.
	 * @param html - url Link to MPK site containing desired schedule (eg. http://rozklady.mpk.krakow.pl/?lang=PL&rozklad=20170428&linia=4__2__41)
	 * @return	BusCount Object - containing all departure times, divided by weekday/saturday/sunday
	 * @throws IOException
	 */
	public static BusCount count(String html) throws IOException{
		BusCount busCount = new BusCount();
		Document document = Jsoup.connect(html).get();
        //Document document = Jsoup.parse(html);
        Elements rows = document.select("table[style=' width: 700px; '] table tbody tr");     
     
        String result = "";

        /**
         * Column 0: Hour
         * Column 1: Minute (weekday)
         * Column 2: Minute (Saturday)
         * Column 3: Minute (Sunday)
         */
        for (Element row : rows){
        	Elements columns = row.getElementsByTag("td");
        	
        	//only iterate through rows where column count is 4 (rest is not interesting for us
        	if(columns.size() == 4){
        		for(Element column : columns){
        			CharSequence cellCharSequence = column.text().replaceAll("A","").replaceAll("NZ", "");
            		if(StringUtils.isNumericSpace(cellCharSequence)){
            			result += cellCharSequence; // saves hours without "A" and "NZ", one below saves string with this info            			
            		}
            		// add tabulation after each column
            		result+="\t";
        		}
        		// add new line after each row
        		result += "\n";
        	}
        }
        
        //store information in BusCount class
        String[] lines = result.split("\n");
        for(String line : lines){
        	int rowHour=0;
        	
        	//get columns
        	String[] columns = line.split("\t");
        	int colIterator = 0;
        	for(String column : columns){
        		//hour
        		if(colIterator == 0){ 
        			rowHour = Integer.parseInt(column);
        		}
        		//minutes (weekday)
        		else if (colIterator == 1){
        			String[] minutes = column.split(" ");
        			for(String minute : minutes){
        				busCount.addWeekdayCourse(new BusCount.HourMinute(
        						rowHour, 
        						Integer.parseInt(minute)
        						));
        			}
        		}
        		// minutes (saturday)
        		else if (colIterator == 2){
        			String[] minutes = column.split(" ");
        			for(String minute : minutes){
        				busCount.addSaturdayCourse(new BusCount.HourMinute(
        						rowHour, 
        						Integer.parseInt(minute)
        						));
        			}
        		}
        		//minutes (sunday)
        		else if (colIterator == 3){
        			String[] minutes = column.split(" ");
        			for(String minute : minutes){
        				busCount.addSundayCourse(new BusCount.HourMinute(
        						rowHour, 
        						Integer.parseInt(minute)
        						));
        			}
        		}
        		colIterator++;
        	}
        }
		
		return busCount;
	}
	
	public void storeException(Exception e){
		this.exceptionString += e + "\n";
	}
	public String exceptions(){
		return this.exceptionString;
	}
	
	
	public String toString(){
		String result = "";
		result += "Week\tSoboty\tNiedziele\n";
		result += 
				getWeekdayCourseCount() + "\t" +
				getSaturdayCourseCount() + "\t" +
				getSundayCourseCount() + "\t\n";
		
		return result;
	}
}

