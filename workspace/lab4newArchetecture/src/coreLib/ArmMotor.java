package coreLib;

import lejos.nxt.NXTRegulatedMotor;

public class ArmMotor {
	private static AbstractConfig config ;
	public static final NXTRegulatedMotor ARM_M= AbstractConfig.SENSOR_MOTOR;
	static int movementDeg = 90 ;
	private static boolean isOpen = false ;
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
		if(!isOpen){
			ARM_M.rotate(-movementDeg,false);
			isOpen = true;
		}
	}
	public static void close (){
		if (isOpen){
			ARM_M.rotate(movementDeg,false);	
			isOpen = false ;
		}
	}
}
