package client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class UI {
	
	private static JFrame theUI; 
	private static JMenuBar theMenuBar;
	private static JScrollPane scrollArea;
	private static JTextArea textInputArea;
	
	private static Color menuBarColour;
	private static Color textInputAreaColour;
	private static Color textColour;
	
	private static Dimension menuBarDimension;
	private static Dimension mainAreaDimension;
	private static Dimension textInputDimension;


	public static void createAndShowGUI() {
		theUI = new JFrame("wetDirtUI");
		theUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		theMenuBar = new JMenuBar();
		theMenuBar.setOpaque(true);
		menuBarColour = new Color(153, 60, 240);
		theMenuBar.setBackground(menuBarColour);
		menuBarDimension = new Dimension(600, 45);
		theMenuBar.setPreferredSize(menuBarDimension);
		
		scrollArea = new JScrollPane();
		mainAreaDimension = new Dimension(600, 900);
		scrollArea.setPreferredSize(mainAreaDimension);
		
		textColour = new Color(255, 255, 255); //white
		textInputAreaColour = new Color(0, 0, 0); //black
		textInputArea = new JTextArea();
		textInputArea.setPreferredSize(textInputDimension);
		textInputArea.setBackground(textInputAreaColour);
		textInputArea.setForeground(textColour);
		
		theUI.setJMenuBar(theMenuBar);
		theUI.getContentPane().add(scrollArea);
		theUI.getContentPane().add(textInputArea);
		theUI.setTitle("Wet_Dirt - The Multiplayer Text Adventure Game");
		
		theUI.pack();
		theUI.setVisible(true);
	}
	
	public UI() {
		createAndShowGUI();
	}

}
