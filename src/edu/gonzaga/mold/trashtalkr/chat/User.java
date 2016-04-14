package edu.gonzaga.mold.trashtalkr.chat;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import edu.gonzaga.mold.trashtalkr.dht.ClientNode;
import edu.gonzaga.mold.trashtalkr.util.Constants;
import net.tomp2p.peers.Number160;

public class User {
	private Number160 userId;
	private ClientNode client;
	private boolean bootstrapped = false;
	
	public User(String ip) throws IOException {
		userId = Constants.generatePeerId();
		client = new ClientNode(ip, userId);
	}
	
	public boolean connect() {
		if (!bootstrapped) {
			boolean success = false;
			try {
				success = client.bootstrap();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			bootstrapped = success;
			return success;
		} else {
			return true;
		}
	}
	
	public void disconnect() {
		client.halt();
	}
	
	public void postMessage(String message) throws IOException {
		ChatMessage m = new ChatMessage(message, this.userId);
		client.postMessage(m);
	}
	
	public List<ChatMessage> checkMessages() throws ClassNotFoundException, IOException {
		return client.getMessages();
	}
}
