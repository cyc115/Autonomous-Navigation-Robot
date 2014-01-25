package hello;

public class O2 implements Runnable{
	Boolean lock ; 
	
	O2(Boolean lock){
		this.lock = lock ;
	}
	
	public void run (){
		while (true){
			synchronized(lock){
				while (!lock){
					System.out.println("Short");
					lock = true;
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	Thread start(){
		Thread t = new Thread(this);
		t.start();
		return t;
	}
}
