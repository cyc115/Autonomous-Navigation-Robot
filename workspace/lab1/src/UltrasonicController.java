/**
 * Controller interface for the Ultrasonic sensor 
 * @author Yuechuan Chen
 * @version 1.0
 * Date: Tue 21st Jan 2014
 */
public interface UltrasonicController {
	/**
	 * make movement decisions based on the distance from the wall.
	 *  <br>In this example we have not used the given filter for the gaps, instead, we have chosen a 
	 * correct camera angle for the error from the gap to be less noticeable.
	 * @param distance to the wall,
	 */
	public void processUSData(int distance);
	/**
	 * return distance from the wall obtained by sensor 
	 */
	public int readUSDistance();
	/**
	 * returns the right motor speed 
	 */
	public int getRightMotorSpeed() ;

	/**
	 * returns the left motor speed 
	 */
	public int getLeftMotorSpeed();
}
