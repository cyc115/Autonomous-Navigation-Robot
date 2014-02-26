package coreLib;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;

public class ArmMotor {
	private static AbstractConfig config ;
	public static final NXTRegulatedMotor ARM_M= AbstractConfig.SENSOR_MOTOR;
	static int movementDeg = 180 ;
	private static boolean isOpen = true ;
	//	private static ArmMotor arm = null;
	
	private ArmMotor(AbstractConfig conf){
		config = conf;
	}
//	public ArmMotor getInstance(){
//		if (arm == null ){
//			arm = new ArmMotor(AbstractConfig.getInstance());
//		}
//		return arm ;
//	}
	

	public static void open(){
		Sound.beep();
		if(!isOpen){
			ARM_M.rotate(-movementDeg,false);
			isOpen = true;
		}
	}
	public static void close (){
		Sound.beep();
		if (isOpen){
			ARM_M.rotate(movementDeg,false);	
			isOpen = false ;
		}
	}
}
