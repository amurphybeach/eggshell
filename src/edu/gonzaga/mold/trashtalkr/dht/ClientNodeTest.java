package edu.gonzaga.mold.trashtalkr.dht;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import net.tomp2p.peers.Number160;

public class ClientNodeTest {

	@Test
	public void test() throws IOException {
		ClientNode cn = new ClientNode("GET OUT", Number160.createHash(69));
		//cn.bootstrap();
	}

}
