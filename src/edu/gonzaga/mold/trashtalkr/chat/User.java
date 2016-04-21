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

/**
 * Class representing a user
 */
public class User {
	private Number160 userId;
	private String displayName;
	private ClientNode client;
	private boolean bootstrapped = false;
	private List<MessageListener> listeners;

	/**
	 * Creates a new User
	 * 
	 * @param ip
	 *            the IP of the master node
	 * @throws IOException
	 */
	public User(String ip, String display) throws IOException {
		displayName = display;
		userId = Util.generatePeerId();
		client = new ClientNode(ip, userId, displayName);
		listeners = new ArrayList<MessageListener>();
		client.addBroadcastListener(new ClientBroadcastListener() {
			@Override
			public void onBroadcast() {
				triggerMessageListeners();
			}
		});
	}

	/**
	 * Add a listener for incoming messages
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addMessageListener(MessageListener listener) {
		listeners.add(listener);
	}

	/**
	 * Trigger all the listeners for incoming messages
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
	 * Connect the user to the p2p network
	 * 
	 * @return true if successful
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
	 * Disconnects the user from the p2p network
	 */
	public void disconnect() {
		client.halt();
	}

	/**
	 * Posts a message to the current chat room
	 * 
	 * @param message
	 *            the message to post
	 * @throws IOException
	 */
	public void postMessage(String message) throws IOException {
		ChatMessage m = new ChatMessage(message, this.userId, this.displayName);
		client.postMessage(m);
	}

	/**
	 * Get all the messages in the current chat room
	 * 
	 * @return the messages
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public List<ChatMessage> checkMessages() throws ClassNotFoundException, IOException {
		List<ChatMessage> messages = client.getMessages();
		Collections.sort(messages);
		return messages;
	}
}
