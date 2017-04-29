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
	 * @param html link to get data
	 * @return formatted result. Each row contains: hour \t minutes weekday \t minutes saturday \t minutes sunday
	 * @throws IOException
	 */
	public static String getFormattedResult(String html) throws IOException{
		Document document = Jsoup.connect(html).get();
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
        			CharSequence cellCharSequence = column.text().replaceAll("[^\\d^\\s]",""); //delete all non-numeric characters (excluding spaces)
            		if(StringUtils.isNumericSpace(cellCharSequence) && cellCharSequence.length() < 30){ //if probably unnecessary
            			result += cellCharSequence;  			
            		}
            		// add tabulation after each column
            		result+="\t";
        		}
        		// add new line after each row
        		result += "\n";
        	} 
        	else if( columns.size() == 2){
        		for(Element column : columns){
        			String[] deleteRedundantChars = column.text().split(" ");
        			CharSequence cellCharSequence = column.text().replaceAll("[^\\d^\\s]","");
            		if(StringUtils.isNumericSpace(cellCharSequence)  && cellCharSequence.length() < 30){
            			result += cellCharSequence;
            		}
            		// add tabulation after each column
            		result+="\t";
        		}
        		// add new line after each row
        		result += "\n";
        	}
        }

		return result;
	}
	
	
	/**
	 * Function to get bus schedule from MPK website.
	 * @param html - url Link to MPK site containing desired schedule (eg. http://rozklady.mpk.krakow.pl/?lang=PL&rozklad=20170428&linia=4__2__41)
	 * @return	BusCount Object - containing all departure times, divided by weekday/saturday/sunday
	 * @throws IOException
	 */
	public static BusCount count(String html) throws IOException{
		BusCount busCount = new BusCount();
		String result = getFormattedResult(html);
        
        //store information in BusCount class
        String[] lines = result.split("\n");
        for(String line : lines){
        	int rowHour=0;
        	
        	//get columns
        	String[] columns = line.split("\t");
        	int colIterator = 0;
        	for(String column : columns){
        		String[] minutes = column.split(" ");
        		
        		switch(colIterator){
        		case 0:
    				if(StringUtils.isNotBlank(column))
        			rowHour = Integer.parseInt(column);
        			break;
        		case 1:
        			for(String minute : minutes){
        				if(StringUtils.isNotBlank(minute))
        				busCount.addWeekdayCourse(new BusCount.HourMinute(
        						rowHour, 
        						Integer.parseInt(minute)
        						));
        			}
        			break;
        		case 2:
        			for(String minute : minutes){
        				if(StringUtils.isNotBlank(minute))
        				busCount.addSaturdayCourse(new BusCount.HourMinute(
        						rowHour, 
        						Integer.parseInt(minute)
        						));
        			}
        			break;
        		case 3:
        			for(String minute : minutes){
        				if(StringUtils.isNotBlank(minute))
        				busCount.addSundayCourse(new BusCount.HourMinute(
        						rowHour, 
        						Integer.parseInt(minute)
        						));
        			}
        			break;
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

