
public interface UltrasonicController {
	
	public void processUSData(int distance);
	
	public int readUSDistance();
	public int getRightMotorSpeed();
	public int getLeftMotorSpeed();
}
