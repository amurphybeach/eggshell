package edu.gonzaga.mold.trashtalkr;

import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.gonzaga.mold.trashtalkr.dht.MasterNode;
import edu.gonzaga.mold.trashtalkr.gui.TrashGUI;
import edu.gonzaga.mold.trashtalkr.gui.TrashMenuGui;
import edu.gonzaga.mold.trashtalkr.util.Util;

public class EntryPoint {
	private static TrashGUI gui = null;
	private static MasterNode master;
	private static Logger logger = LoggerFactory.getLogger(EntryPoint.class);
	private static TrashMenuGui tmg;

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
		} else if (args.length == 1 && args[0].equals("bootstrap")) {
			master = new MasterNode(Util.getLocalAddress());
			gui = new TrashGUI(Util.getLocalAddress(), getWantedName(), master);
		} else
			run();

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
	 * Gets the name you want to be called
	 * 
	 * @return name
	 */
	private static String getWantedName() throws Exception {
		String name = "";
		JFrame frame = new JFrame("Display Name");
		name = JOptionPane.showInputDialog(frame, "Please enter your desired name:");
		return name;
	}

	/**
	 * Builds menu and then waits for user to get their shit together
	 */
	public static void run() throws InterruptedException, UnknownHostException, IOException, ClassNotFoundException {
		tmg = new TrashMenuGui();
		tmg.getFrame().setVisible(true);
		while (tmg.getFrame().isVisible()) {
			// Stall until user makes up his/her mind, shit will not work if
			Thread.sleep(250);
		}
		if (!tmg.getFrame().isVisible()) {
			if (tmg.isMaster()) {
				logger.info("User is Master of his or her own Domain");
				master = new MasterNode(Util.getLocalAddress());
				gui = new TrashGUI(Util.getLocalAddress(), tmg.getName(), master);
			} else {
				gui = new TrashGUI(tmg.getIp(), tmg.getName());
			}
		}
		tmg.getFrame().dispose();

	}

}