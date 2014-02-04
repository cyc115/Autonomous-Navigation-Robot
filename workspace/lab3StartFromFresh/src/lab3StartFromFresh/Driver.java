package lab3StartFromFresh;
/**
 * interface description of the tasks to be done in the third lab
 * @goal Write a program to travel to the waypoints (60, 30), (30, 30), (30, 60), and
(60, 0) in that order. This will the path you demonstrate to the TA for the first
half of the demo. Test this program ten (10) times, and record the position of
the robot a) as reported by the odometer, and b) as measured on the field.

 * @author yuechuan
 *
 */
public interface Driver {
	/**
	 * This method causes the robot to travel to the absolute field location (x, y).
This method should continuously call turnTo(double theta) and then
set the motor speed to forward(straight). This will make sure that your
heading is updated until you reach your exact goal. (This method will poll
the odometer for information)
	 * @param x x location wrt to the origin in cm 
	 * @param y y location wrt to the origin in cm
	 */
	void travelTo(double x, double y);
	
	/**
	 * This method causes the robot to turn (on point) to the absolute heading
theta. This method should turn a MINIMAL angle to it's target.
	 * @param theata angle to turn in rad
	 */
	void turnTo(double theata);
	
	/**
	 * This method returns true if another thread has called travelTo() or
turnTo() and the method has yet to return; false otherwise.
	 * @return if another thread has called travelTo() or
turnTo()
	 */
	boolean isNagivating ();

	void pause();
	void unpause();
	void start();
	
}
