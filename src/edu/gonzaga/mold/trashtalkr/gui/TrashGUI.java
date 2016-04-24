package edu.gonzaga.mold.trashtalkr.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import edu.gonzaga.mold.trashtalkr.EntryPoint;
import edu.gonzaga.mold.trashtalkr.chat.ChatMessage;
import edu.gonzaga.mold.trashtalkr.chat.MessageListener;
import edu.gonzaga.mold.trashtalkr.chat.User;
import edu.gonzaga.mold.trashtalkr.dht.MasterNode;
import edu.gonzaga.mold.trashtalkr.util.Util;

public class TrashGUI extends JFrame {
	private static final long serialVersionUID = -2719771220017509611L;

	private JPanel contentPane;
	private JTextArea chatBox;
	private JTextField inputBox;
	private JButton postButton;
	private MasterNode masterNode;
	private String ipMaster;
	private User you;
	private JButton backBtn;

	/**
	 * Creates a new GUI
	 * 
	 * @param ip
	 *            the IP of the master node
	 * @param masterNode
	 *            the master node, if bootstrapping the network
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public TrashGUI(String ip, String display, MasterNode masterNode) throws IOException, ClassNotFoundException {
		this.ipMaster = ip;
		this.masterNode = masterNode;
		you = new User(ipMaster, display);
		buildUI();
		addListeners();
	}

	/**
	 * Creates a new GUI (no bootstrapping)
	 * 
	 * @param ip
	 *            the IP of the master node
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public TrashGUI(String ip, String display) throws ClassNotFoundException, IOException {
		this(ip, display, null);
	}

	/**
	 * Initializes the GUI
	 */
	public void init() {
		try {
			if (you.connect()) {
				this.updateChatBox(you.checkMessages());
				this.setVisible(true);
			} else {
				System.out.println("FAILED TO CONNECT xD");
				you.disconnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the UI
	 */
	private void buildUI() {
		setTitle("TrashTalkr");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setBackground(UIManager.getColor("Desktop.background"));
		contentPane.setLayout(null);
		setLocationRelativeTo(null);

		JLabel welcomeLbl = new JLabel("Welcome to TrashTalkr - It's TRASH");
		welcomeLbl.setForeground(Color.WHITE);
		welcomeLbl.setBackground(Color.WHITE);
		welcomeLbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
		welcomeLbl.setBounds(100, 6, 233, 16);
		contentPane.add(welcomeLbl);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 22, 438, 207);
		contentPane.add(scrollPane);

		chatBox = new JTextArea();
		DefaultCaret caret = (DefaultCaret) chatBox.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scrollPane.setViewportView(chatBox);
		chatBox.setEditable(false);
		chatBox.setLineWrap(true);

		inputBox = new JTextField();
		inputBox.setBounds(6, 233, 247, 26);
		inputBox.setBackground(Color.WHITE);
		contentPane.add(inputBox);
		inputBox.setColumns(10);

		postButton = new JButton("TrashTalk");
		postButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
		postButton.setBounds(265, 233, 117, 29);
		contentPane.add(postButton);

		// Fucking shit doesn't work LMAO LMAO LMAO
		backBtn = new JButton("X");
		backBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				you.disconnect();
				if (masterNode != null) {
					masterNode.halt();
				}
				dispose();
				try {
					EntryPoint.run();
				} catch (ClassNotFoundException | InterruptedException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		backBtn.setBounds(387, 234, 57, 29);
		contentPane.add(backBtn);
	}

	/**
	 * Adds listeners to all inputs and outputs
	 */
	private void addListeners() {
		// Should work
		Action sendMessageAction = new AbstractAction() {
			private static final long serialVersionUID = 3265672839752886129L;

			@Override
			public void actionPerformed(ActionEvent ae) {
				if (you.connect()) {
					String input = inputBox.getText();
					if (!input.equals("")) {
						try {
							you.postMessage(input);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				inputBox.setText("");
			}
		};
		inputBox.addActionListener(sendMessageAction);
		postButton.addActionListener(sendMessageAction);

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				you.disconnect();
				if (masterNode != null) {
					masterNode.halt();
				}
				System.exit(0);
			}
		});

		you.addMessageListener(new MessageListener() {
			@Override
			public void onMessage(List<ChatMessage> messages) {
				updateChatBox(messages);
			}
		});
	}

	/**
	 * Helper method to update the chatbox with new messages
	 * 
	 * @param messages
	 *            the messages to put in the chatbox
	 */
	private void updateChatBox(List<ChatMessage> messages) {
		chatBox.setText("");
		for (ChatMessage message : messages) {
			chatBox.append(message.toString() + "\n");
		}
	}
}
