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

	public String getMessage() {
		return message;
	}

	public Number160 getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return getMessage();
	}

	@Override
	public int compareTo(ChatMessage other) {
		return Long.compare(this.msPostedAt, other.msPostedAt);
	}
}
