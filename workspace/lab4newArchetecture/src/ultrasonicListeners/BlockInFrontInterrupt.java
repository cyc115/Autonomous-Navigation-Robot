/**
 * 
 */
package ultrasonicListeners;

import lejos.nxt.Motor;
import lejos.nxt.Sound;
import other.Lab5P2;
import coreLib.Coordinate;
import coreLib.Driver;
import coreLib.Odometer;
import coreLib.UltrasonicListener;
import coreLib.UltrasonicPoller;

/**
 * This class is used when there's an obstacle very near the robot 
 * the robot will then check if it is a block or foam and either 
 * picks up or push more coordinates to the stack 
 * @author yuechuan
 *
 */
public class BlockInFrontInterrupt implements UltrasonicListener{
	private int distanceOnInvoke = 20;
	private boolean continuous = true ;
	private boolean called = false ;
	/**
	 *  if the interrupt is running
	 *  set to true 
	 */
	private boolean running = false ;


	@Override
	/**
	 * does action 1 in Lab5P2 --> see more.
	 * also refer to the state diagram on page 151 
	 * {@link  other.Lab5P2#action1 }
	 * TODO fking link does not work ...
	 */	
	public void ultrasonicDistance(int distanceFromObsticle) {
		if (! running){
			Sound.beep();
			Sound.beep();
			Sound.beep();
			running = true ;
			called = true;
			//stop another instance of USListener from being called 
			UltrasonicPoller.disableULinsteners();

			//stop the motor 
			Driver.getInstance().motorStop();
			try{Thread.sleep(500);} catch (Exception e){};
			
			//if the next waypoint is on a block (25 cm near) then remove this waypoint and move to the next 
			int tolerance = 42; 
			if (new Coordinate(Odometer.getInstance().getX(),Odometer.getInstance().getY(),0)
								.isNear(Lab5P2.getWayPoints().peek(),tolerance)){
				Lab5P2.getWayPoints().pop();
			}
			
			Lab5P2.action1(UltrasonicPoller.getInstance());
				
			running = false ;
		}
	}

	@Override
	public int getDistanceOnInvoke() {
		return distanceOnInvoke;
	}

	@Override
	public UltrasonicListener setDistanceOnInvoke(int distance) {
		distanceOnInvoke = distance;
		return this;
	}

	@Override
	public boolean isContinuous() {
		return continuous;
	}

	@Override
	public UltrasonicListener setContinuous(boolean continuous) {
		this.continuous = continuous;
		return this;
	}

	@Override
	public boolean isCalled() {
		
		return called;
	}

	
	public boolean isRunning() {
		return running;
	} 
	
	@Override
	public UltrasonicListener setCalled(boolean called) {
		this.called = called;
		return this;
	}

}
