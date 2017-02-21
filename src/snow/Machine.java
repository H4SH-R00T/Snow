/*	Snow language
 * 
 * 	Functions:
 * 
 * 	mousemove-x-y;
 * 	echo-numberEchoPriority-true/on/false/off
 * 	say-string	//Will be displayed with a priority of 2
 * 	wait-x		//Waits x millis in the Snow.thread
 * 	mousepress-left/right
 * 	mouserelease-left/right
 * 	leftclick	//Left click with a delay between press and release of 1000 millis
 * 	leftclick-x //Left click with a delay between press and release of x millis
 *  rightclick	//Right click with a delay between press and release of 1000 millis
 * 	rightclick-x //Right click with a delay between press and release of x millis
 * 	keypress-c		//Presses the key c
 * 	keyrelease-c	//Releases the key c
 * 	writestring-string //Writes the string character by character
 * 
 */


package snow;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;


public class Machine implements Runnable{

	public static Robot robot;	//Object robot to do cool stuff
	public static String algorithm;
	
	
	
	
	public static boolean echo0 = true;	//Level 0 priority 	trash
	public static boolean echo1 = true;	//Level 1 priority	execution of the algorithm
	public static boolean echo2 = true;	//Level 2 priority  messages from the algorithm
	public static boolean echo3 = true;	//Level 3 priority	errors in the language
	public static boolean isUsingConsole = false;	//If the console is open
	public static int mPositionX;
	public static int mPositionY;
	
	private int iCommand;
	
	private long iterateN = 0;
	private boolean iterationConsidered = false; 
	private HashMap<String, Integer> labels = new HashMap<String, Integer>();
	private HashMap<String, String> variables = new HashMap<String, String>();
	
	private String[] regs  = new String[5];	//Clear the registers;	//Registers
	
	
	public Machine(){
		try {
			robot = new Robot();
			say("Welcome to Snow! Version " + Snow.version, 3);
			
		} catch (AWTException e) {
			
		}
		
	}

	
	
	@Override
	public void run() {
		//The machine always runs;
		
	}
	
	public void executeAlgorithm(String alg){	//Function that executes a SnowAlgorithm 	//Together with executeCommand
		//Resetting variable for new algorithm
		iterationConsidered = false; //Reset variable for the command iterate to be working
		labels.clear();	//Clear the labels for a new algorithm
		variables.clear(); //Clear the variables for a new algorithm
		
		regs = new String[5];	//Clear the registers
		regs[0]="0";
		regs[1]="0";
		regs[2]="0";
		regs[3]="0";
		regs[4]="0";
		
		String[] commands;	//String array that will contain all the commands
		String command = "";	//The command to execute
		
		boolean executeAlgorithm = true; //Remains true if there is at least one command
		alg = alg.replaceAll("[\\t\\n\\r]+",";");
		alg = alg.replaceAll("; ", ";");
		alg = alg.replaceAll(";;", ";");
		
		algorithm = alg;	//Keeps a copy of the algorithm to execute
		loadLabels();	//Before executing the algorithm it loads all the labels into the memory
		
		commands = alg.split(";");	//Splits the commands when there is a semicolon;
		
		iCommand = 0; //Index of the command to execute
		
		
		
		
		
		try{	//Checks if there is at least one command;
		command = commands[0];
		}catch(Exception e){
			executeAlgorithm = false;
			say("No code to execute", 1);
		}
		
		do{	//Executes all the commands
			try{
			command = commands[iCommand];
			} catch(Exception e){
				executeAlgorithm = false;	//No more commands->Finish algorithm
				say("Algorithm finished", 1);	//New line
				say("", 1);
			}
			
			if(executeAlgorithm){executeCommand(command);}	//Executes the command
			
			iCommand++;	//Increases the index of the command->Goes to the next command;
			
		}while (executeAlgorithm);
		
		//Algorithm finished
		
	}
	
