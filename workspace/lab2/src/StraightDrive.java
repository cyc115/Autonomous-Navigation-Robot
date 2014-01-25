import lejos.nxt.*;



public class StraightDrive {
	
	static final int FORWARD_SPEED = 250;
	
	public static void main(String [] args){
		if (Button.waitForAnyPress() == Button.ID_LEFT){
			drive();
		}
	}
	
	static int rotation = 360 * 5 ;
	
	static NXTRegulatedMotor l = Motor.A;
	static NXTRegulatedMotor r = Motor.B;
	
	static void drive(){
		l.stop();
		r.stop();
		
		l.setSpeed(FORWARD_SPEED);
		r.setSpeed(FORWARD_SPEED);
		
		l.rotate(rotation, true);
		r.rotate(rotation,false);
		
		while(Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
				
	}
}
