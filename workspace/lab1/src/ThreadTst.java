
public class ThreadTst implements Runnable {
//	Object o = new Object();
	int counter = 0;
	int id = 0 ;
	
	ThreadTst(int id){
		this.id = id ;
	}
	
	public static void incrementCounter(ThreadTst tt){
		tt.counter ++ ;
		System.out.println("[" + tt.id  + "]\t"+ tt.counter);
	}
	
	public void run (){
		incrementCounter(this);
			
	}
	
	public static void main(){
		
		ThreadTst a = new ThreadTst(1);
		ThreadTst b = new ThreadTst(2);
		a.run();
		b.run();
	}
	
}
