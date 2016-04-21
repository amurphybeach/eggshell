package edu.gonzaga.mold.trashtalkr.chat;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.tomp2p.peers.Number160;

/**
 * Class representing a chat message
 */
public class ChatMessage implements Comparable<ChatMessage>, Serializable {
	private static final long serialVersionUID = 5448162664352364561L;
	private long msPostedAt;
	private String message;
	private Number160 userId;
	private String displayName;

	/**
	 * Protected constructor, only called by User class
	 *
	 * @param message
	 *            text of the message
	 * @param userId
	 *            the ID of the posting user
	 */
	protected ChatMessage(String message, Number160 userId, String display) {
		this.displayName = display;
		this.message = message;
		this.userId = userId;
		this.msPostedAt = System.currentTimeMillis();
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
	 * Gets the time the message was posted in string format
	 *
	 * @return textual representation
	 */
	public String getTime() {
		Date date = new Date(msPostedAt);
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		return formatter.format(date);
	}

	/**
	 * Converts a ChatMessage object to a textual representation
	 *
	 * @return textual representation
	 */
	@Override
	public String toString() {
		return "[" + getTime() + "]" + " " + displayName + ": " + message;
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
