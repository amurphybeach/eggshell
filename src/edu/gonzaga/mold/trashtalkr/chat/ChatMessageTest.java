package edu.gonzaga.mold.trashtalkr.chat;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import net.tomp2p.peers.Number160;

public class ChatMessageTest {


	@Test
	public void test() throws IOException {
		//User you = new User("6969");
		ChatMessage m = new ChatMessage("New", Number160.createHash(69));
	}

}