	public void executeCommand(String com){		//Function that executes a given command;
		String comOrig = com;		//Keeps a copy of the original command
		
		
		com = substituteRegisters(com);
		com = substituteVariablesValues(com);	//Substitutes variable values instead of $variablename before executing the command
				
		
		comOrig = com;
				
		String[] parts = com.split(" ");	//Splits the command into parts
		int parameters = parts.length-1;	//All the words -1
	
		String partsnocom1 = "";	//The command withoud part[0]
		String partsnocom2 = "";	//The command withoud part[0] and withoud part[1]
		String com2 = com;			//Will not be just part[0]. com will become just part[0]
		
		try{
		com = parts[0].toLowerCase();
		say("Executing: " + (char)34 + comOrig + (char)34, 1);	//If there a command it says it's going to execute it
		}catch(Exception e){
			say("Command not found;", 3);	//No command found error
			return;
		}
		
		try{
			partsnocom1 = com2.substring(parts[0].length()+1);	
			partsnocom2 = com2.substring(parts[0].length()+1+parts[1].length()+1);				
		}catch(Exception e){}
		
		
		
		/*
		 * 	COMMANDS!!!
		 */
		
		switch(com){
		
		case "help":
			help();
			break;
			
		case "echohelp":
			echoHelp();
			break;
			
		case "mousemove":
			if (parameters==2){mouseMove(parts[1],parts[2]);}
			break;
			
		case "say":
			say(partsnocom1,2);
			break;
			
		case "echo":
			if (parameters==2){echoOO(parts[1], parts[2]);}
			break;
			
		case "wait":
			if (parameters==1){wait(parts[1]);}
			break;
			
		case "mousepress":
			if (parameters==1){mousePress(parts[1]);}
			break;
			
		case "mouserelease":
			if (parameters==1){mousePress(parts[1]);}
			break;
			
		case "leftclick":
			if (parameters==1){mousePress("left"); wait(parts[1]); mouseRelease("left");} else {mousePress("left"); wait("1000"); mouseRelease("left");}
			break;
			
		case "rightclick":
			if (parameters==1){mousePress("right"); wait(parts[1]); mouseRelease("right");} else {mousePress("right"); wait("1000"); mouseRelease("right");}
			break;
			
		case "keypress":
			if (parameters==1){keyPress(parts[1]);}
			break;
			
		case "keyrelease":
			if (parameters==1){keyRelease(parts[1]);}
			break;
			
		case "getmousecoordinates":
			getCoordinates();
			break;
			
		case "writestring":
			writeString(partsnocom2, parts[1]);
			break;
			
		case "gmc":
			getCoordinates();
			break;
			
		case "cls":
			Window.console.resetLog();
			break;
			
		case "goto":
			if (parameters==1){goTo(parts[1]);}
			break;
			
		case ":":
			//if(parameters==1){addLabel(parts[1]);}
			break;
			
		case "iterate":
			if(parameters==1){iterateNum(parts[1]);}
			break;
			
		case "execute":
			Window.execute();
			break;
			
		case "exit":
			iCommand = -10; 	//Impossible iCommand => Exits the executeAlgorithm() method
			break;
			
		case "scp":
			if(parameters==1){specialCharPress(parts[1]);}
			break;
			
		case "scr":
			if(parameters==1){specialCharRelease(parts[1]);}
			break;
			
		case "scl":
			specialCharacterList();
			break;
			
		case "if":
			ifStatement(comOrig);
			say("ahahhahaah", 1);
			break;
			
		case "var":
			if (parameters==2){variable(parts[1],parts[2]);}
			break;
			
		case "//":
			say("Comment: " + partsnocom1,1);
			break;
			
		case "getred":
			if(parameters==2){getRed(parts[1],parts[2]);}
			break;
			
		case "getgreen":
			if(parameters==2){getGreen(parts[1],parts[2]);}
			break;
			
		case "getblue":
			if(parameters==2){getBlue(parts[1],parts[2]);}
			break;
			
		case "getheight":
			regs[0] = String.valueOf(Utilities.getHeight());
			break;
			
		case "getwidth":
			regs[0] = String.valueOf(Utilities.getWidth());
			break;
			
			
		default:
			say("No command " + (char)34 + com + (char)34 + " found - Type " + (char)34 + "help" + (char)34 + " for help.", 3);		//No command found error
			break;
		}
		
		if(isUsingConsole){Window.console.writeConsole("");} //Add a new line in the console
	}

	
	
