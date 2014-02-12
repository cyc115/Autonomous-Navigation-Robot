package lab4newArchetecture;

import lejos.nxt.UltrasonicSensor;


/**
 * TODO this class is incomplete and is not yet Event driven 
 * @author yuechuan
 *
 */
public class UltrasonicPoller extends Thread implements UltrasonicPlanner {
	private int SLEEP_INTERVAL = 25 ;
	AbstractConfig config;
	private int distance = 25; //initialize the distance read to 25
	UltrasonicSensor uSensor;
	private static UltrasonicPoller uPoller ;
	private static boolean threadStarted = false ;
	
	private UltrasonicPoller (AbstractConfig config){
		this.config = config;
		uSensor = new UltrasonicSensor(AbstractConfig.ULTRASONIC_SENSOR_PORT);
		//start the sensor 
		uSensor.continuous();
		//filter the distance and get 10 times polling avg
		double mean = 0 ;
		for (int i = 0 ; i < 10 ; i ++ ){
			mean = ((mean*i) + uSensor.getDistance())/i;
		}
		distance = (int) mean ;
	}
	
	public static UltrasonicPoller getInstance(){
		if (uPoller == null){
			uPoller = new UltrasonicPoller(AbstractConfig.getInstance());
		}
		return uPoller;
	}
	
	public void run (){
		threadStarted = true ;
		while (true){
			distance = uSensor.getDistance();
			try { Thread.sleep(SLEEP_INTERVAL); } catch(Exception e){};
		}
	}
	/**
	 * distance refresh rate 20hz 
	 * @return
	 */
	public int getDistance() {
		return distance;
	}
}
