package hello;

public class O1 implements Runnable {
	Boolean lock ;
	
	O1(Boolean lock){
		this.lock = lock;
	}
	@Override
	public void run() {
		while (true){
		synchronized (lock){
			while (lock){
				System.out.println("LOOOOOOOOOOOOOOOOOOG");
				lock = false ;
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
