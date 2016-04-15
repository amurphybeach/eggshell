package edu.gonzaga.mold.trashtalkr.chat;

import java.util.List;

/**
 * Listener interface for message events
 */
public interface MessageListener {
	/**
	 * Called when new messages are received
	 * 
	 * @param messages
	 *            the new messages
	 */
	void onMessage(List<ChatMessage> messages);
}
