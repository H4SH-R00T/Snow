package snow;

public class Snow{
	final static String version = "3.0.1";	//Version of the program
	
	static Machine machine;	//It contains the robot object and much more
	static Thread thread; //Thread of the machine

	public static void main(String[] args) {
		//To show the window
		Window window = new Window();	//The GUI
		window.frame.setVisible(true);
		
		machine = new Machine();
		thread = new Thread(machine);
		thread.start();	//Calls the run method of the object machine
		
		window.button2();
	}
	
	

}
