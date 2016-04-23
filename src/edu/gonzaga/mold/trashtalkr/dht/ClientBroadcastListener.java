package edu.gonzaga.mold.trashtalkr.dht;

import net.tomp2p.peers.Number160;

/**
 * Listener interface for broadcast events
 */
public interface ClientBroadcastListener {
	/**
	 * Called when broadcasts are received
	 */
	void onBroadcast(Number160 key);
}
