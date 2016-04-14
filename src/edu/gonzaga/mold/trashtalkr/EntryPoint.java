package edu.gonzaga.mold.trashtalkr;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import edu.gonzaga.mold.trashtalkr.dht.MasterNode;
import edu.gonzaga.mold.trashtalkr.gui.Gui;

public class EntryPoint {
	public static void main(String[] args) throws Exception {
		if (args.length == 2 && args[0].equals("bootstrap")) {
			MasterNode b = new MasterNode(args[1]);
			b.examine(1000);
		} 
		else{
			Gui windowGui = new Gui();
			windowGui.setVisible(true);
			while(windowGui.isVisible()){
				//
			}
			
		}
	}
	
	static String getLine() {
		System.out.print("Please enter a short line of text: ");
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		String inLine = "";
		try {
			inLine = in.readLine();
		} catch (Exception e) {
			System.err.println("Error reading input.");
			e.printStackTrace();
			System.exit(1);
		}
		return inLine;
	}
}