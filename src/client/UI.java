package client;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.AttributeSet.ColorAttribute;
import javax.swing.text.StyleConstants.ColorConstants;


public class UI {
	
	private static JFrame theUI; 
	private static JTextArea theActionBar;
	private static JTextPane scrollArea;
	private static JTextArea textInputArea;
	private static JScrollPane scrollContainer;
	
	private static Color actionBarColour;
	private static Color textInputAreaColour;
	private static Color systemTextColour;
	private static Color userTextColour;
	
	private static Font textFont;
	
	private static Dimension menuBarDimension;
	private static Dimension mainAreaDimension;
	private static Dimension textInputDimension;

	private static KeyListener listenEnter;
	
	private static String newline = System.getProperty("line.separator");
	
	private static String returnString;

	public static void createAndShowGUI() {
		theUI = new JFrame("wetDirtUI");
		theUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		systemTextColour = new Color(98, 252, 56); 
		userTextColour = new Color(255, 239, 10);
		textInputAreaColour = new Color(0, 0, 0); //black
		actionBarColour = new Color(153, 60, 240);
				
		textFont = new Font("myFont", Font.BOLD, 14);
		
		menuBarDimension = new Dimension(600, 90);
		mainAreaDimension = new Dimension(600, 600);
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
		
		theActionBar = new JTextArea("Possible Commands are: \n\tMove $direction$, "
				+ "\n\tLook $object$, Look Around, Examine inventory/$object$, "
				+ "\n\tCheck doors, Open/Close $direction$ door"
				+ "\n\tCheck inventory, Take/Drop $object$");
		theActionBar.setOpaque(true);
		theActionBar.setBackground(actionBarColour);
		theActionBar.setPreferredSize(menuBarDimension);
		theActionBar.setName("Possible Commands: ");
		theActionBar.setToolTipText("Possible Commands are: \n Move &Direction& \n Look \n Examine");
		theActionBar.setForeground(systemTextColour);
		theActionBar.setEditable(false);
		theActionBar.setLineWrap(true);
		theActionBar.setWrapStyleWord(true);
		
		scrollArea = new JTextPane();
		scrollArea.setEditable(false);
		scrollArea.setBackground(textInputAreaColour);
		scrollArea.setFont(textFont);
		scrollArea.setForeground(systemTextColour);
		scrollArea.setBorder(BorderFactory.createLineBorder(Color.white));

		
		scrollContainer = new JScrollPane(scrollArea);
		scrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollContainer.setPreferredSize(mainAreaDimension);
		
		textInputArea = new JTextArea();
		textInputArea.setPreferredSize(textInputDimension);
		textInputArea.setBackground(textInputAreaColour);
		textInputArea.setForeground(userTextColour);
		textInputArea.setFocusable(true);
		textInputArea.setEditable(true);
		textInputArea.setFont(textFont);
		textInputArea.setBorder(BorderFactory.createLineBorder(Color.white));
		textInputArea.addKeyListener(listenEnter);
		textInputArea.setLineWrap(true);
		textInputArea.setWrapStyleWord(true);
		textInputArea.setCaretColor(systemTextColour);
		textInputArea.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				textInputArea.requestFocus();
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		theUI.addWindowListener(new WindowAdapter() {
			
			public void windowActivated(WindowEvent e) {
				textInputArea.requestFocus();
			}
			
			public void windowOpened(WindowEvent e) {
				textInputArea.requestFocus();
			}
		});	
		
		
		theUI.getContentPane().add(theActionBar, BorderLayout.PAGE_START);
		theUI.getContentPane().add(scrollContainer, BorderLayout.CENTER);
		theUI.getContentPane().add(textInputArea, BorderLayout.PAGE_END);
		theUI.setTitle("Wet_Dirt - The Multiplayer Text Adventure Game");
		theUI.requestFocusInWindow();
		textInputArea.requestFocus();
		
		returnString = "";
		
		theUI.pack();
		theUI.setVisible(true);
		
		addToOutput("Welcome to Wet_Dirt!", false);
		

	}
	
    public static void activateKeyEvent(KeyEvent e, String keyStatus) {
    	int id = e.getID();
    	String inputString = "";
    	if (id == KeyEvent.KEY_RELEASED) {
    		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    			Document theInputDoc = textInputArea.getDocument();
    			try {
					inputString = theInputDoc.getText(0, theInputDoc.getLength()).trim();
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
    			returnString = inputString;
    			addToOutput(inputString, true);
    			textInputArea.setText("");
    		}
    	}
    }
    
    public static void addToOutput(String inputString, boolean isUser) {
    	inputString = newline + inputString;
		Document theOutputDoc = scrollArea.getDocument();
		if (isUser) {
			try {
				MutableAttributeSet userAttrib = scrollArea.getInputAttributes();
				StyleConstants.setForeground(userAttrib, userTextColour);
				theOutputDoc.insertString(theOutputDoc.getLength(), inputString, userAttrib);
				scrollArea.setCaretPosition(theOutputDoc.getLength());
			} catch (BadLocationException e1) {
				
			}
		} else {
			try {
				MutableAttributeSet systemAttrib = scrollArea.getInputAttributes();
				StyleConstants.setForeground(systemAttrib, systemTextColour);
				theOutputDoc.insertString(theOutputDoc.getLength(), inputString, systemAttrib);
				scrollArea.setCaretPosition(theOutputDoc.getLength());
			} catch (BadLocationException e1) {
				
			}
		}
    }
    
    public  static String getInputResult() {
    	while (returnString.equals("")) {
    		try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	String tempString = returnString;
    	tempString.trim();
    	returnString = "";
    	return tempString;
    }

	
	public UI() {
		createAndShowGUI();
	}

}
