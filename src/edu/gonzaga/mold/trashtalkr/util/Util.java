package edu.gonzaga.mold.trashtalkr.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.tomp2p.peers.Number160;

public class Util {
	/**
	 * Generate a unique peer ID for the user, based on IP address
	 * 
	 * @return the peer ID
	 */
	public static Number160 generatePeerId() {
		try {
			return Number160.createHash(getLocalAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return Number160.ZERO;
	}

	/**
	 * Gets the user's IP address
	 * 
	 * @return the user's IP address
	 * @throws UnknownHostException
	 */
	public static String getLocalAddress() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}

	public static int generatePort() throws UnknownHostException {
		String addr = getLocalAddress();
		String[] parts = addr.split("\\.");
		return Integer.parseInt(parts[parts.length - 1], 10) + Constants.CLIENT_PORT;
	}
}
