package client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;

public class UI {
	
	private static JFrame theUI; 
	private static JMenuBar theMenuBar;
	private static JTextPane scrollArea;
	private static JTextArea textInputArea;
	
	private static Color menuBarColour;
	private static Color textInputAreaColour;
	private static Color textColour;
	
	private static Font textFont;
	
	private static Dimension menuBarDimension;
	private static Dimension mainAreaDimension;
	private static Dimension textInputDimension;

	private static KeyListener listenEnter;

	public static void createAndShowGUI() {
		theUI = new JFrame("wetDirtUI");
		theUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textColour = new Color(98, 252, 56); 
		textInputAreaColour = new Color(0, 0, 0); //black
		menuBarColour = new Color(153, 60, 240);
				
		textFont = new Font("myFont", Font.BOLD, 14);
		
		menuBarDimension = new Dimension(600, 45);
		mainAreaDimension = new Dimension(600, 900);
		textInputDimension = new Dimension(600, 20);
		
		listenEnter = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				activateKeyEvent(e, "RELEASED");
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		
		theMenuBar = new JMenuBar();
		theMenuBar.setOpaque(true);
		theMenuBar.setBackground(menuBarColour);
		theMenuBar.setPreferredSize(menuBarDimension);
		theMenuBar.setName("Possible Commands: ");
		theMenuBar.setToolTipText("Possible Commands are: \n Move &Direction& \n Look \n Examine");
		
		scrollArea = new JTextPane();
		scrollArea.setPreferredSize(mainAreaDimension);
		scrollArea.setEditable(false);
		scrollArea.setBackground(textInputAreaColour);
		scrollArea.setBorder(BorderFactory.createLineBorder(Color.white));

		textInputArea = new JTextArea();
		textInputArea.setPreferredSize(textInputDimension);
		textInputArea.setBackground(textInputAreaColour);
		textInputArea.setForeground(textColour);
		textInputArea.setFocusable(true);
		textInputArea.setEditable(true);
		textInputArea.setFont(textFont);
		textInputArea.setBorder(BorderFactory.createLineBorder(Color.white));
		textInputArea.addKeyListener(listenEnter);
		
		
		theUI.setJMenuBar(theMenuBar);
		theUI.getContentPane().add(scrollArea, BorderLayout.CENTER);
		theUI.getContentPane().add(textInputArea, BorderLayout.PAGE_END);
		theUI.setTitle("Wet_Dirt - The Multiplayer Text Adventure Game");
		theUI.requestFocusInWindow();
		textInputArea.requestFocusInWindow();
		
		theUI.pack();
		theUI.setVisible(true);
	}
	
	public void runGUI() {
		while (true) {
			
		}
	}
    
    public static void activateKeyEvent(KeyEvent e, String keyStatus) {
    	int id = e.getID();
    	String inputString = "";
    	if (id == KeyEvent.KEY_RELEASED) {
    		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    			Document theInputDoc = textInputArea.getDocument();
    			StyledDocument theOutputDoc = scrollArea.getStyledDocument();
    			try {
					inputString = theInputDoc.getText(0, theInputDoc.getLength());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
    			addToOutput(inputString);
    		}
    		
    	}
    }
    
    public static void addToOutput(String inputString) {
    	inputString = inputString + "\n";
		StyledDocument theOutputDoc = scrollArea.getStyledDocument();
		try {
			theOutputDoc.insertString(theOutputDoc.getLength(), inputString, null);
		} catch (BadLocationException e1) {
			
		}
    }
	
	public UI() {
		createAndShowGUI();
	}

}
