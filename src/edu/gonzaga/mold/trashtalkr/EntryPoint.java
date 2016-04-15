package edu.gonzaga.mold.trashtalkr;

import javax.swing.SwingUtilities;
import edu.gonzaga.mold.trashtalkr.dht.MasterNode;
import edu.gonzaga.mold.trashtalkr.gui.TrashGUI;

public class EntryPoint {
	private static TrashGUI gui;

	public static void main(String[] args) throws Exception {
		if (args.length == 2 && args[0].equals("bootstrap")) {
			MasterNode master = new MasterNode(args[1]);
			gui = new TrashGUI(args[1], master);
		} else if (args.length == 1) {
			gui = new TrashGUI(args[0]);
		}
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