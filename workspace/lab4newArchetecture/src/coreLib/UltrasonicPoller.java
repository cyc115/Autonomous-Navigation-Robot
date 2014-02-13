package coreLib;

import java.util.ArrayList;

import lejos.nxt.UltrasonicSensor;


/**
 * ultrasonicPoller will poll the ultrasonic sensor periodically and call which ever UltrasonicListener 
 * that should run when reaching certain distance. this is a event driven approach and should be (hopefully) faster 
 * than the while loop setting flag approach 
 * 
 * TODO test this class's event driven approach 
 * @author yuechuan
 *
 */
public class UltrasonicPoller extends Thread implements UltrasonicPlanner {
	private int SLEEP_INTERVAL = 25 ;
	AbstractConfig config;
	UltrasonicSensor uSensor;
	private int distance = 25; //initialize the distance read to 25
	private static UltrasonicPoller instance ;
	private static boolean threadStarted = false ;
	private ArrayList <UltrasonicListener> usListenerList = new ArrayList<UltrasonicListener>();

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
		if (instance == null){
			instance = new UltrasonicPoller(AbstractConfig.getInstance());
		}
		return instance;
	}
	
	public void run (){
		threadStarted = true ;
		while (!AbstractConfig.getInstance().isDriveComplete()){
			distance = uSensor.getDistance();
			for (UltrasonicListener usw : usListenerList){
				//if the distance is within range.
				//if it has not been called
				//or if it should be called continuously
				if (usw.getDistanceOnInvoke() >= distance && (!usw.isCalled() || usw.isContinuous())){
					usw.ultrasonicDistance(distance);
					usw.setCalled(true);
				}
			}
			try { Thread.sleep(SLEEP_INTERVAL); } catch(Exception e){};
		} 
		threadStarted = false;
	}
	/**
	 * subscribe the listener iff there is no doublecates in the list.
	 * or else update the values in the list
	 * @param uListener
	 * @param distanceOnInvoke
	 * @param continuous
	 * @return true if subscriber is doublecated 
	 */
	public boolean subscribe(UltrasonicListener uListener){
		boolean doublecate = false ;
		for (UltrasonicListener usl : usListenerList){
			if (usl.equals(uListener)) {
				doublecate = true ;
				break ;
			}
		}
		if (!doublecate){
			usListenerList.add(uListener);
		}
		return doublecate;
	}
	/**
	 * 
	 * @param ulistener 
	 * @return true when unsubscribe successful
	 */
	public boolean unsubscribe(UltrasonicListener ulistener){
		boolean removed = false ;
		for (UltrasonicListener usl: usListenerList){
			if (usl.equals(ulistener)){
				usListenerList.remove(usl);
				removed = true ;
			}
		}
		return removed;
	}
	
	/**
	 * distance refresh rate 20hz 
	 * @return
	 */
	public int getDistance() {
		return distance;
	}
	
	public static boolean isThreadStarted() {
		return threadStarted;
	}

	public static void setThreadStarted(boolean threadStarted) {
		UltrasonicPoller.threadStarted = threadStarted;
	}

}
