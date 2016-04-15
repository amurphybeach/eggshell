package edu.gonzaga.mold.trashtalkr.chat;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.gonzaga.mold.trashtalkr.dht.ClientBroadcastListener;
import edu.gonzaga.mold.trashtalkr.dht.ClientNode;
import edu.gonzaga.mold.trashtalkr.dht.GetMessagesCallback;
import edu.gonzaga.mold.trashtalkr.util.Util;
import net.tomp2p.peers.Number160;

public class User {
	private Number160 userId;
	private ClientNode client;
	private boolean bootstrapped = false;
	private List<MessageListener> listeners;

	public User(String ip) throws IOException {
		userId = Util.generatePeerId();
		client = new ClientNode(ip, userId);
		listeners = new ArrayList<MessageListener>();
		client.addBroadcastListener(new ClientBroadcastListener() {
			@Override
			public void onBroadcast() {
				triggerMessageListeners();
			}
		});
	}

	/**
	 * <h1>Add Message Listener</h1>
	 * <p>
	 * Adds a listener to list
	 * </p>
	 * 
	 * @param listener
	 */
	public void addMessageListener(MessageListener listener) {
		listeners.add(listener);
	}

	/**
	 * <h1>Trigger Listner</h1>
	 * <p>
	 * Sets off listener messages
	 * </p>
	 * 
	 * @param
	 */
	private void triggerMessageListeners() {
		client.getMessagesAsync(new GetMessagesCallback() {
			@Override
			public void call(List<ChatMessage> messages) {
				Collections.sort(messages);
				try {
					for (MessageListener listener : listeners) {
						listener.onMessage(messages);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void err(Throwable t) {
				t.printStackTrace();
			}
		});
	}

	/**
	 * <h1>Connect</h1>
	 * <p>
	 * Connects to the client bootstrap
	 * </p>
	 * 
	 * @return true if successful - false otherwise
	 */
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

	/**
	 * <h1>Disconnect</h1>
	 * <p>
	 * Client disconnects you
	 * </p>
	 * 
	 */
	public void disconnect() {
		client.halt();
	}

	/**
	 * <h1>Post Message</h1>
	 * <p>
	 * Adds a message to Chatmassage client
	 * </p>
	 * 
	 * @param message
	 */
	public void postMessage(String message) throws IOException {
		ChatMessage m = new ChatMessage(message, this.userId);
		client.postMessage(m);
	}

	/**
	 * <h1>Check Message</h1>
	 * <p>
	 * Checks the client messages and puts them in List
	 * </p>
	 * 
	 * @return List Messages
	 */
	public List<ChatMessage> checkMessages() throws ClassNotFoundException, IOException {
		List<ChatMessage> messages = client.getMessages();
		Collections.sort(messages);
		return messages;
	}
}
