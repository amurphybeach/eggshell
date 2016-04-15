package edu.gonzaga.mold.trashtalkr.dht;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private String ip;
	private List<ClientBroadcastListener> listeners;

	Logger logger = LoggerFactory.getLogger(ClientNode.class);

	public ClientNode(String ip, Number160 peerId, boolean behindFirewall) throws IOException {
		this.ip = ip;
		Peer pb = new PeerBuilder(peerId).ports(Constants.CLIENT_PORT).behindFirewall(true)
				.broadcastHandler(new ClientBroadcastHandler(this)).start();
		this.peer = new PeerBuilderDHT(pb).start();
		listeners = new ArrayList<ClientBroadcastListener>();
	}

	public ClientNode(String ip, Number160 peerId) throws IOException {
		this(ip, peerId, true);
	}

	public boolean bootstrap() throws UnknownHostException {
		PeerAddress bootstrapServer = new PeerAddress(Number160.ZERO, InetAddress.getByName(ip),
				Constants.BOOTSTRAPPER_PORT, Constants.BOOTSTRAPPER_PORT);
		FutureDiscover fd = peer.peer().discover().peerAddress(bootstrapServer).start();
		fd.awaitUninterruptibly();
		if (!fd.isSuccess()) {
			System.out.println(fd.failedReason());
			return false; // failure
		}
		bootstrapServer = fd.reporter();
		FutureBootstrap bootstrap = peer.peer().bootstrap().peerAddress(bootstrapServer).start();
		bootstrap.awaitUninterruptibly();
		if (!bootstrap.isSuccess()) {
			System.out.println(bootstrap.failedReason());
			return false;
		}
		return true;
	}

	public void halt() {
		peer.peer().announceShutdown().start().awaitUninterruptibly();
		peer.peer().shutdown().awaitUninterruptibly();
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
				logger.error("Could not find key for val: " + d.object());
			}
		}
		return messages;
	}

	public void postMessage(ChatMessage message) throws IOException {
		int r = new Random().nextInt();
		peer.add(Number160.createHash(MasterNode.eventName)).data(new Data(r)).start().awaitUninterruptibly();
		peer.put(new Number160(r)).data(new Data(message)).start().awaitUninterruptibly();
		peer.peer().broadcast(new Number160(r)).start();
	}

	protected void triggerListeners() {
		for (ClientBroadcastListener listener : listeners) {
			listener.onBroadcast();
		}
	}

	public void addBroadcastListener(ClientBroadcastListener listener) {
		listeners.add(listener);
	}
}
