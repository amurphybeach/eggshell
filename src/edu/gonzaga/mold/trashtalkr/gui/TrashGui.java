package edu.gonzaga.mold.trashtalkr.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import edu.gonzaga.mold.trashtalkr.chat.ChatMessage;
import edu.gonzaga.mold.trashtalkr.chat.User;

public class TrashGui extends JFrame {

	private Vector<String> messageList = new Vector<String>();
	private JButton btnNewButton;
	private JTextField textPane;
	private JTextArea chatBox;
	private JLabel welcomeLbl;
	private JScrollPane scrollPane;
	private List<ChatMessage> messages;
	private JPanel contentPane;
	private static String ipMaster;
	private User you;
	private int delay = 250; // every .25 seconds
	private Timer timer;

	public TrashGui(String ip) throws IOException, ClassNotFoundException {
		this.ipMaster = ip;
		you = new User(ipMaster);

		if (you.connect()) {
			System.out.println("We good fam");
		} else {
			System.out.println("Failure to connect");
		}

		messages = you.checkMessages();

		setTitle("TrashTalkr");
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(139, 0, 0));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		welcomeLbl = new JLabel("Welcome to TrashTalkr - It's TRASH");
		welcomeLbl.setForeground(Color.WHITE);
		welcomeLbl.setBackground(Color.WHITE);
		welcomeLbl.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
		welcomeLbl.setBounds(100, 6, 233, 16);
		contentPane.add(welcomeLbl);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 22, 438, 207);
		contentPane.add(scrollPane);

		chatBox = new JTextArea();
		scrollPane.setViewportView(chatBox);
		chatBox.setEditable(false);
		chatBox.setLineWrap(true);

		textPane = new JTextField();
		textPane.setBounds(6, 233, 247, 26);
		textPane.setBackground(Color.WHITE);
		contentPane.add(textPane);
		textPane.setColumns(10);

		btnNewButton = new JButton("TrashTalk");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (you.connect()) {
					String inLine = textPane.getText();
					if (inLine != "") {
						try {
							you.postMessage(inLine);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				System.out.println();
				chatBox.append("You are not connected but good try \n");
				textPane.setText("");
			}
		});
		btnNewButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 13));
		btnNewButton.setBounds(265, 233, 117, 29);
		contentPane.add(btnNewButton);

		ActionListener action = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					messages = you.checkMessages();
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (ChatMessage m : messages) {
					System.out.println(m.getUserId().toString() + ": " + m.getMessage());
					chatBox.append(m.getUserId().toString() + ": " + m.getMessage() + "\n");
					messageList.add(m.getUserId().toString() + ": " + m.getMessage());
				}
			}
		};

		timer = new Timer(delay, action);
		timer.setInitialDelay(0);
		timer.start();

		Action keyEnter = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (you.connect()) {
					String inLine = textPane.getText();
					if (inLine != "") {
						try {
							you.postMessage(inLine);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				System.out.println();
				chatBox.append("You are not connected but good try \n");
				textPane.setText("");
			}
		};

		textPane.addActionListener(keyEnter);

		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				you.disconnect();
				System.exit(0);
			}
		});
	}

}
