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
import edu.gonzaga.mold.trashtalkr.util.Util;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFutureListener;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.futures.FutureDone;
import net.tomp2p.futures.Futures;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;
import net.tomp2p.storage.Data;

/**
 * Class representing a node in the p2p network
 */
public class ClientNode {
	private PeerDHT peer;
	private String ip;
	private List<ClientBroadcastListener> listeners;

	private Logger logger = LoggerFactory.getLogger(ClientNode.class);

	/**
	 * Creates a new ClientNode
	 * 
	 * @param ip
	 *            the IP of the master node
	 * @param peerId
	 *            unique peer id for this user
	 * @param behindFirewall
	 *            whether the node is behind a firewall
	 * @throws IOException
	 */
	public ClientNode(String ip, Number160 peerId, boolean behindFirewall, String display) throws IOException {
		this.ip = ip;
		Peer pb = new PeerBuilder(peerId).ports(Util.generatePort()).behindFirewall(behindFirewall)
				.broadcastHandler(new ClientBroadcastHandler(this)).start();
		this.peer = new PeerBuilderDHT(pb).start();
		listeners = new ArrayList<ClientBroadcastListener>();
	}

	/**
	 * Secondary constructor
	 * 
	 * @param ip
	 *            the IP of the master node
	 * @param peerId
	 *            unique peer id for this user
	 * @throws IOException
	 */
	public ClientNode(String ip, Number160 peerId, String display) throws IOException {
		this(ip, peerId, true, display);
	}

	/**
	 * Bootstraps a peer to the network
	 * 
	 * @return true on success
	 * @throws UnknownHostException
	 */
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

	/**
	 * Shuts down a peer, disconnecting it from the network
	 */
	public void halt() {
		peer.peer().announceShutdown().start().awaitUninterruptibly();
		peer.peer().shutdown().awaitUninterruptibly();
	}

	/**
	 * Gets all the ChatMessage objects for the current event
	 * 
	 * @return the messages
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public List<ChatMessage> getMessages(String eventName) throws ClassNotFoundException, IOException {
		FutureGet eventFutureGet = peer.get(Number160.createHash(eventName)).all().start();
		eventFutureGet.awaitUninterruptibly();
		Iterator<Data> iterator = eventFutureGet.dataMap().values().iterator();
		FutureGet messageFutureGet;
		List<ChatMessage> messages = new ArrayList<ChatMessage>();
		while (iterator.hasNext()) {
			Data d = iterator.next();
			messageFutureGet = peer.get(new Number160(((Integer) d.object()).intValue())).start();
			messageFutureGet.awaitUninterruptibly();
			if (messageFutureGet.data() != null) {
				messages.add((ChatMessage) messageFutureGet.data().object());
			} else {
				logger.error("Could not find key for val: " + d.object());
			}
		}
		return messages;
	}

	/**
	 * Gets all the ChatMessage objects for the current event asynchronously
	 * 
	 * @param callback
	 *            the callback object
	 */
	public void getMessagesAsync(String eventName, GetMessagesCallback callback) {
		FutureGet eventFutureGet = peer.get(Number160.createHash(eventName)).all().start();
		eventFutureGet.addListener(new BaseFutureListener<FutureGet>() {
			@Override
			public void operationComplete(FutureGet eventFutureGet) throws Exception {
				Iterator<Data> iterator = eventFutureGet.dataMap().values().iterator();
				List<FutureGet> messageFutureGets = new ArrayList<FutureGet>();
				while (iterator.hasNext()) {
					Data d = iterator.next();
					FutureGet messageFutureGet = peer.get(new Number160(((Integer) d.object()).intValue())).start();
					messageFutureGets.add(messageFutureGet);
				}
				Futures.whenAll(messageFutureGets).addListener(new BaseFutureListener<FutureDone<List<FutureGet>>>() {
					@Override
					public void operationComplete(FutureDone<List<FutureGet>> futureDone) throws Exception {
						List<ChatMessage> messages = new ArrayList<ChatMessage>();
						for (FutureGet messageFutureGet : futureDone.object()) {
							if (messageFutureGet.data() != null) {
								messages.add((ChatMessage) messageFutureGet.data().object());
							} else {
								logger.error("Could not find key for val during async message get");
							}
						}
						callback.call(messages);
					}

					@Override
					public void exceptionCaught(Throwable t) throws Exception {
						callback.err(t);
					}
				});
			}

			@Override
			public void exceptionCaught(Throwable t) throws Exception {
				callback.err(t);
			}
		});
	}

	/**
	 * Post a message to the DHT
	 * 
	 * @param message
	 *            the message
	 * @throws IOException
	 */
	public void postMessage(String eventName, ChatMessage message) throws IOException {
		int r = new Random().nextInt();
		peer.add(Number160.createHash(eventName)).data(new Data(r)).start().awaitUninterruptibly();
		peer.put(new Number160(r)).data(new Data(message)).start().awaitUninterruptibly();
		peer.peer().broadcast(Number160.createHash(eventName)).start();
	}

	/**
	 * Add a listener for incoming broadcasts
	 * 
	 * @param listener
	 *            the listener
	 */
	public void addBroadcastListener(ClientBroadcastListener listener) {
		listeners.add(listener);
	}

	/**
	 * Trigger all the listeners for incoming broadcasts
	 */
	protected void triggerListeners(Number160 key) {
		for (ClientBroadcastListener listener : listeners) {
			listener.onBroadcast(key);
		}
	}
}
