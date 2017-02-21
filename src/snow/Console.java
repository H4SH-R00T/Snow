package snow;



import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.Font;
import java.awt.TextArea;
import java.awt.Color;

public class Console implements KeyListener{	//The window of the console 

	public JFrame frame;
	public static String log = "";	//Log of console
	
	private JTextField textField;
	private TextArea textArea1;
	
	/**
	 * Create the application.
	 */
	public Console() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("static-access")
	private void initialize() {
		
		
		//Creation of components
		frame = new JFrame("Console");
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setBounds(1010, 100, 555, 800);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		textArea1 = new TextArea();
		textArea1.setBackground(Color.BLACK);
		textArea1.setForeground(Color.GREEN);
		textArea1.setFont(new Font("Consolas", Font.PLAIN, 20));
		textArea1.setBounds(0, 0, 549, 712);
		textArea1.setEditable(false);
		frame.getContentPane().add(textArea1);
		
		textField = new JTextField();
		textField.setBackground(Color.BLACK);
		textField.setForeground(Color.GREEN);
		textField.setFont(new Font("Consolas", Font.PLAIN, 20));
		textField.setBounds(0, 718, 549, 42);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		//textField.requestFocus();
		textField.addKeyListener(this);
		
		
		
		
		
		
		Snow.machine.isUsingConsole = true;
	}

	public void writeConsole(String r){
		
		log = log + r + System.lineSeparator();
		textArea1.setText(log);
		textArea1.setCaretPosition(textArea1.getText().length());	//For autoscroll
         
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ENTER){	//Command to be executed if you press ENTER
			//Execute command
			String command = textField.getText();		//Gets the command from the textField
			writeConsole(command);
			textField.setText("");						//Deletes the text from the command line
			
			Snow.machine.executeCommand(command);		//Executes the command from the static machine contained in the Snow class
			
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void resetLog(){
		log = "";
		textArea1.setText("");
		writeConsole("Snow version " + Snow.version + " - by Caspo");
	}
}
