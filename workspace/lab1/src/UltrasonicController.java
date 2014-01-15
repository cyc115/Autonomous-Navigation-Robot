
public interface UltrasonicController {
	
	public void processUSData(Distance distance);
	
	/**
	 * @return returns the last read Distance from the ultrasonic sensor 
	 */
	public Distance readUSDistance();
	
	/**
	 * get the direction of ultrasonic sensor 
	 */
	public Distance.Direction getDirection ();
}
