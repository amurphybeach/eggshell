package edu.gonzaga.mold.trashtalkr.dht;

/**
 * Listener interface for broadcast events
 */
public interface ClientBroadcastListener {
	/**
	 * Called when broadcasts are received
	 * 
	 * @param key
	 *            the key of the message received
	 */
	void onBroadcast();
}
