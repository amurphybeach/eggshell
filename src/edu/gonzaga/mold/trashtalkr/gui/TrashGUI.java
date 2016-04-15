package edu.gonzaga.mold.trashtalkr.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import edu.gonzaga.mold.trashtalkr.chat.ChatMessage;
import edu.gonzaga.mold.trashtalkr.chat.MessageListener;
import edu.gonzaga.mold.trashtalkr.chat.User;
import edu.gonzaga.mold.trashtalkr.dht.MasterNode;

public class TrashGUI extends JFrame {
	private static final long serialVersionUID = -2719771220017509611L;

	private JPanel contentPane;
	private JTextArea chatBox;
	private JTextField inputBox;
	private JButton postButton;

	private MasterNode masterNode;

	private String ipMaster;
	private User you;

	public TrashGUI(String ip, MasterNode masterNode) throws IOException, ClassNotFoundException {
		this.ipMaster = ip;
		this.masterNode = masterNode;
		you = new User(ipMaster);
		buildUI();
		addListeners();
	}

	public TrashGUI(String ip) throws ClassNotFoundException, IOException {
		this(ip, null);
	}

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

	private void buildUI() {
		setTitle("TrashTalkr");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		chatBox = new JTextArea();
		chatBox.setBounds(10, 11, 414, 182);
		chatBox.setEditable(false);
		chatBox.setLineWrap(true);

		JScrollPane scrollPane = new JScrollPane(chatBox);
		scrollPane.setBounds(10, 13, 412, 185);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane);

		inputBox = new JTextField();
		inputBox.setBounds(20, 204, 193, 31);
		contentPane.add(inputBox);

		postButton = new JButton("TrashTalk");

		postButton.setBounds(228, 212, 121, 23);
		contentPane.add(postButton);
	}

	private void addListeners() {
		postButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (you.connect()) {
					String inLine = inputBox.getText();
					if (!inLine.equals("")) {
						try {
							you.postMessage(inLine);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				inputBox.setText("");
			}
		});

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

	private void updateChatBox(List<ChatMessage> messages) {
		chatBox.setText("");
		for (ChatMessage message : messages) {
			chatBox.append(message.toString() + "\n");
		}
	}
}
