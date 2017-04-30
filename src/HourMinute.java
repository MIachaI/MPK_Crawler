/**
	 * @author umat
	 * Class stores hour and minute
	 * Infomation is used to describe time that bus stops on a bus stop.
	 * 
	 */
	public class HourMinute{
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