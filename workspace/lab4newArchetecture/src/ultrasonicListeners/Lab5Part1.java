package ultrasonicListeners;

import coreLib.AbstractConfig;
import coreLib.Configuration;
import coreLib.Driver;
import coreLib.LCDWriter;
import coreLib.UltrasonicListener;
import coreLib.UltrasonicPoller;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.robotics.Color;

public class Lab5Part1 implements UltrasonicListener{
	//following 3 vars are used for UltrasonicListeners
	private int distanceOnInvoke;
	private boolean continuous;
	private boolean called ;

	public Lab5Part1(int distanceOnInvoke , boolean called ) {
		this.distanceOnInvoke = distanceOnInvoke;
		this.called = called;
	}
	
	
	public static void main(String [] args){
		AbstractConfig config = Configuration.getInstance();
		UltrasonicPoller uspoller = UltrasonicPoller.getInstance();
		LCDWriter.getInstance().start();
		LCDWriter lcd = LCDWriter.getInstance();
		uspoller.start();
		
		Lab5Part1 lab5 = new Lab5Part1(7, false);
		lcd.writeToScreen("press start", 0);
		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		Driver.setSpeed(config.getForwardSpeed());
		Driver.getInstance().motorForward();
		uspoller.subscribe(lab5);

		while(Button.waitForAnyPress() != Button.ID_ESCAPE){}
		
		System.exit(0);
	}

	@Override
	public void ultrasonicDistance(int distanceFromObsticle) {
		Driver.getInstance().motorStop();
		LCDWriter.getInstance().writeToScreen("see smt["+ distanceFromObsticle + "]", 3);
		ColorSensor cs = new ColorSensor(AbstractConfig.LIGHT_SENSOR_PORT);
		cs.setFloodlight(true);
		
		while(true){
			Color c = cs.getColor();
			LCDWriter.getInstance().writeToScreen( UltrasonicPoller.getInstance().getDistance() +"" , 2);
			LCDWriter.getInstance().writeToScreen("B" +c.getBlue() , 6);
			LCDWriter.getInstance().writeToScreen("R" +c.getRed() , 5);
			LCDWriter.getInstance().writeToScreen("G" +c.getGreen() , 4);
			LCDWriter.getInstance().writeToScreen((((double)c.getRed()/c.getBlue() >1.2) ? "brick" : "the other thing" ), 2);
			try{Thread.sleep(200);}catch(Exception e){};
		}
	}
	
	public int getDistanceOnInvoke() {
		return distanceOnInvoke;
	}

	public UltrasonicListener setDistanceOnInvoke(int distanceOnInvoke) {
		this.distanceOnInvoke = distanceOnInvoke;
		return this;
	}

	public boolean isContinuous() {
		return continuous;
	}

	public UltrasonicListener setContinuous(boolean continuous) {
		this.continuous = continuous;
		return this;
	}

	public boolean isCalled() {
		return called;
	}

	public UltrasonicListener setCalled(boolean called) {
		this.called = called;
		return this;
	}
}
	
	
