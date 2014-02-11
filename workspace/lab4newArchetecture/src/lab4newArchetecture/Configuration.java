package lab4newArchetecture;

import lejos.nxt.comm.RConsole;

public class Configuration extends AbstractConfig{
	
	/**
	 * set the default location ans open RConsole.
	 * 
	 */
	public Configuration (){
		setCurrentLocation(new Coordinate(0, 0, 0));
		setStartLocation(new Coordinate(0, 0, 0));
		RConsole.openUSB(1000);
	}
	private static Configuration config = new Configuration();
	
	public static AbstractConfig getInstance() {

		return config;
	}

	@Override
	public void leftButtonPressed() {
			
	}

	@Override
	public void rightButtonPressed() {
			
	}

}