	public void loadLabels(){	//Loads all the labels in the memory
		
		String[] commands;	//String array that will contain all the commands
		String command = "";	//The command to execute
		
		commands = algorithm.split(";");	//Splits the commands when there is a semicolon;
		
		int i = 0; //Index of the command to execute in the future
		
		try{	//Checks if there is at least one command;
		command = commands[0];
		
		}catch(Exception e){
			return;		//Returns if there is no value
		}
		
		
		do{	//Looks for labels and adds them to the hashmap labels
			try{
				command = commands[i];		//Tries continuing to the next command
			} catch(Exception e){
				return;
			}
			
			String[] parts = command.split(" ");	//Splits the command
			
			command = parts[0].toLowerCase();		//Command part 0 to lowercase
			
			if (command.equals(":")){						//If the command is adding a label
				if(!labels.containsKey(parts[1])){
					labels.put(parts[1], i);		//Adds label
				}
			}
			
			i++;	//Increases the index of the command->Goes to the next command;	
		}while (true);
		
	}
	
	
	//Functions supported by SnowLanguage
	
	public void help(){	//Prints the help
		    say("",2);
			say("Snow language - by Caspo",2);
			say("",2);
			say("Functions:",2);
			say("",2);
			
			say(":-label			//To declare a label",2);
			say("$$n$	//Recalls the value of the register n	(5 registers-> n: 1-5)",2);
			say("$variablename$		//To recall the value of the variable",2);
			say("cls			//Clear screen");
			say("echohelp		//Prints the priority levels of echo",2);
			say("echo-numberEchoPriority-true/on/false/off",2);
			say("execute			//Iterates the algorithm forever",2);
			say("exit			//Stops the algorithm",2);
			say("getblue-x-y	//Saves in the register 1 the blue component of the pixel x y",2);
			say("getgreen-x-y	//Saves in the register 1 the green component of the pixel x y",2);
			say("getheight	//Saves in the register 1 the screen height",2);
			say("getmousecoordinates	//Prints mouse coordinates",2);
			say("getred-x-y		//Saves in the register 1 the red component of the pixel x y",2);
			say("getwidth	//Saves in the register 1 the screen width",2);
			say("gmc			//Prints mouse coordinates",2);
			say("goto-label		//Go back to a label",2);
			say("help 			//Prints help",2);
			say("if condition ? subroutine1 ? subroutine2	//If statement (Remember a space before ? and after ?",2);
			say("iterate-x 		//Iterates the algorithm for x times", 2);
			say("keypress-c		//Presses the key c",2);
			say("keyrelease-c		//Releases the key c",2);
			say("leftclick			//Left click with a delay between press and release of 1000 millis",2);
			say("leftclick-x 		//Left click with a delay between press and release of x millis",2);
			say("mousemove-x-y;	//Mouse moves at x and y",2);
			say("mousepress-left/right	//L/R button of mouse pressed",2);
			say("mouserelease-left/right	//L/R button of mouse released",2);
			say("rightclick		//Right click with a delay between press and release of 1000 millis",2);
			say("rightclick-x 		//Right click with a delay between press and release of x millis",2);
			say("say-string		//Will be displayed with a priority of 2",2);
			say("scl			//special characters list",2);
			say("scp-special character	//Special character press",2);
			say("scr-special character	//Special character release",2);
			say("var-variable-value	//Changes the value of the variable",2);
			say("wait-x			//Waits x millis in the Snow.thread",2);
			say("writestring-x-string 	//Writes the string character by character; for a space write ç; Delay between keypress and keyrelease of x millis (At least 15)",2);

				
			
			
			
			
			
	}
	
