package edu.gonzaga.mold.trashtalkr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import edu.gonzaga.mold.trashtalkr.chat.ChatMessage;
import edu.gonzaga.mold.trashtalkr.chat.User;
import edu.gonzaga.mold.trashtalkr.dht.MasterNode;

public class EntryPoint {
	public static void main(String[] args) throws Exception {
		if (args.length > 0 && args[0].equals("bootstrap")) {
			MasterNode b = new MasterNode();
			b.examine(1000);
		} else if (args.length == 0) {
			User you = new User();
			if (you.connect()) {
				String inLine = null;
				while ((inLine = getLine()) != null) {
					if (inLine.equals("show")) {
						List<ChatMessage> messages = you.checkMessages();
						Collections.sort(messages);
						for (ChatMessage m : messages) {
							System.out.println(m.getUserId().toString() + ": " + m.getMessage());
						}
					} else {
						you.postMessage(inLine);
					}
				}
				System.out.println();
				you.disconnect();
			} else {
				System.out.println("failed to connect (client already running on this ip/port?");
			}
		} else {
			System.err.println("Must be called with:" + "\n\tNATChat bootstrap <ip>\t\t(supernode)\n\t"
			        + "NATChat <bootstrap ip>\t\t(normal node).\n");
		}
		System.out.println("Shutting down...");
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