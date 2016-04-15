package edu.gonzaga.mold.trashtalkr.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.tomp2p.peers.Number160;

public class Util {
	public static Number160 generatePeerId() {
		try {
			return Number160.createHash(getLocalAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return Number160.ZERO;
	}

	public static String getLocalAddress() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
}