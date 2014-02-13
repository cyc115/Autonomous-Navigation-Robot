package coreLib;

/**
 * these are the interface for classes that listens to the ultrasonic poller
 * it contains a method ultrasonicDistance(int distance); which obtains the
 * distance measured by the ultrasonic poller and make actions accordinly.
 * @author yuechuan
 *
 */
public interface UltrasonicListener {
	
	
	
	public void ultrasonicDistance(int distanceFromObsticle);
	
	public int getDistanceOnInvoke ();
	public UltrasonicListener setDistanceOnInvoke (int distance);
	
	public boolean isContinuous();
	public UltrasonicListener setContinuous(boolean continuous );

	public boolean isCalled();
	public UltrasonicListener setCalled(boolean called);
	
	

}