	public void echoHelp(){
		
		//Level 0 priority 	trash
		//Level 1 priority	execution of the algorithm
		//Level 2 priority  messages from the algorithm
		//Level 3 priority	errors in the language
		
		say("Level 0 - Trash", 2);
		say("Level 1 - Execution of the algorithm", 2);
		say("Level 2 - Messages from the algorithm", 2);
		say("Level 3 - Errors in the language", 2);
	}
	
	public void mouseMove(String p1, String p2){
		int x = Utilities.getInt(p1);
		int y = Utilities.getInt(p2);
		
		robot.mouseMove(x, y);
	}
	
	public void echoOO(String x, String oo){
		
		if (x=="on"){x = "true";}
		if (x=="off"){x = "false";}
		
		boolean t = Boolean.parseBoolean(oo);	//The new state of echo variable
		
		switch (x){
		case "0":
			echo0 = t;
			break;
		case "1":
			echo1 = t;
			break;
		case "2":
			echo2 = t;
			break;
		case "3":
			echo3 = t;
			break;
		
		}
	}
	
	public static void say(String s, int p){	//Simpler function that System.out.println(String) with 4 levels of priority;
		if(p==0 && echo0){System.out.println(s);if(isUsingConsole){Window.console.writeConsole(s);}}	
		if(p==1 && echo1){System.out.println(s);if(isUsingConsole){Window.console.writeConsole(s);}}	
		if(p==2 && echo2){System.out.println(s);if(isUsingConsole){Window.console.writeConsole(s);}}	
		if(p==3 && echo3){System.out.println(s);if(isUsingConsole){Window.console.writeConsole(s);}}	
	}

	@SuppressWarnings("static-access")
	public void wait(String t){					//Waits for t millis
		int time = Utilities.getInt(t);
		try{
			Snow.thread.sleep(time);
		}catch(Exception e){
			
		}
	}
	
	public void mousePress(String button){		//Press the left or right button of the mouse
		if (button=="left"){
			robot.mousePress(InputEvent.BUTTON1_MASK);
		}else if (button=="right"){
			robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		}
	}
	
	public void mouseRelease(String button){	//Release the left or right button of the mouse
		if (button=="left"){
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}else if (button=="right"){
			robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		}
	}
	
	public void keyPress(String k){
		
		char c = k.charAt(0);	//Gets the char
		
		
		int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
		robot.keyPress(keyCode);	//The robot presses that key
		
	}
	
	public void keyPress(int k){
		robot.keyPress(k);
	}
	
	public void keyRelease(String k){
			char c = k.charAt(0);	//Gets the char
		
			
			int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
			
			robot.keyRelease(keyCode);
	}
	
	public void keyRelease(int k){
		robot.keyRelease(k);
	}
	
	public void writeString(String s, String w){	//To write a string character by character
	    s = s.replaceAll("ç", " ");		//Adds the spaces
		for (int i = 0; i < s.length(); i++) {
	        char c = s.charAt(i);
	        
	        if (Character.isUpperCase(c)) {	//If there is a capitalized character
	            robot.keyPress(KeyEvent.VK_SHIFT);
	        }
	        
	        wait(w);
	        
	        robot.keyPress(Character.toUpperCase(c));
	        
	        
			wait(w);
			
	        
	        robot.keyRelease(Character.toUpperCase(c));

	        wait(w);
	        
	        if (Character.isUpperCase(c)) {
	            robot.keyRelease(KeyEvent.VK_SHIFT);
	        }
	        
	        wait(w);
	    }
	}
	
