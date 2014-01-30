/*
 * OdometryDisplay.java
 */
import lejos.nxt.LCD;

public class OdometryDisplay extends Thread {
	private static final long DISPLAY_PERIOD = 250;
	private Odometer odometer;
	private OdometryCorrection odoCorrection; 
	private double offSet = 11.66,rotationOffset = 2.5 ; //offset btn sensor and wheels



	// constructor
	public OdometryDisplay(Odometer odometer,OdometryCorrection currection ) {
		this.odometer = odometer;
		this.odoCorrection = currection;
	}

	// run method (required for Thread)
	public void run() {
		long displayStart, displayEnd;
		double[] position = new double[3];

		// clear the display once
		LCD.clearDisplay();
		

		while (true) {
			displayStart = System.currentTimeMillis();
			String l0 = "disXCov" + odoCorrection.distXCovered,					
			l1= "disYCov" + odoCorrection.distYCovered,			
			l2= "" ,
			l3=  "dir:" + SquareDriver.getCurrentMovementDirection() ,	//the x and y covered distance in the first sqr
			l4="line: " + odoCorrection.getLineNumber();
			
			// clear the lines for displaying odometry information
			LCD.drawString("X: " + formattedDoubleToString(position[0],2), 0, 0);
			LCD.drawString("Y: "+ formattedDoubleToString(position[1],2), 0, 1);
			LCD.drawString("T: "+ formattedDoubleToString(wrap(position[2],2*Math.PI),2), 0, 2);
			LCD.drawString(l0, 0, 3);
			LCD.drawString(l1, 0, 4);
			LCD.drawString(l2, 0, 5);
			LCD.drawString(l3, 0, 6);
			LCD.drawString(l4, 0, 7);

			// get the odometry information
			odometer.getPosition(position, new boolean[] { true, true, true });
			
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
	/**
	 * wrap value wrt to wrapN
	 * @param value
	 * @param wrapN
	 * @return
	 */
	private static double wrap(double value , double wrapN){
		double multiples = value / wrapN ;
		return (multiples - (int)multiples) * wrapN ;
	}
	private static String formattedDoubleToString(double x, int places) {
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

}
