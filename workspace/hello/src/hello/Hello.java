package hello;

/**
 * have two objects do things after on another.
 * @author yuechuan
 *
 */
public class Hello {
	static Boolean lock = new Boolean(false);
	
	public static void main(String [] args){
		O1 o1 = new O1(lock);
		O2 o2 = new O2(lock);
		o1.start();
		o2.start();
	}
	

}
