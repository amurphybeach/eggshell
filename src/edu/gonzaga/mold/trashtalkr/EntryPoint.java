package edu.gonzaga.mold.trashtalkr;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.gonzaga.mold.trashtalkr.dht.MasterNode;
import edu.gonzaga.mold.trashtalkr.gui.TrashGUI;
import edu.gonzaga.mold.trashtalkr.util.Util;

public class EntryPoint {
	private static TrashGUI gui = null;
	private static MasterNode master;
	private static Logger logger = LoggerFactory.getLogger(EntryPoint.class);

	/**
	 * Main entry point of program
	 * 
	 * @param args
	 *            program arguments
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 1 && args[0].equals("headless")) {
			master = new MasterNode(Util.getLocalAddress());
			logger.info("Listening on address " + Util.getLocalAddress());
		} else if (args.length == 2 && args[0].equals("bootstrap")) {
			master = new MasterNode(args[1]);
			gui = new TrashGUI(args[1], "Peer_Leader", master);
		} else if (args.length == 1) {
			gui = new TrashGUI(args[0], "Unamed_warrior");
		} else {
			gui = new TrashGUI(getWantedAddress(), getWantedName());
		}
		if (gui != null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						gui.init();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	/**
	 * Gets the IP Address to connect to
	 * 
	 * @return ipToConnect
	 */
	private static String getWantedAddress() {
		String ipToConnect = "";
		JFrame frame = new JFrame("InputDialog Example #1");
		ipToConnect = JOptionPane.showInputDialog(frame, "Please enter IP address:");
		return ipToConnect;
	}

	/**
	 * Gets the name you want to be called
	 * 
	 * @return name
	 */
	private static String getWantedName() {
		String name = "";
		JFrame frame = new JFrame("InputDialog Example #1");
		name = JOptionPane.showInputDialog(frame, "Please enter your desired name:");
		return name;
	}
}