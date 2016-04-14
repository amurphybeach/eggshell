package edu.gonzaga.mold.trashtalkr.dht;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import edu.gonzaga.mold.trashtalkr.chat.ChatMessage;
import edu.gonzaga.mold.trashtalkr.util.Constants;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;

public class ClientNode {	
	private PeerDHT peer;
	
	public ClientNode(Number160 peerId, boolean behindFirewall) throws IOException  {
		Peer pb = new PeerBuilder(peerId).ports(Constants.CLIENT_PORT).behindFirewall(true).start();
		this.peer = new PeerBuilderDHT(pb).start();
		peer.peer().peerAddress().inetAddress();
	}
	
	public ClientNode(Number160 peerId) throws IOException {
		this(peerId, true);
	}
	
	public boolean bootstrap() throws UnknownHostException {
		PeerAddress bootStrapServer = new PeerAddress(Number160.ZERO, InetAddress.getByName(Constants.BOOTSTRAPPER_IP), Constants.BOOTSTRAPPER_PORT, Constants.BOOTSTRAPPER_PORT);
		FutureDiscover fd = peer.peer().discover().peerAddress(bootStrapServer).start();
		fd.awaitUninterruptibly();
		if (!fd.isSuccess()) {
			return false; // failure
		}
		bootStrapServer = fd.reporter();
		FutureBootstrap bootstrap = peer.peer().bootstrap().peerAddress(bootStrapServer).start();
		bootstrap.awaitUninterruptibly();
		if (!bootstrap.isSuccess()) {
			return false;
		}
		return true;
	}
	
	public List<ChatMessage> getMessages() throws ClassNotFoundException, IOException {
		FutureGet fget = peer.get(Number160.createHash(MasterNode.eventName)).all().start();
		fget.awaitUninterruptibly();
		Iterator<Data> iterator = fget.dataMap().values().iterator();
		FutureGet fg;
		ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
		while (iterator.hasNext()) {
			Data d = iterator.next();
			fg = peer.get(new Number160(((Integer) d.object()).intValue())).start();
			fg.awaitUninterruptibly();
			if (fg.data() != null) {
				messages.add((ChatMessage) fg.data().object());
			} else {
				System.err.println("Could not find key for val: " + d.object());
			}
		}
		return messages;
	}
	
	public void postMessage(ChatMessage message) throws IOException {
		int r = new Random().nextInt();
		System.out.println("Storing DHT address (" + r + ") in DHT");
		peer.add(Number160.createHash(MasterNode.eventName)).data(new Data(r)).start().awaitUninterruptibly();
		System.out.println("Adding (" + message.getMessage() + ") to DHT");
		peer.put(new Number160(r)).data(new Data(message)).start().awaitUninterruptibly();
	}
	
	public void halt() {
		peer.peer().announceShutdown().start().awaitUninterruptibly();
		peer.peer().shutdown().awaitUninterruptibly();
	}
}
