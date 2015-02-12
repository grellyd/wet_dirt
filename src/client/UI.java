package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;


public class UI {
	
	private static JFrame theUI; 
	private static JTextPane scrollArea;
	private static JTextPane chatScrollArea;
	private static JTextArea textInputArea;
	private static JTextArea chatInputArea;
	private static JScrollPane scrollContainer;
	private static JScrollPane chatScrollContainer;
	
//	private static Color actionBarColour;
	private static Color textInputAreaColour;
	private static Color systemTextColour;
	private static Color userTextColour;
	
	private static Font textFont;
	
	private static Dimension chatScrollAreaDimension;
	private static Dimension mainAreaDimension;
	private static Dimension chatInputDimension;
	private static Dimension textInputDimension;

	private static KeyListener textListenEnter;
	private static KeyListener chatListenEnter;
	
	private static String newline = System.getProperty("line.separator");
	
	private static String returnString;
	private static String chatString;
	
	private final static int textInputAreaHeight = 20;

	public static void createAndShowGUI() {
		theUI = new JFrame("wetDirtUI");
		theUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		systemTextColour = new Color(98, 252, 56); 
		userTextColour = new Color(255, 239, 10);
		textInputAreaColour = new Color(0, 0, 0); //black
		//actionBarColour = new Color(153, 60, 240);
				
		textFont = new Font("myFont", Font.BOLD, 14);
		
		mainAreaDimension = new Dimension(600, 600);
		chatScrollAreaDimension = new Dimension(150, 600);
		textInputDimension = new Dimension(600, textInputAreaHeight);
		chatInputDimension = new Dimension(150, textInputAreaHeight);
		
		textListenEnter = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				activateTextKeyEvent(e, "RELEASED");
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		
		chatListenEnter = new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				activateChatKeyEvent(e, "RELEASED");				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		
		scrollArea = new JTextPane();
		scrollArea.setEditable(false);
		scrollArea.setBackground(textInputAreaColour);
		scrollArea.setFont(textFont);
		scrollArea.setForeground(systemTextColour);
		scrollArea.setBorder(BorderFactory.createLineBorder(Color.white));
		scrollArea.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// Do nothing
			}

			@Override
			public void focusGained(FocusEvent e) {
				textInputArea.requestFocus();
				
			}
			
		});
		
		chatScrollArea = new JTextPane();
		chatScrollArea.setEditable(false);
		chatScrollArea.setBackground(textInputAreaColour);
		chatScrollArea.setFont(textFont);
		chatScrollArea.setForeground(systemTextColour);
		chatScrollArea.setBorder(BorderFactory.createLineBorder(Color.white));
		chatScrollArea.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// Do nothing
			}

			@Override
			public void focusGained(FocusEvent e) {
				chatInputArea.requestFocus();
			}
			
		});
		
		scrollContainer = new JScrollPane(scrollArea);
		scrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollContainer.setPreferredSize(mainAreaDimension);

		chatScrollContainer = new JScrollPane(chatScrollArea);
		chatScrollContainer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		chatScrollContainer.setPreferredSize(chatScrollAreaDimension);
		
		textInputArea = new JTextArea();
		textInputArea.setPreferredSize(textInputDimension);
		textInputArea.setBackground(textInputAreaColour);
		textInputArea.setForeground(userTextColour);
		textInputArea.setFocusable(true);
		textInputArea.setEditable(true);
		textInputArea.setFont(textFont);
		textInputArea.setBorder(BorderFactory.createLineBorder(Color.white));
		textInputArea.addKeyListener(textListenEnter);
		textInputArea.setLineWrap(true);
		textInputArea.setWrapStyleWord(true);
		textInputArea.setCaretColor(systemTextColour);
		
		chatInputArea = new JTextArea();
		chatInputArea.setPreferredSize(chatInputDimension);
		chatInputArea.setBackground(textInputAreaColour);
		chatInputArea.setForeground(userTextColour);
		chatInputArea.setFocusable(true);
		chatInputArea.setEditable(true);
		chatInputArea.setFont(textFont);
		chatInputArea.setBorder(BorderFactory.createLineBorder(Color.white));
		chatInputArea.addKeyListener(chatListenEnter);
		chatInputArea.setLineWrap(true);
		chatInputArea.setWrapStyleWord(true);
		chatInputArea.setCaretColor(systemTextColour);
		
		theUI.addWindowListener(new WindowAdapter() {
			
			public void windowActivated(WindowEvent e) {
				textInputArea.requestFocus();
			}
			
			public void windowOpened(WindowEvent e) {
				textInputArea.requestFocus();
			}
		});	
		
		//TODO: Fix the chat container. Perhaps two containers; one for the text area and input,
		//		another for the chat area and input.
		theUI.getContentPane().add(chatScrollContainer, BorderLayout.EAST);
		theUI.getContentPane().add(scrollContainer, BorderLayout.CENTER);
		theUI.getContentPane().add(textInputArea, BorderLayout.PAGE_END);
		theUI.getContentPane().add(chatInputArea, BorderLayout.WEST);
		theUI.setTitle("Wet_Dirt - The Multiplayer Text Adventure Game");
		theUI.requestFocusInWindow();
		textInputArea.requestFocus();
		
		returnString = "";
		
		theUI.pack();
		theUI.setVisible(true);
		
		addToOutput("Welcome to Wet_Dirt!", false);
		

	}
	
    public static void activateTextKeyEvent(KeyEvent e, String keyStatus) {
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
    
    public static void activateChatKeyEvent(KeyEvent e, String keyStatus) {
    	int id = e.getID();
    	String inputString = "";
    	if (id == KeyEvent.KEY_RELEASED) {
    		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
    			Document theInputDoc = chatInputArea.getDocument();
    			try {
					inputString = theInputDoc.getText(0, theInputDoc.getLength()).trim();
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
    			chatString = inputString;
    			addToChat(inputString, true);
    			passChatMessage();
    			chatInputArea.setText("");
    		}
    	}
    }

    
    public static void addToOutput(String inputString, boolean isUser) {
    	doAddToArea(inputString, isUser, scrollArea);
    }
    
    public static void addToChat(String inputString, boolean isUser) {
    	doAddToArea(inputString, isUser, chatScrollArea);
    }
    	
    public static void doAddToArea(String inputString, boolean isUser, JTextPane area) {
    	inputString = newline + inputString;
		Document theOutputDoc = area.getDocument();
		Color myColour;
		if (isUser) {
			myColour = userTextColour;
		} else {
			myColour = systemTextColour;
		}
		try {
			MutableAttributeSet userAttrib = area.getInputAttributes();
			StyleConstants.setForeground(userAttrib, myColour);
			theOutputDoc.insertString(theOutputDoc.getLength(), inputString, userAttrib);
			area.setCaretPosition(theOutputDoc.getLength());
		} catch (BadLocationException e1) {
			
		}
    }
       
    public static String getInputResult() {
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

    public static String passChatMessage() {
    	// notify listeners.
    	chatScrollArea.notify();
    	// send the string
    	String tempString = chatString.trim();
    	chatString = "";
    	return tempString;
    }
	
	public UI() {
		createAndShowGUI();
	}

}
