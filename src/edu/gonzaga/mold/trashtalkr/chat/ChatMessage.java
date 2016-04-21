package edu.gonzaga.mold.trashtalkr.chat;

import java.io.Serializable;
import net.tomp2p.peers.Number160;

/**
 * Class representing a chat message
 */
public class ChatMessage implements Comparable<ChatMessage>, Serializable {
	private static final long serialVersionUID = 5448162664352364561L;
	private long msPostedAt;
	private String message;
	private Number160 userId;

	/**
	 * Protected constructor, only called by User class
	 * 
	 * @param message
	 *            text of the message
	 * @param userId
	 *            the ID of the posting user
	 */
	protected ChatMessage(String message, Number160 userId) {
		this.message = message;
		this.userId = userId;
		this.msPostedAt = System.currentTimeMillis();
	}

	/**
	 * Get message
	 * 
	 * @return message
	 */
	public String getMessage() {
		return "[" + convertSecondsToHMmSs(msPostedAt) + "]" + ": " + message;
	}

	public static String convertSecondsToHMmSs(long seconds) {
		long s = seconds % 60;
		long m = (seconds / 60) % 60;
		long h = (seconds / (60 * 60)) % 24;
		return String.format("%d:%02d:%02d", h, m, s);
	}

	/**
	 * Get userId
	 * 
	 * @return user id
	 */
	public Number160 getUserId() {
		return userId;
	}

	/**
	 * Converts a ChatMessage object to a textual representation
	 * 
	 * @return textual representation
	 */
	@Override
	public String toString() {
		return getMessage();
	}

	/**
	 * Compares one ChatMessage object with another
	 * 
	 * @param other
	 *            the other ChatMessage object
	 * @return comparison result
	 */
	@Override
	public int compareTo(ChatMessage other) {
		return Long.compare(this.msPostedAt, other.msPostedAt);
	}
}
