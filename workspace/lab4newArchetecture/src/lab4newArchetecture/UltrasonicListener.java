package lab4newArchetecture;

/**
 * these are the interface for classes that listens to the ultrasonic poller
 * it contains a method ultrasonicDistance(int distance); which obtains the
 * distance measured by the ultrasonic poller and make actions accordinly.
 * @author yuechuan
 *
 */
public interface UltrasonicListener {
	
	public void ultrasonicDistance(int distanceFromObsticle);

}
