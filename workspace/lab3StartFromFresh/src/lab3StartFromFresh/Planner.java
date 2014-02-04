package lab3StartFromFresh;

public interface Planner {
	void setConfiguration (RobotConfiguration config);
	RobotConfiguration getConfiguration();
	void start();
	public boolean isWallFollow();
	public void setWallFollow(boolean wallFollow) ;
}
