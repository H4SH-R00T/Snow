package snow;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.TextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;

public class Window implements KeyListener{

	public JFrame frame;
	public static Console console;
	private static TextArea textArea;
	
	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Snow");
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setBounds(100, 100, 910, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		textArea = new TextArea();
		textArea.setForeground(Color.GREEN);
		textArea.setBackground(Color.BLACK);
		textArea.setFont(new Font("Consolas", Font.PLAIN, 20));
		textArea.setBounds(10, 10, 582, 724);
		frame.getContentPane().add(textArea);
		
		JButton btnNewButton = new JButton("Run");
		btnNewButton.setBackground(Color.BLACK);
		btnNewButton.setForeground(Color.GREEN);
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				execute();
			}
		});
		btnNewButton.setBounds(598, 10, 282, 70);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnConsole = new JButton("Console");
		btnConsole.setForeground(Color.GREEN);
		btnConsole.setBackground(Color.BLACK);
		btnConsole.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnConsole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button2();
			}
		});
		btnConsole.setBounds(598, 132, 282, 70);
		frame.getContentPane().add(btnConsole);
		
		JButton btnImport = new JButton("Import...");
		btnImport.setForeground(Color.GREEN);
		btnImport.setBackground(Color.BLACK);
		btnImport.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				button3();
			}
		});
		btnImport.setBounds(598, 589, 282, 70);
		frame.getContentPane().add(btnImport);
		
		JButton btnExport = new JButton("Export...");
		btnExport.setForeground(Color.GREEN);
		btnExport.setBackground(Color.BLACK);
		btnExport.setFont(new Font("Tahoma", Font.PLAIN, 24));
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button4();
			}
		});
		btnExport.setBounds(598, 664, 282, 70);
		frame.getContentPane().add(btnExport);
		
		
		
		
		frame.addKeyListener(this);	//Adds a keylistener for the window
		
	}

	
	
	
	//Window elements methods
	public static void execute(){	//Method for button1
		String alg;
		alg = textArea.getText();
		Snow.machine.executeAlgorithm(alg);
	}
	
	public void button2(){
		console = new Console();
		console.frame.setVisible(true);
		Snow.machine.executeCommand("mousemove 1040 860");
		Snow.machine.executeCommand("leftclick 5");
		console.resetLog();
		
	}
	
	public void button3(){
		//Import algorithm in .alg
		JFileChooser openFile = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("ALGORITHM FILES", "alg", "algorithm");
		openFile.setFileFilter(filter);
        openFile.showOpenDialog(null);
        
        String path;
        String alg = "";
        
        
        path = openFile.getSelectedFile().getPath();
        
        
        try{
        	BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));

        	String str;

     		while ((str = in.readLine()) != null) {
     		    alg = alg + str + System.lineSeparator();
     		}

            in.close();
     	    
        }catch(Exception e){Machine.say("Couldn't import the algorithm file", 3);}
        
		
		
		
		
		if (!(alg == "")){alg = alg.replaceAll("ç", System.lineSeparator());textArea.setText(alg);}
        
	}
	
	public void button4(){
		//Import algorithm in .alg
		String path;
		String alg = "";
		
		JFileChooser saveFile = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("ALGORITHM FILES", "alg", "algorithm");
		saveFile.setFileFilter(filter);
        saveFile.showSaveDialog(null);
        
        path = saveFile.getSelectedFile().getPath();
        
        alg = textArea.getText();
        alg = alg.replaceAll("[\\t\\n\\r]+","ç");
        alg = alg.replaceAll("; ", ";");
		alg = alg.replaceAll(";;", ";");
		
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "utf-8"))) {
        	writer.write(alg);
        	writer.close();
        }catch(Exception e){
        	Machine.say("Couldn't export the algorithm file", 3);
        }
		
	}
	
	
	//KeyListener methods:
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("hey");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
