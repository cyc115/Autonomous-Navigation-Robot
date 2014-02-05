package lab3StartFromFresh;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class UltraSonicPlanner extends Thread implements Planner{

	private static final int THRESHOLD = 20;	//start wall following 
	private static final int TAN_TOLORANCE = 3;
	private RobotConfiguration config ;
	private Coordinate currentPos ,nextPos , prevPos;
	private UsPoller usPoller ;
	private UltrasonicController pcontrol; 
	private Object lock ;
	private boolean wallFollow = false ;

	public UltraSonicPlanner(RobotConfiguration config) {
		this.config= config; 
		usPoller = new UsPoller(config);
		currentPos = config.getCurrentLocation();
		nextPos = config.getNextLocation();
		prevPos = config.getStartingCoordinate();
		lock = new Object();
		}
	
	public void run(){
		//initialized here to try 
//		pcontrol = new PController(config,this);
		
		//start the poller 
		usPoller.start();
//		pcontrol.start();
		
		//check wall following 
		while (true){
			if (usPoller.getDistance() < THRESHOLD){
				setWallFollow(true);
			}
			else {
				setWallFollow(false);
			}
			//sleep 20 ms 
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) { 			}
		}

			

	}
	
	//check if the current location is out of the tangent line 
	//TODO check if the next location is being changed 
	/**
	 * check if current loc is on tangent 
	 * @return
	 */
	private boolean onTangent() {
		//the slope 
		double slop = prevPos.calcSlop(nextPos);
		
		if(Math.abs((slop * (currentPos.getX() - prevPos.getX()) + prevPos.getY())
				- currentPos.getY() ) <= TAN_TOLORANCE  ){
			return true;
		}
		else 
			return false ;
		
	}

	@Override
	public void setConfiguration(RobotConfiguration config) {
		this.config=config;
	}

	@Override
	public RobotConfiguration getConfiguration() {
		return config;
	}

	
	
	public boolean hasWallAhead() {
		return false ;
	//		synchronized(lock){
//			return wallFollow;
//		}
	}

	public void setWallFollow(boolean wallFollow) {
		synchronized(lock){
			this.wallFollow = wallFollow;
		}
	}


}
