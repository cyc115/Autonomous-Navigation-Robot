package lab4newArchetecture;

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
	private ArrayList <UltrasonicSubscriberWrapper> listenerWrappers = new ArrayList<UltrasonicSubscriberWrapper>();

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
		while (true){
			distance = uSensor.getDistance();
			for (UltrasonicSubscriberWrapper usw : listenerWrappers){
				//if the distance is within range.
				//if it has not been called
				//or if it should be called continuously
				if (usw.getDistanceOnInvoke() <= distance && (!usw.isCalled() || usw.isContinueous())){
					usw.getListener().ultrasonicDistance(distance);
					usw.setCalled(true);
				}
			}
			try { Thread.sleep(SLEEP_INTERVAL); } catch(Exception e){};
		}
	}
	/**
	 * subscribe the listener iff there is no doublecates in the list.
	 * or else update the values in the list
	 * @param uListener
	 * @param distanceOnInvoke
	 * @param continuous
	 */
	public void subscribe(UltrasonicListener uListener , int distanceOnInvoke,boolean continuous){
		
		boolean doublecate = false ;
		for (UltrasonicSubscriberWrapper wrapper : listenerWrappers){
			if (wrapper.equals(uListener)) {
				wrapper.setDistanceOnInvoke(distanceOnInvoke);
				wrapper.setContinueous(continuous);
				doublecate = true ;
				break ;
			}
		}
		if (!doublecate){
			listenerWrappers.add(new UltrasonicSubscriberWrapper(uListener, distanceOnInvoke,continuous));
		}
	}
	/**
	 * 
	 * @param ulistener 
	 * @return true when unsubscribe successful
	 */
	public boolean unsubscribe(UltrasonicListener ulistener){
		boolean removed = false ;
		for (UltrasonicSubscriberWrapper wrapper : listenerWrappers){
			if (wrapper.equals(ulistener)){
				listenerWrappers.remove(wrapper);
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
