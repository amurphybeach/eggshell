package edu.gonzaga.mold.trashtalkr.dht;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.tomp2p.message.Message;
import net.tomp2p.p2p.BroadcastHandler;
import net.tomp2p.p2p.Peer;

public class ClientBroadcastHandler implements BroadcastHandler {
	Logger logger = LoggerFactory.getLogger(ClientBroadcastHandler.class);

	private ClientNode client;

	protected ClientBroadcastHandler(ClientNode client) {
		this.client = client;
	}

	@Override
	public BroadcastHandler receive(Message message) {
		logger.info("Got a broadcast message");
		client.triggerListeners();
		return this;
	}

	@Override
	public BroadcastHandler init(Peer peer) {
		return this;
	}

}
