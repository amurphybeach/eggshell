package edu.gonzaga.mold.trashtalkr.chat;

import java.io.Serializable;

import net.tomp2p.peers.Number160;

public class ChatMessage implements Comparable<ChatMessage>, Serializable {
	private static final long serialVersionUID = 5448162664352364561L;
	private long msPostedAt;
	private String message;
	private Number160 userId;

	protected ChatMessage(String message, Number160 userId) {
		this.message = message;
		this.userId = userId;
		this.msPostedAt = System.currentTimeMillis();
	}

	/**
	 * <h1>Get Message</h1>
	 * <p>
	 * @return  message
	 * </p>
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * <h1>Get UserId</h1>
	 * <p>
	 *@return  user id
	 * </p>
	 */
	public Number160 getUserId() {
		return userId;
	}

	/**
	 * <h1>ToString/h1>
	 * <p>
	 *@return Returns message
	 * </p>
	 */
	@Override
	public String toString() {
		return getMessage();
	}

	/**
	 * <h1>Compare Message</h1>
	 * <p>
	 * @return Returns int
	 * </p>
	 */
	@Override
	public int compareTo(ChatMessage other) {
		return Long.compare(this.msPostedAt, other.msPostedAt);
	}
}
