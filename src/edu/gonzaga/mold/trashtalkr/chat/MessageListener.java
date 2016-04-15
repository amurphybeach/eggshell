package edu.gonzaga.mold.trashtalkr.chat;

import java.util.List;

public interface MessageListener {
	void onMessage(List<ChatMessage> messages);
}
