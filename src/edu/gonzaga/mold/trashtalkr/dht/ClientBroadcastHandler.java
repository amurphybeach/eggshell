package edu.gonzaga.mold.trashtalkr.dht;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.tomp2p.message.Message;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.StructuredBroadcastHandler;

/**
 * Helper class to trigger message events
 */
public class ClientBroadcastHandler extends StructuredBroadcastHandler {
	Logger logger = LoggerFactory.getLogger(ClientBroadcastHandler.class);

	private ClientNode client;

	/**
	 * Create a new broadcast handler (protected)
	 * 
	 * @param client
	 *            reference to the creating ClientNode
	 */
	protected ClientBroadcastHandler(ClientNode client) {
		this.client = client;
	}

	/**
	 * Handles incoming broadcasts
	 * 
	 * @param message
	 *            an incoming broadcast message
	 * @return this
	 */
	@Override
	public StructuredBroadcastHandler receive(Message message) {
		logger.info("Got a broadcast message");
		StructuredBroadcastHandler sbh = super.receive(message);
		client.triggerListeners();
		return sbh;
	}

	/**
	 * Initializes the broadcast handler
	 * 
	 * @param peer
	 *            reference to the initializing peer
	 */
	@Override
	public StructuredBroadcastHandler init(Peer peer) {
		return super.init(peer);
	}

}
