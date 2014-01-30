package lab3;

public class Coordinate {
	private double x , y ;
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}

	Coordinate(double x , double y){
		
	}
	
	public Coordinate() {
		x = -1 ;
		y = -1 ;
	}

	static double calculateDist(Coordinate a , Coordinate b){
		double dist = Math.sqrt( (a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y));
		return dist;
	}
	static Coordinate calculateDifference(Coordinate a , Coordinate b ){
		return new Coordinate (a.x - b.x , a.y - b.y);
	}
	
	public String toString(){
		return "x:" + x + " y:" + y ;
	}
}
