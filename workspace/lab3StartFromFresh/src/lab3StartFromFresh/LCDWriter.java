package lab3StartFromFresh;

import lejos.nxt.LCD;

public class LCDWriter extends Thread implements Monitor{
	private static final long DISPLAY_PERIOD = 250;
	private RobotConfiguration config;
	public String [] s ;
	private Object lock ;
	
	LCDWriter(RobotConfiguration config){
		lock = new Object();;
		s = new String[8];
		this.config = config;
		for(int i = 0 ; i < 8 ; i++){
			s[i] = "";
		}
	}
	
	public void run(){
		long displayStart, displayEnd;
		LCD.clearDisplay();

		while (true) {
			displayStart = System.currentTimeMillis();

			
			// clear the lines for displaying odometry information
			for (int i = 0; i < 8; i++) {
				LCD.drawString(s[7-i], 0, i);
			}
			
			// throttle the OdometryDisplay
			displayEnd = System.currentTimeMillis();
			if (displayEnd - displayStart < DISPLAY_PERIOD) {
				try {
					Thread.sleep(DISPLAY_PERIOD - (displayEnd - displayStart));
				} catch (InterruptedException e) {
				}
			}
		}
	}
	@Override
	public void writeToScreen(String str, int lineNumber) {
		synchronized (lock){
			s[lineNumber] = ""+str;
		}

	}
	public static String formattedDoubleToString(double x, int places) {
		String result = "";
		String stack = "";
		long t;
		
		// put in a minus sign as needed
		if (x < 0.0)
			result += "-";
		
		// put in a leading 0
		if (-1.0 < x && x < 1.0)
			result += "0";
		else {
			t = (long)x;
			if (t < 0)
				t = -t;
			
			while (t > 0) {
				stack = Long.toString(t % 10) + stack;
				t /= 10;
			}
			
			result += stack;
		}
		
		// put the decimal, if needed
		if (places > 0) {
			result += ".";
		
			// put the appropriate number of decimals
			for (int i = 0; i < places; i++) {
				x = Math.abs(x);
				x = x - Math.floor(x);
				x *= 10.0;
				result += Long.toString((long)x);
			}
		}
		
		return result;
	}
	
	/**
	 * wrap value wrt to wrapN
	 * @param value
	 * @param wrapN
	 * @return
	 */
	public static double wrap(double value , double wrapN){
		double multiples = value / wrapN ;
		return (multiples - (int)multiples) * wrapN ;
	}


}