	/*public void writeString(String s){	//To write a string character by character
	    for (int i = 0; i < s.length(); i++) {
	        char c = s.charAt(i);
	       
	        if (Character.isUpperCase(c)) {	//If there is a capitalized character
	            robot.keyPress(KeyEvent.VK_SHIFT);
	        }
	        
	        robot.keyPress(Character.toUpperCase(c));
	        
	        
			wait("5");
			
	        
	        robot.keyRelease(Character.toUpperCase(c));

	        if (Character.isUpperCase(c)) {
	            robot.keyRelease(KeyEvent.VK_SHIFT);
	        }
	        
	        wait("5");
	    }
}
	*/
	
	public void getCoordinates(){
		mPositionX = MouseInfo.getPointerInfo().getLocation().x;
		mPositionY = MouseInfo.getPointerInfo().getLocation().y;
		String t1 = String.valueOf(mPositionX);
		String t2 = String.valueOf(mPositionY);
		say("x: " + t1, 2);
		say("y: " + t2, 2);
	}
	
	
	public void iterateNum(String n){
		
		if (!iterationConsidered){int volte = Utilities.getInt(n);iterateN = volte;iterationConsidered = true;}
		
		if (iterateN>1){iCommand = (-1);iterateN--;}
	}
	
	/*public void addLabel(String l){	//Adds a label for the goto statement
		if(!labels.containsKey(l)){
			labels.put(l, iCommand);
		}
	}*/
	
	public void goTo(String l){
		if(labels.containsKey(l)){	//Returns to the label
			iCommand = labels.get(l)-1;
		}
	}
	
	public void specialCharPress(String character){
		
		int k;
		
		
		switch(character){
		case "enter":
			k = KeyEvent.VK_ENTER;
			break;
			
		case "shift":
			k = KeyEvent.VK_SHIFT;
			break;
		
		case "ctrl":
			k = KeyEvent.VK_CONTROL;
			break;
			
		case "alt":
			k = KeyEvent.VK_ALT;
			break;
			
		case "altgraph":
			k = KeyEvent.VK_ALT_GRAPH;
			break;
			
		case "esc":
			k = KeyEvent.VK_ESCAPE;
			break;
		
		case "backspace":
			k = KeyEvent.VK_BACK_SPACE;
			break;
		
		case "asterisk":
			k = KeyEvent.VK_ASTERISK;
			break;
			
		case "f1":
			k = KeyEvent.VK_F1;
			break;
		case "f2":
			k = KeyEvent.VK_F2;
			break;
		case "f3":
			k = KeyEvent.VK_F3;
			break;
		case "f4":
			k = KeyEvent.VK_F4;
			break;
		case "f5":
			k = KeyEvent.VK_F5;
			break;
		case "f6":
			k = KeyEvent.VK_F6;
			break;
		case "f7":
			k = KeyEvent.VK_F7;
			break;
		case "f8":
			k = KeyEvent.VK_F8;
			break;
		case "f9":
			k = KeyEvent.VK_F9;
			break;
		case "f10":
			k = KeyEvent.VK_F10;
			break;
			
		case "delete":
			k = KeyEvent.VK_DELETE;
			break;
			
		case "tab":
			k = KeyEvent.VK_TAB;
			break;
		
		case "space":
			k = KeyEvent.VK_SPACE;
			break;
			
		case "slash":
			k = KeyEvent.VK_SLASH;
			break;
			
		case "windows":
			k = KeyEvent.VK_WINDOWS;
			break;
			
		case "leftarrow":
			k = KeyEvent.VK_LEFT;
			break;
		
		case "downarrow":
			k = KeyEvent.VK_DOWN;
			break;
		
		case "uparrow":
			k = KeyEvent.VK_UP;
			break;
		
		case "rightarrow":
			k = KeyEvent.VK_RIGHT;
			break;
			
		default:
			k = KeyEvent.VK_0;
			break;
		}
		
		robot.keyPress(k);
		
	}
	
