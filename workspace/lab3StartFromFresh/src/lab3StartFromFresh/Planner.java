package lab3StartFromFresh;

public interface Planner {
	void setConfiguration (RobotConfiguration config);
	RobotConfiguration getConfiguration();
	void start();
	boolean wallFollow();
}
