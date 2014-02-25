package coreLib;

import lejos.nxt.comm.RConsole;


/**
 * location x , y and theata in degrees 
 * @author yuechuan
 *
 */
public class Coordinate extends Point{
	double theta;
	
	public Coordinate(double x, double y , double d ){
		super (x,y);
		this.theta = d ;
	}
	
	/**
	 * theta is in <b> RAD </b>
	 * @return
	 */
	public double getTheta() {
		return theta;
	}

	public Coordinate setTheta(double position) {
		this.theta = position;
		return this;
	}
	
	

	@Override
	public String toString() {
		return "t" + String.valueOf(Math.toDegrees(getTheta())).substring(0, 3) + ",X" + String.valueOf(getX()).substring(0, 3) 
				+ ",Y" + String.valueOf(getY()).substring(0, 3);
	}
	

	public String toString2() {
		return "t" + String.valueOf(Math.toDegrees(getTheta())) + ",X" + String.valueOf(getX())
				+ ",Y" + String.valueOf(getY());
	}

	
	/**
	 * @param currentLocation (x,y,theata wrt y axes)
	 * @param nextLocation (x,y, and some bogus theata)
	 * @return the concave turning angle where the head of the
	 * robot points to the next location. where neg means counter
	 * clockwise turn and positive means clockwise turns <b> in deg</b>
	 */
		public static double calculateRotationAngle( Coordinate currentLocation
			, Coordinate nextLocation){
		double	 dX = nextLocation.getX() - currentLocation.getX()
				,dY = nextLocation.getY() - currentLocation.getY(),
				/**
				 * in degree 
				 */
				currentAngle = Math.toDegrees(currentLocation.getTheta());
		
		RConsole.println("dX" + dX);
		RConsole.println("dY" + dY);
		
		double result = -currentAngle ;
		
		
		//double result = 0;
		if (dX >= 0 ){
			if (dY >= 0) 
				result += (Math.atan(dX/dY) * 180 /Math.PI);
			else // (dY <= 0) 
				result += (90 - (Math.atan(dY/dX) * 180 /Math.PI));
		}
		else if (dX < 0){
			if (dY >= 0)
				result +=(Math.atan(dX/dY) * 180 /Math.PI) ;
//			result +=(-(Math.atan(dX/dY) * 180 /Math.PI) -90);
			else //(dY <=0)
				result +=(-90-(Math.atan(dY/dX) * 180 /Math.PI));
		}
		//should never reach this case, but keep it to make sure nothing goes wrong
		else if (dX==0 && dY == 0){
			result = 0 ;
		}
	
	return normalize(result);
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
	 * @param angle in deg
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
	
	public double calcSlop(Coordinate c){
		return (c.getY() - this.getY()) / (c.getX() - this.getX());
	}
	
	public Coordinate clone(){
		return new Coordinate(this.getX(),this.getY() , this.getTheta());
	}
	
	/**
	 * return true if current coordinate is near the given coordinate 
	 * @param c 
	 * @return true if c is near the given Coordination
	 */
	public boolean isNear(Coordinate c) {
		double deltaDist = (this.getX()-c.getX()) *(this.getX()-c.getX()) 
				+ (this.getY()-c.getY()) *(this.getY()-c.getY()) ;
		return deltaDist <= 10 ? true : false ;
	}
	
}
