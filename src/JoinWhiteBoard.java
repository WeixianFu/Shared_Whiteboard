// Student Name: Weixian Fu
// Student ID: 1183945

package src;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class JoinWhiteBoard {

	public static void main(String[] args) {
		
		int count = args.length;
		if (count!=3) {
			System.out.print("Should have 3 args\n");
			System.exit(0);
		}
		
		String host = args[0];
		String port = args[1];
		String boardname = args[2];
		
//		String host = "localhost";
//		String port = "5555";
//		String boardname = "test";

		JoinWhiteBoard start = new JoinWhiteBoard();
//		start.loginUI();
		start.initialize(host, port, boardname);
	}

	private JFrame firstframe;
	protected ServerService server;
	private JPanel usernamePane;
	private JLabel feedback;
	private JTextField userTxt;
	
	public void initialize(String hoString, String portString, String boarString) {
		
		
//		this.clientConnectPane();
//		
//		firstframe.setVisible(true);
		
		
		String host = hoString;
		String port = portString;
		String boardName = boarString;
		
//		int count = args.lenght;
//		if (count==3) {
//			System.out.print("Should have 3 args\n");
//			System.exit(0);
//		}
		
		if (host.equals(null) || port.equals(null) || boardName.equals(null) || host.equals("")
				|| port.equals("") || boardName.equals("")) {
			JOptionPane.showMessageDialog(firstframe, "Plz input something", "ERROR",
					JOptionPane.WARNING_MESSAGE);
		} else {
			String address = "//" + host + ":" + port + "/" + boardName;
			try {
				server = (ServerService) Naming.lookup(address);
				
				firstframe = new JFrame();
				firstframe.setBounds(100, 100, 450, 300);
				firstframe.setResizable(false);
				firstframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				firstframe.setVisible(true);
				
				this.usernamePane();
				firstframe.setVisible(true);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(firstframe, "Cannot find WhiteBoard.\nPlease retry!",
						"ERROR", JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	

	

	public void usernamePane() {
		usernamePane = new JPanel();
		usernamePane.setBackground(new Color(160,214,190));
		usernamePane.setBorder(new EmptyBorder(5, 5, 5, 5));
		firstframe.setContentPane(usernamePane);
		usernamePane.setLayout(null);

		JLabel portLb = new JLabel("Cilent Name");
		portLb.setBounds(50, 84, 85, 50);
		usernamePane.add(portLb);

		userTxt = new JTextField();
		userTxt.setBounds(147, 95, 170, 29);
		usernamePane.add(userTxt);

		feedback = new JLabel("");
		feedback.setBounds(147, 139, 170, 16);
		usernamePane.add(feedback);

		JButton startpaint = new JButton("Connect Now!");
		startpaint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = userTxt.getText();
				if (username.equals(null) || username.equals("")) {
					JOptionPane.showMessageDialog(firstframe, "The Name cannot be empty!\nPlease input your username", "Client -- " + username,
							JOptionPane.WARNING_MESSAGE);
					userTxt.setText(null);
				} else {
					try {
						ClientUI client = new ClientUI(server);
						if(server.checkUserName(username)){
							JOptionPane.showMessageDialog(firstframe, "Great! You can have this name.\nwaiting fo approval...", "Client -- " + username,
									JOptionPane.PLAIN_MESSAGE);
							if(server.approval(username)){
								client.setUsername(username);
								server.register(username, client);
								JOptionPane.showMessageDialog(firstframe, "Welcome, you are approved.",  "Client -- " + username, JOptionPane.PLAIN_MESSAGE);
								firstframe.setVisible(false);
								server.boardcastClient(username);
								client.initialize();
							}else{
								JOptionPane.showMessageDialog(firstframe, "Sorry,you didn't get the approval from Manager." , "Client -- " + username,
										JOptionPane.WARNING_MESSAGE);
								System.exit(0);
							}
						}else{
							JOptionPane.showMessageDialog(firstframe, "Sorry, this user name has been used.");
							userTxt.setText(null);
						}
					} catch (RemoteException e1) {
						JOptionPane.showMessageDialog(firstframe, "ERROR", "ERROR", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		startpaint.setBounds(147, 178, 170, 29);
		usernamePane.add(startpaint);

	}

	
}
