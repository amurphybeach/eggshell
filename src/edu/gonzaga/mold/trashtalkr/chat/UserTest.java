package edu.gonzaga.mold.trashtalkr.chat;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class UserTest {

	@Test
	public void test() throws IOException, ClassNotFoundException {
		User me = new User("69");
		me.connect();
		me.disconnect();
		//me.postMessage("get out");
	}

}
