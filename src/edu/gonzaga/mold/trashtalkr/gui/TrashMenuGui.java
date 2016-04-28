package edu.gonzaga.mold.trashtalkr.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class TrashMenuGui {
	private JFrame frame;
	private String name = "";
	private String ipToJoin = "";
	private boolean isMaster = false;

	public TrashMenuGui() {
		frame = new JFrame();
		JPanel contentPane = new JPanel();
		JTextField ipTextField;
		JTextField nameTextField;

		frame.setTitle("TrashMenu");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 450, 300);
		frame.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 51, 153));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("TrashTalkr Menu");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 13));
		lblNewLabel.setBounds(174, 6, 104, 16);
		// contentPane.add(lblNewLabel);

		// Button to create a new lobby with you as host
		JButton btnCreateRoom = new JButton("Create Room");
		btnCreateRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isMaster = true;
				frame.setVisible(false);
			}
		});
		btnCreateRoom.setBounds(166, 90, 124, 29);
		contentPane.add(btnCreateRoom);

		JButton btnNewButton = new JButton();
		btnNewButton.setIcon(new ImageIcon(getClass().getResource("TrashTalkrLogo.png")));
		btnNewButton.setBounds(170, 6, 123, 54);
		contentPane.add(btnNewButton);

		// Text field for IP address of join room
		ipTextField = new JTextField();
		ipTextField.setBounds(166, 137, 130, 26);
		contentPane.add(ipTextField);
		ipTextField.setColumns(10);

		// Button for joining a lobby with correct IP address
		JButton btnJoinLobby = new JButton("Join Room");
		btnJoinLobby.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ipToJoin = ipTextField.getText();
				frame.setVisible(false);
			}
		});
		btnJoinLobby.setBounds(170, 165, 124, 29);
		contentPane.add(btnJoinLobby);

		// Textfield to enter a name Change
		nameTextField = new JTextField();
		nameTextField.setBounds(166, 197, 130, 26);
		contentPane.add(nameTextField);
		nameTextField.setColumns(10);
		nameTextField.setText(name);

		JButton btnChangeName = new JButton("Change Name");
		btnChangeName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				name = nameTextField.getText();
				nameTextField.setText(name);
			}
		});
		btnChangeName.setBounds(168, 225, 124, 29);
		contentPane.add(btnChangeName);

	}

	/**
	 * Returns the current frame
	 * 
	 * @return frame
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Gets current desired username
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets if the user is a master user
	 * 
	 * @return isMaster boolean
	 */
	public boolean isMaster() {
		return isMaster;
	}

	/**
	 * Gets the IP Address to connect to
	 * 
	 * @return ipToJoin
	 */
	public String getIp() {
		return ipToJoin;
	}
}