	public void specialCharRelease(String character){
		int k;
		
		
		switch(character){
		case "enter":
			k = KeyEvent.VK_ENTER;
			break;
			
		case "shift":
			k = KeyEvent.VK_SHIFT;
			break;
		
		case "ctrl":
			k = KeyEvent.VK_CONTROL;
			break;
			
		case "alt":
			k = KeyEvent.VK_ALT;
			break;
			
		case "altgraph":
			k = KeyEvent.VK_ALT_GRAPH;
			break;
			
		case "esc":
			k = KeyEvent.VK_ESCAPE;
			break;
		
		case "backspace":
			k = KeyEvent.VK_BACK_SPACE;
			break;
		
		case "asterisk":
			k = KeyEvent.VK_ASTERISK;
			break;
			
		case "f1":
			k = KeyEvent.VK_F1;
			break;
		case "f2":
			k = KeyEvent.VK_F2;
			break;
		case "f3":
			k = KeyEvent.VK_F3;
			break;
		case "f4":
			k = KeyEvent.VK_F4;
			break;
		case "f5":
			k = KeyEvent.VK_F5;
			break;
		case "f6":
			k = KeyEvent.VK_F6;
			break;
		case "f7":
			k = KeyEvent.VK_F7;
			break;
		case "f8":
			k = KeyEvent.VK_F8;
			break;
		case "f9":
			k = KeyEvent.VK_F9;
			break;
		case "f10":
			k = KeyEvent.VK_F10;
			break;
			
		case "delete":
			k = KeyEvent.VK_DELETE;
			break;
			
		case "tab":
			k = KeyEvent.VK_TAB;
			break;
		
		case "space":
			k = KeyEvent.VK_SPACE;
			break;
			
		case "slash":
			k = KeyEvent.VK_SLASH;
			break;
			
		case "windows":
			k = KeyEvent.VK_WINDOWS;
			break;
			
		case "leftarrow":
			k = KeyEvent.VK_LEFT;
			break;
		
		case "downarrow":
			k = KeyEvent.VK_DOWN;
			break;
		
		case "uparrow":
			k = KeyEvent.VK_UP;
			break;
		
		case "rightarrow":
			k = KeyEvent.VK_RIGHT;
			break;
			
		default:
			k = KeyEvent.VK_0;
			break;
		}
		
		robot.keyRelease(k);
		
		
	}
	
	public void specialCharacterList(){
		say("Special characters list",2);
		
		say("",2);
		
		say("alt",2);
		say("altgraph",2);
		say("asterisk",2);
		say("backspace",2);
		say("ctrl",2);
		say("delete",2);
		say("downarrow",2);
		say("enter",2);
		say("esc",2);
		say("f1, f2, f3, f4, f5, f6, f7, f8, f9, f10",2);
		say("leftarrow",2);
		say("rightarrow",2);
		say("shift",2);
		say("space",2);
		say("slash",2);
		say("uparrow",2);
		say("tab",2);
		say("windows",2);
		
	}
	
	public void ifStatement(String com){
		say(com, 1);
		String command = com.replaceAll("if ", "");	//Removes the if parts
		
		String parts[];
		parts = command.split(" \\? ");
		
		int len = parts.length;
		
		if (len<2){return;}	//Exits if statement if there is just a condition
		
		say("", 2);
		say("Condition: " + (char)34 + parts[0] + (char)34, 1);
		say("Then: " + (char)34 + parts[1] + (char)34, 1);
		if (len>1){say("Else: " + (char)34 + parts[2] + (char)34, 1);}
		
		//Check condition
		boolean con = Utilities.getCondition(parts[0]);
		say("",1);
		say("Condition: " + con, 1);
		
		if (con) {say("THEN",1);subRoutine(parts[1]);}	//Executes the first subroutine			THEN
		else if (!con){if (len>1){say("ELSE",1);subRoutine(parts[2]);}}	//Executes the second subroutine	ELSE
	}
	
	
	public void subRoutine(String alg){	// An executeAlgorithm method modified for subroutines (Like the ones contained in if statements)
		
		String[] commands;	//String array that will contain all the commands
		String command = "";	//The command to execute
		
		
		alg = alg.replaceAll("[\\t\\n\\r]+",";");
		alg = alg.replaceAll("; ", ";");
		alg = alg.replaceAll(";;", ";");
		
		//loadLabels();	//Before executing the algorithm it loads all the labels into the memory //In the soubroutines no labels allowed
		
		commands = alg.split(";");	//Splits the commands when there is a semicolon;
		
		int i = 0;	//iCommand for the subroutine
		
		
		
		try{	//Checks if there is at least one command;
		command = commands[0];
		}catch(Exception e){
			return;
		}
		
		
		
		
		
		while(true){	//Executes all the commands of the subroutine
			try{
			command = commands[i];
			} catch(Exception e){
				return;	//No more commands->Finish subroutine
			}
			
			executeCommand(command);	//Executes the command of the subroutines
			
			i++;	//Increases the index of the command->Goes to the next command;
			
		}
		
		//Algorithm finished
		

	}
	
