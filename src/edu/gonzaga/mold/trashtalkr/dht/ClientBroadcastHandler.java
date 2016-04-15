package edu.gonzaga.mold.trashtalkr.dht;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.tomp2p.message.Message;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.StructuredBroadcastHandler;

public class ClientBroadcastHandler extends StructuredBroadcastHandler {
	Logger logger = LoggerFactory.getLogger(ClientBroadcastHandler.class);

	private ClientNode client;

	protected ClientBroadcastHandler(ClientNode client) {
		this.client = client;
	}

	@Override
	public StructuredBroadcastHandler receive(Message message) {
		logger.info("Got a broadcast message");
		StructuredBroadcastHandler sbh = super.receive(message);
		client.triggerListeners();
		return sbh;
	}

	@Override
	public StructuredBroadcastHandler init(Peer peer) {
		return super.init(peer);
	}

}
