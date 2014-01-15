/**
 * Distance object 
 * @author yuechuan
 *
 */
public class Distance {
	//distance measured by the sensor
	int distance ;
	Direction direction ; //the direction of the measurement 
	/**
	 * pointing position of the sensor.\n
	 * for lab 1 we'll use only up and left 
	 * @author yuechuan
	 *
	 */
	public enum Direction{
		LEFT,UP,RIGHT,DOWN
	};
	
}
