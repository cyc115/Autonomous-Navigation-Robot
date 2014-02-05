package lab4;

public interface Monitor extends Runnable {
	void writeToScreen(String str ,int lineNumber);
	
}
