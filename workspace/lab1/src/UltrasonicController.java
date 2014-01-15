
public interface UltrasonicController {
	
	public void processUSData(int distance);
	
	/**
	 * @return returns the last read Distance from the ultrasonic sensor 
	 */
	public int readUSDistance();
}
