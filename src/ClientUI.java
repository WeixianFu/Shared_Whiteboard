// Student Name: Weixian Fu
// Student ID: 1183945

package src;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ClientUI extends ClientServiceImpl implements MouseListener, MouseMotionListener, ActionListener {

	private static final long serialVersionUID = 1L;
	protected ServerService server;
	private JFrame frame;
	private JMenuBar menu;
	private JButton colourChooser;
	private JToolBar toolBar;

//	private String tools[] = { "pen", "eraser", "line", "circle", "oval", "rect", "roundrect", "word" };
//	private String tip[] = { "Pencil", "Eraser", "Line", "Circle", "Oval", "Rectangle", "RoundRect", "Word" };
	
	private String tools[] = {"line", "circle", "rectangle", "triangle", "text", "pen", "eraser", "oval",  "roundrect" };
	private String tip[] = { "Line", "Circle", "Rectangle", "Triangle", "Text", "Pencil", "Eraser", "Oval", "RoundRect"};
	
//	private String tools[] = {"line", "circle", "rectangle", "text", "pen", "eraser", "oval",  "roundrect"};
//	private String tip[] = { "Line", "Circle", "Rectangle", "Text", "Pencil", "Eraser", "Oval", "RoundRect"};

	private Icon icons[];
	private Icon colourChooserIcon;
	private JButton button[];
	private JMenuItem exit;
	protected Component whiteboard;
	protected DrawArea drawArea;
	private String toolChoice = "";
	protected Image buffer;
	private Color color = Color.black;
	private int R = 0, G = 0, B = 0;
	private int x1, y1;
	private int x2, y2;
	private JPanel rightpanel;
	private JPanel rightdown;
	private JTextArea messageTxt;
	private JTextArea messageArea;
	private JTextArea memberArea;
	private JButton sendButton;
	private JScrollPane memberScrollPanel;
	private JScrollPane messageScrollPanel;
	private JScrollPane txtScorllPanel;
	private String username;
	private JLabel editLb;

	protected ClientUI(ServerService server) throws RemoteException {
		super();
		this.username = "Manager";
		this.server = server;

	}

	public void initialize() {
		frame = new JFrame("White Board: Guest -- " + this.username);

		// initialize menu bar
		menu = new JMenuBar();
		menu.setBackground(null);

		frame.setJMenuBar(menu);

		// initialize menu item File
	
		
		// right panel
		rightpanel = new JPanel();
		rightpanel.setPreferredSize(new Dimension(200, 200));
		frame.getContentPane().add(rightpanel, BorderLayout.EAST);
		rightpanel.setLayout(new BorderLayout(0, 0));
//		rightpanel.setBorder(new TitledBorder("Member Window"));

		rightdown = new JPanel();
		rightdown.setPreferredSize(new Dimension(200, 50));
		rightpanel.add(rightdown, BorderLayout.SOUTH);
		rightdown.setLayout(new BorderLayout(0, 0));

		sendButton = new JButton("Send");
		sendButton.addActionListener(this);
		sendButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		rightdown.add(sendButton, BorderLayout.EAST);

		txtScorllPanel = new JScrollPane();
		messageTxt = new JTextArea();
		messageTxt.setLineWrap(true);
		messageTxt.setWrapStyleWord(true);
		messageTxt.setBorder(new EmptyBorder(5, 5, 5, 5));
		 rightdown.add(messageTxt, BorderLayout.CENTER);
		rightdown.add(txtScorllPanel, BorderLayout.CENTER);
		txtScorllPanel.setViewportView(messageTxt);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		rightpanel.add(splitPane, BorderLayout.CENTER);

		messageScrollPanel = new JScrollPane();
		messageArea = new JTextArea();
		messageScrollPanel.setBorder(new TitledBorder("Message"));
		messageArea.setEditable(false);
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		splitPane.setRightComponent(messageScrollPanel);
		messageScrollPanel.setViewportView(messageArea);
		try {
			for (String message : server.getMessageList()) {
				messageArea.append(message + "\n");
			}
		} catch (Exception e) {
		}

		memberScrollPanel = new JScrollPane();
		memberArea = new JTextArea();
		memberScrollPanel.setBorder(new TitledBorder("Member"));
		memberArea.setLineWrap(true);
		memberArea.setWrapStyleWord(true);
		memberArea.setEditable(false);
		splitPane.setLeftComponent(memberScrollPanel);
		memberScrollPanel.setViewportView(memberArea);
		try {
			for (Entry<String, ClientService> entry : server.getClientsList().entrySet()) {
				memberArea.append(entry.getKey() + "\n");
			}
		} catch (Exception e) {
			System.out.print("Oops, Something went wrong in Member Area, the wrong info is: ");
			System.out.print(e);
		}

		// the design of tool bar
		toolBar = new JToolBar(JToolBar.HORIZONTAL);
		toolBar.setBorder(new TitledBorder("Tools"));
		toolBar.setFloatable(false);

		icons = new ImageIcon[tools.length];
		button = new JButton[tools.length];
		for (int i = 0; i < tools.length; i++) {
			icons[i] = new ImageIcon(getClass().getResource("/icon/" + tools[i] + ".png"));
			button[i] = new JButton("", icons[i]);
			button[i].setToolTipText(tip[i]);
			toolBar.add(button[i]);
			button[i].setBackground(Color.white);
			button[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			button[i].addActionListener(this);
		}
		frame.getContentPane().add(toolBar, BorderLayout.SOUTH);
		
		

		colourChooserIcon = new ImageIcon(getClass().getResource("/icon/color.png"));
		colourChooser = new JButton("", colourChooserIcon);
		colourChooser.setBackground(Color.white);
		colourChooser.addActionListener(this);
		toolBar.add(colourChooser);
		
		
		editLb = new JLabel();
		toolBar.add(editLb);

		drawArea = new DrawArea();
		drawArea.setBackground(Color.white);
		frame.getContentPane().add(drawArea, BorderLayout.CENTER);

		// initialize frame
		frame.setBounds(720, 480, 720, 480);
		frame.setVisible(true);
		frame.validate();
		// frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					server.leave(username);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					System.out.print("Oops, Something went wrong in Leaving ");
					System.out.print(e1);
				}
				System.exit(0);
			}
		});

		whiteboard = drawArea;
		whiteboard.addMouseListener(this);
		whiteboard.addMouseMotionListener(this);

	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exit)// exit
		{
			try {
				server.leave(username);
			} catch (RemoteException e1) {
				System.out.print("Oops, Something went wrong in exit ");
				System.out.print(e1);
			}
			System.exit(0);
		} else if (e.getSource() == sendButton) {
			String s = messageTxt.getText();
			try {
				if (s.equals(null) || s.equals("")) {

				} else {
					Date d = new Date(); 
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");  
			        String dateNowStr = sdf.format(d); 
					String message = dateNowStr+" "+username + ": " + s;
					server.boradcastMessage(message);
				}
			} catch (RemoteException e1) {
				System.out.print("Oops, Something went wrong in sending ");
				System.out.print(e1);
			}
			messageTxt.setText(null);
		} else if (e.getSource() == colourChooser) {// get color chooser
			colorPallet();
		}

		for (int i = 0; i < tools.length; i++) {
			if (e.getSource() == button[i]) {
				toolChoice = tip[i];
			}
		}
	}

	public void colorPallet() {
		color = JColorChooser.showDialog(null, "Choose your color", color);
		try {
			R = color.getRed();
			G = color.getGreen();
			B = color.getBlue();
		} catch (Exception e) {
			R = 0;
			G = 0;
			B = 0;
		}

	}

	public void redraw() throws RemoteException {
		drawArea.repaint();
	}

	class DrawArea extends Canvas {

		private static final long serialVersionUID = 1L;

		public DrawArea() {
		}

		public void paint(Graphics g) {
			try {
				Vector<Shape> shapeList = server.getShapeList();
				for (Shape s : shapeList) {
					drawTask(s);
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	

	public void notifyKick() throws RemoteException {
		new Thread() {
			@Override
			public void run() {
				JOptionPane.showMessageDialog(whiteboard, "Sorry! You have been kicked out by Manager", "Attention",
						JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
		}.start();
	}

	public void notifyKickMessage(String message) throws RemoteException {
		new Thread() {
			@Override
			public void run() {
				messageArea.append(message);
				resetMemberboard();
			}
		}.start();
	}

	public boolean notifyTask(Shape s) throws RemoteException {
		new Thread() {
			@Override
			public void run() {
				drawTask(s);
			}
		}.start();
		return true;
	}

	public void notifyMessage(String txtmessage) throws RemoteException {
		new Thread() {
			@Override
			public void run() {
				messageArea.append(txtmessage + "\n");
			}
		}.start();
	}

	public void notifyClient(String s) throws RemoteException {
		new Thread() {
			@Override
			public void run() {
				if (!s.equals(username)) {
					Date d = new Date(); 
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");  
			        String dateNowStr = sdf.format(d); 
					messageArea.append(dateNowStr+" Manager: Welcom " + s + ".\n");
					resetMemberboard();
				}
			}
		}.start();
	}

	public void notifyNewFile() throws RemoteException {
		new Thread() {
			@Override
			public void run() {
				JOptionPane.showMessageDialog(whiteboard, "The manager has newed a empty board", "Attention",
						JOptionPane.WARNING_MESSAGE);
				try {
					redraw();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void notifyOpenFile() throws RemoteException {
		new Thread() {
			@Override
			public void run() {
				JOptionPane.showMessageDialog(whiteboard, "The manager has opened a file", "Attention",
						JOptionPane.WARNING_MESSAGE);
				try {
					redraw();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void notifyManagerLeave() throws RemoteException {
		new Thread() {
			@Override
			public void run() {
				JOptionPane.showMessageDialog(whiteboard, "The manager is closing the board", "Attention",
						JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
		}.start();
	}

	public void notifyClientLeave(String s) throws RemoteException {
		new Thread() {
			@Override
			public void run() {
				Date d = new Date(); 
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");  
		        String dateNowStr = sdf.format(d); 
				messageArea.append(dateNowStr+" Manager: " + s + " has left.\n");
				resetMemberboard();
			}
		}.start();
	}

	public void resetMemberboard() {
		memberArea.setText(null);
		try {
			for (Entry<String, ClientService> entry : server.getClientsList().entrySet()) {
				memberArea.append(entry.getKey() + "\n");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void drawTask(Shape s) {

		int x1 = s.getX1();
		int x2 = s.getX2();
		int y1 = s.getY1();
		int y2 = s.getY2();
		int R = s.getR();
		int G = s.getG();
		int B = s.getB();
		String user = s.getUsername();
		

		editLb.setText("  " + user + " is editing...");
		Graphics2D gr = (Graphics2D) whiteboard.getGraphics();

		String shapeType = s.getType();
		switch (shapeType) {
		case "Pencil": {
			gr.setPaint(new Color(R, G, B));
			gr.drawLine(x1, y1, x2, y2);
			break;
		}
		case "Eraser": {
			gr.setPaint(Color.WHITE);
			gr.drawLine(x1, y1, x2, y2);
			break;
		}
		case "Line": {
			gr.setPaint(new Color(R, G, B));
			gr.drawLine(x1, y1, x2, y2);
			break;
		}
		case "Oval": {
			gr.setPaint(new Color(R, G, B));
			gr.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
			break;
		}
		case "Circle": {
			gr.setPaint(new Color(R, G, B));
			gr.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)),
					Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)));
			break;
		}
		case "Text": {
			gr.setPaint(new Color(R, G, B));
			gr.drawString(s.getTxt(), x1, y1);
			break;
		}
		case "Rectangle": {
			gr.setPaint(new Color(R, G, B));
			gr.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
			break;
		}
		case "RoundRect": {
			gr.setPaint(new Color(R, G, B));
			gr.drawRoundRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2), 50, 35);
			break;
		}
		case "Triangle": {
			gr.setPaint(new Color(R, G, B));
			int x3 = s.getX3();
			int y3 = s.getY3();
 			gr.drawLine(x1, y1, x2, y2);
 			gr.drawLine(x1, y1, x3, y3);
 			gr.drawLine(x2, y2, x3, y3);
			break;
		}
		default:
			System.out.print("Unexpected value: " + shapeType + "\n");
		}
	}
	
	


	@Override
	public void mouseDragged(MouseEvent ev) {
		// TODO Auto-generated method stub

		x2 = ev.getX();
		y2 = ev.getY();
		if (toolChoice.equals("Pencil") || toolChoice.equals("Eraser")) {
			x2 = ev.getX();
			y2 = ev.getY();
			Shape shape = new Shape(toolChoice, x1, x2, y1, y2,  R, G, B, username);
			try {
				server.broadcast(shape);
			} catch (RemoteException e) {
				System.out.print("Oops, Something went wrong when start Manager, the wrong info is:\n");
				System.out.print(e);
			}
			x1 = x2;
			y1 = y2;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent ev) {

	}

	@Override
	public void mousePressed(MouseEvent ev) {
		// TODO Auto-generated method stub

		x1 = ev.getX();
		y1 = ev.getY();
		x2 = x1;
		y2 = y1;
		if (toolChoice.equals("Pencil") || toolChoice.equals("Eraser")) {
//			int stroke = Integer.parseInt(strokeType.getSelectedItem().toString());
			Shape shape = new Shape(toolChoice, x1, x2, y1, y2, R, G, B, username);
			try {
				server.broadcast(shape);
			} catch (RemoteException e) {
				JOptionPane.showMessageDialog(whiteboard, "Drawing error", "Warning",
						JOptionPane.WARNING_MESSAGE);
			}
		} else if (toolChoice.equals("Text")) {
//			int stroke = Integer.parseInt(strokeType.getSelectedItem().toString());
//			String fonttype = fontlist.getSelectedItem().toString();
			Shape shape = new Shape(toolChoice, x1, x2, y1, y2, R, G, B, username);
			try {
				String word = JOptionPane.showInputDialog(whiteboard, "Plz input your text", "Kick",
						JOptionPane.PLAIN_MESSAGE, null, null, null).toString();
				if (word.equals(null) || word.equals("")) {
					JOptionPane.showMessageDialog(whiteboard, "Plz input something", "Warning",
							JOptionPane.WARNING_MESSAGE);
				} else {
					shape.setTxt(word);
//					shape.setFont(fonttype);
					server.broadcast(shape);
				}
			} catch (Exception e) {

			}

		}
	}

	@Override
	public void mouseReleased(MouseEvent ev) {
		// TODO Auto-generated method stub

		if (toolChoice.equals("Pencil") || toolChoice.equals("Eraser")) {
			x1 = ev.getX();
			y1 = ev.getY();
		}
		x2 = ev.getX();
		y2 = ev.getY();
		
		

		Shape shape = new Shape(toolChoice, x1, x2, y1, y2, R, G, B, username);
		if (toolChoice.equals("Triangle")) {
			System.out.print(shape.getType());
			Random rand = new Random();
			int x3 = rand.nextInt(480);
			int y3 = rand.nextInt(360);
			shape.setX3(x3);
			shape.setY3(y3);
		}

		shape.setFill(false);
//		if (fillchoose.isSelected()) {
//			shape.setFill(true);
//		}else{
//			shape.setFill(false);
//		}
		try {
			server.broadcast(shape);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void mouseEntered(MouseEvent ev) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent ev) {
		// TODO Auto-generated method stub

	}
	
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
