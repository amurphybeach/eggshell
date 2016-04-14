package edu.gonzaga.mold.trashtalkr.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import edu.gonzaga.mold.trashtalkr.chat.ChatMessage;
import edu.gonzaga.mold.trashtalkr.chat.User;

public class TrashGui extends JFrame {

	private Vector<String> messageList = new Vector<String>();
	private JPanel contentPane;
	private static String ipMaster;
	private User you;

	public TrashGui(String ip) throws IOException {
		this.ipMaster = ip;
		you = new User(ipMaster);
		
		if(you.connect()){
			System.out.println("We good fam");
		}
		else{
			System.out.println("Failure to connect");
		}

		setTitle("TrashTalkr");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JList list = new JList();
		list.setBounds(10, 11, 414, 182);
		contentPane.add(list);

		JTextPane textPane = new JTextPane();
		textPane.setBounds(20, 204, 193, 31);
		contentPane.add(textPane);

		JButton btnNewButton = new JButton("TrashTalk");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (you.connect()) {
					String inLine = textPane.getText();
					if (inLine != "") {
						List<ChatMessage> messages = null;
						try {
							messages = you.checkMessages();
						} catch (ClassNotFoundException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						Collections.sort(messages);
						for (ChatMessage m : messages) {
							System.out.println(m.getUserId().toString() + ": " + m.getMessage());
							list.setListData(messageList);
						}
						try {
							you.postMessage(inLine);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				System.out.println();
				// you.disconnect();
			}
		});
		btnNewButton.setBounds(228, 212, 121, 23);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("X");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				you.disconnect();
			}
		});
		btnNewButton_1.setBounds(359, 212, 50, 23);
		contentPane.add(btnNewButton_1);

		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				you.disconnect();
				System.exit(0);
			}
		});
	}

}
