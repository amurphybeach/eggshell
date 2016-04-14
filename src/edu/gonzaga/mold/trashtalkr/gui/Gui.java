package edu.gonzaga.mold.trashtalkr.gui;

import java.awt.EventQueue;
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

public class Gui extends JFrame {

	private JPanel contentPane;
	private String ipMaster;
	private User you = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui frame = new Gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Gui() {
		setTitle("TrashTalkr");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
				try {
					you = new User(ipMaster);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
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
				you.disconnect();
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
	}

	Vector<String> messageList = new Vector<String>();

}
