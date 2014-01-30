import lejos.nxt.LightSensor;


public class LightSenseLine extends Thread{

	private final LightSensor lightSensor;
	
	LightSenseLine(LightSensor lightSensor){
		this.lightSensor = lightSensor;
	}
	
	public void run(){
		while (true){
			lightSensor.readNormalizedValue() ;
		}
	}
}
