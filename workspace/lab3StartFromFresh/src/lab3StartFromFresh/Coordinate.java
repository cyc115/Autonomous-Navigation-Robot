package lab3StartFromFresh;

/**
 * location x , y and theata in degrees 
 * @author yuechuan
 *
 */
public class Coordinate extends Point{
	double theta;
	
	Coordinate(double x, double y , double d ){
		super (x,y);
		this.theta = d ;
	}
	
	/**
	 * in degrees 
	 * @return
	 */
	public double getTheta() {
		return theta;
	}

	public void setTheta(double position) {
		this.theta = position;
	}
	/**
	 * @test
	 * @param currentLocation (x,y,theata wrt y axes)
	 * @param nextLocation (x,y, and some bogus theata)
	 * @return the concave turning angle where the head of the
	 * robot points to the next location. where neg means counter
	 * clockwise turn and positive means clockwise turns 
	 */
	public static double calculateRotationAngle( Coordinate currentLocation
										, Coordinate nextLocation){
		double	 dX = nextLocation.getX() - currentLocation.getX()
				,dY = nextLocation.getY() - currentLocation.getY(),
				currentAngle = currentLocation.getTheta();
		
		double result = -currentAngle ;		//not yet finished 
//		double result = 0;
		if (dX > 0 ){
			if (dY > 0) 
				result += (Math.atan(dX/dY) * 180 /Math.PI);
			else // (dY <= 0) 
				result += (90 - (Math.atan(dY/dX) * 180 /Math.PI));
		}
		else if (dX < 0){
			if (dY > 0)
				result +=(-(Math.atan(dX/dY) * 180 /Math.PI) -90);
			else //(dY <=0)
				result +=(-90-(Math.atan(dY/dX) * 180 /Math.PI));
		}
		//should never reach this case, but keep it to make sure nothing goes wrong
		else if (dX==0 && dY == 0){
			result = 0 ;
		}
		
		return normalize(result);
	}
	@Override
	public String toString() {
		return "t" + theta + ",X" + getX()
				+ ",Y" + getY() ;
	}



	/**
	 * @param start
	 * @param end
	 * @return the distance between c1 and c2 using the formula sqrt(dX^2 + dY^2) 
	 */
	public static double calculateDistance(Coordinate start , Coordinate end){
		return Math.sqrt( sqr(end.getY() - start.getY()) + sqr(end.getX() - start.getX())); 
	}
	
	
	public static double sqr(double x){
		return x* x;
	}
	/**
	 * normalize angle in degress 
	 * @param angle
	 * @return cancave angle in deg 
	 */
	public static double normalize (double angle ){
		double normalized = angle;
		if (angle > 180){
			normalized = (-360+ angle);
		}
		else if (angle <-180){
			normalized = 360 + angle;
		}
		return normalized;
	}
	
}
