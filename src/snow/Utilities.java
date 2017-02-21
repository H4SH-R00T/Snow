package snow;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Utilities {
	public static int getInt(String s){
		return Integer.parseInt(s);
	}
	
	public static boolean getCondition(String condition){
		String[] parts;
		String operator = "";
		boolean returno = false;
		
		if (condition.contains("==")){operator = "==";}
		if (condition.contains("!=")){operator = "!=";}
		if (condition.contains(">")){operator = ">";}
		if (condition.contains("<")){operator = "<";}
		if (condition.contains(">=")){operator = ">=";}
		if (condition.contains("<=")){operator = "<=";}
		
		parts = condition.split(operator);
		
		
		switch (operator){
		case "==":
			if (parts[0].equals(parts[1])){returno = true;}
			break;
			
		case "!=":
			returno = (!parts[0].equals(parts[1]));
			break;
			
		case ">":
			returno = (getInt(parts[0])>getInt(parts[1]));
			break;
			
		case "<":
			returno = (getInt(parts[0])<getInt(parts[1]));
			break;
			
		case ">=":
			returno = (getInt(parts[0])>=getInt(parts[1]));
			break;
			
		case "<=":
			returno = (getInt(parts[0])<=getInt(parts[1]));
			break;
		}
		
		return returno;
	}

	public static int getWidth(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();

		return (int) width;
	}

	public static int getHeight(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		
		return (int) height;
	}

}
