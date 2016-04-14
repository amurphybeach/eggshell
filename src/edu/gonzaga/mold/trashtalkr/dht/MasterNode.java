package edu.gonzaga.mold.trashtalkr.dht;

import java.io.IOException;
import java.net.InetAddress;

import edu.gonzaga.mold.trashtalkr.util.Constants;
import net.tomp2p.connection.Bindings;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;

public class MasterNode {
	public static int id = 1;
	public static String eventName = "global";
	
	private PeerDHT peer;
	
	public MasterNode(boolean behindFirewall) throws IOException {
		Bindings b = new Bindings();
		b.addAddress(InetAddress.getByName(Constants.BOOTSTRAPPER_IP));
		Peer pb = new PeerBuilder(Number160.createHash(id)).bindings(b).ports(Constants.BOOTSTRAPPER_PORT).behindFirewall(behindFirewall).start();
		this.peer = new PeerBuilderDHT(pb).start();
	}
	
	public MasterNode() throws IOException {
		this(false);
	}
	
	public void examine(int ms) {
		try {
			while (true) {
				Thread.sleep(ms);
				FutureGet fg = peer.get(Number160.createHash(eventName)).all().start();
				fg.awaitUninterruptibly();
				int size = fg.dataMap().size();
				System.out.println("size " + size);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			peer.shutdown();
		}
	}
}
