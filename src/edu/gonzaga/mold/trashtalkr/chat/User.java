package edu.gonzaga.mold.trashtalkr.chat;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.gonzaga.mold.trashtalkr.dht.ClientBroadcastListener;
import edu.gonzaga.mold.trashtalkr.dht.ClientNode;
import edu.gonzaga.mold.trashtalkr.dht.GetMessagesCallback;
import edu.gonzaga.mold.trashtalkr.util.Constants;
import net.tomp2p.peers.Number160;

public class User {
	private Number160 userId;
	private ClientNode client;
	private boolean bootstrapped = false;
	private List<MessageListener> listeners;

	public User(String ip) throws IOException {
		userId = Constants.generatePeerId();
		client = new ClientNode(ip, userId);
		listeners = new ArrayList<MessageListener>();
		client.addBroadcastListener(new ClientBroadcastListener() {
			@Override
			public void onBroadcast() {
				triggerMessageListeners();
			}
		});
	}

	public void addMessageListener(MessageListener listener) {
		listeners.add(listener);
	}

	private void triggerMessageListeners() {
		client.getMessagesAsync(new GetMessagesCallback() {
			@Override
			public void call(List<ChatMessage> messages) {
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
		List<ChatMessage> messages = client.getMessages();
		Collections.sort(messages);
		return messages;
	}
}
