package edu.gonzaga.mold.trashtalkr.dht;

import java.util.List;

import edu.gonzaga.mold.trashtalkr.chat.ChatMessage;

/**
 * Callback interface for getMessagesAsync
 */
public interface GetMessagesCallback {
	/**
	 * Called on non-error
	 * 
	 * @param messages
	 *            the messages
	 */
	void call(List<ChatMessage> messages);

	/**
	 * Called on error
	 * 
	 * @param t
	 *            the error
	 */
	void err(Throwable t);
}
