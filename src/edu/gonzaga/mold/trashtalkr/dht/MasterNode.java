package edu.gonzaga.mold.trashtalkr.dht;

import java.io.IOException;
import java.net.InetAddress;

import edu.gonzaga.mold.trashtalkr.util.Constants;
import net.tomp2p.connection.Bindings;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;

public class MasterNode {
	public static int id = 1;
	public static String eventName = "global";

	private PeerDHT peer;

	/**
	 * Create a new MasterNode (there should only be one of these on a network)
	 * 
	 * @param ip
	 *            the IP address to bind to
	 * @param behindFirewall
	 *            whether the node is behind a firewall
	 * @throws IOException
	 */
	public MasterNode(String ip, boolean behindFirewall) throws IOException {
		Bindings b = new Bindings();
		b.addAddress(InetAddress.getByName(ip));
		Peer pb = new PeerBuilder(Number160.createHash(id)).bindings(b).ports(Constants.BOOTSTRAPPER_PORT)
				.behindFirewall(behindFirewall).start();
		this.peer = new PeerBuilderDHT(pb).start();
	}

	/**
	 * Secondary constructor
	 * 
	 * @param ip
	 *            the IP address to bind to
	 * @throws IOException
	 */
	public MasterNode(String ip) throws IOException {
		this(ip, true);
	}

	/**
	 * Shuts down a master node, preventing new connections
	 */
	public void halt() {
		peer.shutdown();
	}
}
