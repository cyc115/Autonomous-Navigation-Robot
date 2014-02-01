package lab3StartFromFresh;

import lejos.nxt.LCD;

public class LCDWriter extends Thread implements Monitor{
	private static final long DISPLAY_PERIOD = 250;
	private RobotConfiguration config;
	public String [] s ;
	
	LCDWriter(RobotConfiguration config){
		s = new String[8];
		this.config = config;
		for(int i = 0 ; i < 8 ; i++){
			s[i] = " ";
		}
	}
	
	public void run(){
		long displayStart, displayEnd;
		LCD.clearDisplay();

		while (true) {
			displayStart = System.currentTimeMillis();

			
			// clear the lines for displaying odometry information
			LCD.drawString(s[0], 0, 0);
			LCD.drawString(s[1], 0, 1);
			LCD.drawString(s[2], 0, 2);
			LCD.drawString(s[3], 0, 3);
			LCD.drawString(s[4], 0, 4);
			LCD.drawString(s[5], 0, 5);
			LCD.drawString(s[6], 0, 6);
			LCD.drawString(s[7], 0, 7);
			
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
		s[lineNumber] = str;
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