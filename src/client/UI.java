package client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

import com.sun.webkit.ContextMenu.ShowContext;

public class UI {
	
	private static JFrame theUI; 
	private static JTextArea theActionBar;
	private static JTextArea scrollArea;
	private static JTextArea textInputArea;
	
	private static Color actionBarColour;
	private static Color textInputAreaColour;
	private static Color textColour;
	
	private static Font textFont;
	
	private static Dimension menuBarDimension;
	private static Dimension mainAreaDimension;
	private static Dimension textInputDimension;

	private static KeyListener listenEnter;
	
	private static String newline = System.getProperty("line.separator");

	public static void createAndShowGUI() {
		theUI = new JFrame("wetDirtUI");
		theUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		textColour = new Color(98, 252, 56); 
		textInputAreaColour = new Color(0, 0, 0); //black
		actionBarColour = new Color(153, 60, 240);
				
		textFont = new Font("myFont", Font.BOLD, 14);
		
		menuBarDimension = new Dimension(600, 90);
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
		
		theActionBar = new JTextArea("Possible Commands are: Move $Direction$, Look $Object$ OR around, Examine $Object$, Check doors, Open/Close $Direction$ door");
		theActionBar.setOpaque(true);
		theActionBar.setBackground(actionBarColour);
		theActionBar.setPreferredSize(menuBarDimension);
		theActionBar.setName("Possible Commands: ");
		theActionBar.setToolTipText("Possible Commands are: \n Move &Direction& \n Look \n Examine");
		theActionBar.setForeground(textColour);
		theActionBar.setEditable(false);
		theActionBar.setLineWrap(true);
		theActionBar.setWrapStyleWord(true);
		
		scrollArea = new JTextArea("Welcome to Wet Dirt!" + newline);
		scrollArea.setPreferredSize(mainAreaDimension);
		scrollArea.setEditable(false);
		scrollArea.setBackground(textInputAreaColour);
		scrollArea.setFont(textFont);
		scrollArea.setForeground(textColour);
		scrollArea.setBorder(BorderFactory.createLineBorder(Color.white));
		scrollArea.setLineWrap(true);
		scrollArea.setWrapStyleWord(true);

		textInputArea = new JTextArea();
		textInputArea.setPreferredSize(textInputDimension);
		textInputArea.setBackground(textInputAreaColour);
		textInputArea.setForeground(textColour);
		textInputArea.setFocusable(true);
		textInputArea.setEditable(true);
		textInputArea.setFont(textFont);
		textInputArea.setBorder(BorderFactory.createLineBorder(Color.white));
		textInputArea.addKeyListener(listenEnter);
		textInputArea.setLineWrap(true);
		textInputArea.setWrapStyleWord(true);
		textInputArea.setCaretColor(textColour);
				
		theUI.getContentPane().add(theActionBar, BorderLayout.PAGE_START);
		theUI.getContentPane().add(scrollArea, BorderLayout.CENTER);
		theUI.getContentPane().add(textInputArea, BorderLayout.PAGE_END);
		theUI.setTitle("Wet_Dirt - The Multiplayer Text Adventure Game");
		theUI.requestFocusInWindow();
		textInputArea.requestFocusInWindow();
		
		theUI.pack();
		theUI.setVisible(true);
		

	}
	
    public static void activateKeyEvent(KeyEvent e, String keyStatus) {
    	int id = e.getID();
    	String inputString = "";
    	if (id == KeyEvent.KEY_RELEASED) {
    		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    			Document theInputDoc = textInputArea.getDocument();
    			try {
					inputString = theInputDoc.getText(0, theInputDoc.getLength());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
    			addToOutput(inputString);
    			textInputArea.setText("");
    		}
    	}
    }
    
    public static void addToOutput(String inputString) {
		Document theOutputDoc = scrollArea.getDocument();
		SimpleAttributeSet normal = new SimpleAttributeSet();
		
		try {
			theOutputDoc.insertString(theOutputDoc.getLength(), inputString, normal);
		} catch (BadLocationException e1) {
			
		}
    }
	
	public UI() {
		createAndShowGUI();
	}

}