	public void variable(String variable, String value){
	
		say("Variable: " + variable, 1);
		say("New value: " + value, 1);
		
		if (variables.containsKey(variable)){	//Variable already used
			variables.remove(variable);	//Removes the variable
			variables.put(variable, value);
		}else{	//Variable not used yet -> Add the variable in the HashMap variables
			variables.put(variable, value);
		}
		
		
	}
	
	public void getRed(String x, String y){
		int c = robot.getPixelColor(Utilities.getInt(x), Utilities.getInt(y)).getRed();
		regs[0] = Integer.toString(c);
	}
	
	public void getGreen(String x, String y){
		int c = robot.getPixelColor(Utilities.getInt(x), Utilities.getInt(y)).getRed();
		regs[0] = Integer.toString(c);
	}

	public void getBlue(String x, String y){
		int c = robot.getPixelColor(Utilities.getInt(x), Utilities.getInt(y)).getRed();
		regs[0] = Integer.toString(c);
	}

	public String substituteVariablesValues(String com){
		while (com.contains("$")){	//Going to replace with the value of the variable
			int iDollar = com.indexOf("$");		//Index of dollar	where the name of the variable to replace starts
			
			String nameVar = "";	//It will be built with the name of the variable
			
			for (int i = iDollar+1; i<com.length(); i++){
				if (com.charAt(i)==' ' || com.charAt(i)==36){break;}	//The name of the variable finished when there is a space
				
				nameVar += com.charAt(i);
			}
						
			say("Substitution " + nameVar + " value: " + variables.get(nameVar), 1);
			say("$" + nameVar + "$",1);
			
			if(variables.containsKey(nameVar)){com = com.replaceAll("\\$" + nameVar + "\\$", variables.get(nameVar));}
			say(com);
		}
		return com;
	}
	
	public String substituteRegisters(String com){
				
		if (com.contains("$$1$")){com = com.replaceAll("\\$\\$1\\$", regs[0]);}
		if (com.contains("$$2$")){com = com.replaceAll("\\$\\$2\\$", regs[1]);}
		if (com.contains("$$3$")){com = com.replaceAll("\\$\\$3\\$", regs[2]);}
		if (com.contains("$$4$")){com = com.replaceAll("\\$\\$4\\$", regs[3]);}
		if (com.contains("$$5$")){com = com.replaceAll("\\$\\$5\\$", regs[4]);}
		
		
		
		return com;
	}
	
	
	
	
	
	//Other functions of the machine like say();
	
	public static void say(String s){	//Simpler function that System.out.println(String);
		if(echo0){System.out.println(s);if(isUsingConsole){Window.console.writeConsole(s);}}	
	}
	
	public static void say(int s){	//Simpler function that System.out.println(Int);
		if(echo0){System.out.println(s);if(isUsingConsole){Window.console.writeConsole(Integer.toString(s));}}	
	}



	
}
