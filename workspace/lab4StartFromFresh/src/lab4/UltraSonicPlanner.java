package lab4;

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
	private boolean wallAhead = false ;

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
		pcontrol = new PController(config,this);
		
		//start the poller 
		usPoller.start();
//		pcontrol.start();
		
//		//check wall following 
//		while (true){
//			if (usPoller.getDistance() < THRESHOLD){
//				setWallFollow(true);
//			}
//			else {
//				setWallFollow(false);
//			}
//			//sleep 20 ms 
//			try {
//				Thread.sleep(20);
//			} catch (InterruptedException e) { 			}
//		}
//		

		
//		while(!config.driveComplete()){
//			
//			if (wallFollow()){
//				//pause to let navigation to finish for now 
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				
//				//run wall following 
//
//				}
//			
//			else {
//				//run navigation
//				// the navigation is started automatically because
//				//it listens to the wall follow
//			}

			

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
		synchronized(lock){
			return wallAhead;
		}
	}

	public void setWallFollow(boolean wallAhead) {
		synchronized(lock){
			this.wallAhead = wallAhead;
		}
	}


}
