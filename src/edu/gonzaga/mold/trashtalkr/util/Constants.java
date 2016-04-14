package edu.gonzaga.mold.trashtalkr.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.tomp2p.peers.Number160;

public class Constants {
	public static final String BOOTSTRAPPER_IP = "147.222.212.43";
	public static final int BOOTSTRAPPER_PORT = 4000;
	public static final int CLIENT_PORT = 4563;
	
	public static Number160 generatePeerId() {
		try {
			return Number160.createHash(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return Number160.ZERO;
	}
}
