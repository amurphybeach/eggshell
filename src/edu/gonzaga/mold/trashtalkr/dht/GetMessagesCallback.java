package edu.gonzaga.mold.trashtalkr.dht;

import java.util.List;

import edu.gonzaga.mold.trashtalkr.chat.ChatMessage;

public interface GetMessagesCallback {
	void call(List<ChatMessage> messages);
	void err(Throwable t);
}
