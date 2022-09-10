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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.Iterator;

public class ManagerUI extends ClientServiceImpl implements MouseListener, MouseMotionListener, ActionListener {

	private static final long serialVersionUID = 1L;
	protected ServerService server;
	private JFrame frame;
	private JMenuBar menu;
	private JMenu fileMenu;
	private JMenuItem newFile;
	private JMenuItem openFile;
	private JMenuItem saveFile;
	private JMenuItem saveImg;
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
	private JMenuItem close;
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
	private JMenu groupMenu;
	private JMenuItem kick;
	private JLabel editLb;

	protected ManagerUI(ServerService server) throws RemoteException {
		super();
		this.username = "Manager";
		this.server = server;

	}

	public void initialize() {
		frame = new JFrame("White Board: Manager -- " + this.username);
		// initialize menu bar
		menu = new JMenuBar();
		fileMenu = new JMenu("File");
//		groupMenu = new JMenu("Group");

		menu.add(fileMenu);
//		menu.add(groupMenu);
		menu.setBackground(null);

		frame.setJMenuBar(menu);

		// initialize menu item File
		
		newFile = new JMenuItem("New");
		openFile = new JMenuItem("Open");
		saveFile = new JMenuItem("Save");
		saveImg = new JMenuItem("Save as Png");
		close = new JMenuItem("Close");
		
		
		newFile.addActionListener(this);
		openFile.addActionListener(this);
		saveFile.addActionListener(this);
		saveImg.addActionListener(this);
		close.addActionListener(this);

		fileMenu.add(newFile);
		fileMenu.add(openFile);
		fileMenu.add(saveFile);
		fileMenu.add(saveImg);
		fileMenu.add(close);

		kick = new JMenuItem("Kick");
		kick.addActionListener(this);
//		groupMenu.add(kick);
		
		// right panel
		rightpanel = new JPanel();
		rightpanel.setPreferredSize(new Dimension(200, 200));
		frame.getContentPane().add(rightpanel, BorderLayout.EAST);
		rightpanel.setLayout(new BorderLayout(0, 0));
		rightpanel.setBorder(new TitledBorder("Member Window"));

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
		messageScrollPanel.setBorder(new TitledBorder("Chat"));
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
		memberScrollPanel.setBorder(new TitledBorder("Users"));
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
			System.out.print("Oops, Something went wrong in User Area, the wrong info is: ");
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
		
		toolBar.add(kick);
		
		
		editLb = new JLabel();
		toolBar.add(editLb);

		drawArea = new DrawArea();
		drawArea.setBackground(Color.white);
		frame.getContentPane().add(drawArea, BorderLayout.CENTER);

		// initialize frame
		frame.setBounds(0, 0, 900, 500);
		frame.setVisible(true);
		frame.validate();
		// frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					server.managerLeave();
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
		if (e.getSource() == newFile) { // new file

			try {
				server.newFile();
			} catch (Exception e1) {
				System.out.print("Oops, Something went wrong when server saving: \n");
				System.out.print(e);
			}

		} else if (e.getSource() == openFile) {// open file

			JFileChooser filechooser = new JFileChooser();
			filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = filechooser.showOpenDialog(whiteboard);

			if (returnVal == JFileChooser.CANCEL_OPTION) {
				return;
			}
			File fileName = filechooser.getSelectedFile();
			fileName.canRead();
			if (fileName == null || fileName.getName().equals("")) {
				JOptionPane.showMessageDialog(filechooser, "File name", "plz input file name!",
						JOptionPane.ERROR_MESSAGE);
			}

			else {

				try {
					FileInputStream ifs = new FileInputStream(fileName);
					ObjectInputStream input = new ObjectInputStream(ifs);
					Vector<Shape> list = (Vector<Shape>) input.readObject();
					server.openFile(list);
					input.close();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(whiteboard, "File Open Error!", "ERROR", JOptionPane.ERROR_MESSAGE);
				}

			}

		} else if (e.getSource() == saveFile) {// save file
			JFileChooser filechooser = new JFileChooser();
			filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = filechooser.showSaveDialog(whiteboard);
			if (result == JFileChooser.CANCEL_OPTION) {
				return;
			}
			File fileName = filechooser.getSelectedFile();
			fileName.canWrite();
			if (fileName == null || fileName.getName().equals("")) {
				JOptionPane.showMessageDialog(filechooser, "File name", "plz input file name!",
						JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					fileName.delete();
					FileOutputStream fos = new FileOutputStream(fileName + ".whiteboard");
					ObjectOutputStream output = new ObjectOutputStream(fos);
					Vector<Shape> list = server.getShapeList();
					output.writeObject(list);
					output.close();
					fos.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		} else if (e.getSource() == saveImg) {
			JFileChooser filechooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("png", "jpeg");
			filechooser.setFileFilter(filter);
			filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = filechooser.showSaveDialog(whiteboard);
			if (result == JFileChooser.CANCEL_OPTION) {
				return;
			}
			File fileName = filechooser.getSelectedFile();

			fileName.canWrite();
			if (fileName == null || fileName.getName().equals("")) {
				JOptionPane.showMessageDialog(filechooser, "File name", "plz input file name!",
						JOptionPane.ERROR_MESSAGE);
			} else {

				try {
					fileName.delete();
					
					BufferedImage bImg = new BufferedImage(drawArea.getWidth(), drawArea.getHeight(), BufferedImage.TYPE_INT_RGB);
					Graphics2D g = bImg.createGraphics();
					g.setColor(color.white);
					g.fillRect(0, 0, drawArea.getWidth(), drawArea.getHeight());
					Vector<Shape> shapelist = server.getShapeList();
					
					Iterator i = shapelist.iterator();
				      while (i.hasNext()) {
				    	  Shape tempShape = (Shape) i.next();
				    	  SaveDrawTask(tempShape, g);
				      }				
					try { 
						if (ImageIO.write(bImg, "png", new File(fileName.getPath()+".png"))) {
							System.out.print("successful saved\n");
						}
						
					} catch (Exception e2) {
						// TODO: handle exception
						System.out.print("Oops, Something went wrong when server saving: \n");
						System.out.print(e2);
					}

				} catch (Exception e1) {
					System.out.print("Oops, Something went wrong when server saving: \n");
					System.out.print(e1);
				}
				
			}
			
		} else if (e.getSource() == close) {// exit
			try {
				server.managerLeave();
				System.exit(0);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			System.exit(0);
		} else if (e.getSource() == colourChooser) {// get color chooser
			colorPallet();
		} else if (e.getSource() == kick) {
			new Thread() {
				@Override
				public void run() {

					Object user = JOptionPane.showInputDialog(whiteboard, "Plz input the user name you want to kick:\n",
							"Kick", JOptionPane.PLAIN_MESSAGE, null, null, null);
					String username = (String) user;
					try {
						if (username.equals("Manager") || username.equals(null) || username.equals("")) {
							JOptionPane.showMessageDialog(whiteboard, "Plz input the right username", "Attention",
									JOptionPane.WARNING_MESSAGE);
						} else {
							if (server.kickUser(username)) {

							} else {
								JOptionPane.showMessageDialog(null, "Sorry! No such user", "Attention",
										JOptionPane.WARNING_MESSAGE);
							}
						}
					} catch (Exception e) {
						System.out.print("Oops, Something went wrong\n");
						System.out.print(e);
					}

				}
			}.start();

		} else if (e.getSource() == sendButton) {
			String s = messageTxt.getText();
			String txt = s;
			try {
				if (s.equals(null) || s.equals("")) {

				} else {
					Date d = new Date(); 
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");  
			        String dateNowStr = sdf.format(d); 
					String message = dateNowStr+" "+username + ": " + txt;
					server.boradcastMessage(message);
				}
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			messageTxt.setText(null);
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

	public synchronized boolean notifyTask(Shape s) throws RemoteException {

		new Thread() {
			@Override
			public void run() {
				drawTask(s);
			}
		}.start();
		return true;
	}

	public synchronized void notifyNewFile() throws RemoteException {
		redraw();

	}

	public synchronized void notifyManagerLeave() throws RemoteException {

	}

	public synchronized void notifyOpenFile() throws RemoteException {
		redraw();
	}

	public synchronized void notifyClientLeave(String s) throws RemoteException {
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

	public synchronized void notifyMessage(String txtmessage) throws RemoteException {
		new Thread() {
			@Override
			public void run() {
				messageArea.append(txtmessage + "\n");
			}
		}.start();

	}

	public synchronized void notifyKickMessage(String message) throws RemoteException {
		new Thread() {
			@Override
			public void run() {
				messageArea.append(message);
				resetMemberboard();
			}
		}.start();
	}

	public synchronized void notifyClient(String s) throws RemoteException {
		new Thread() {
			@Override
			public void run() {
				if (!s.equals(username)) {
					Date d = new Date(); 
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");  
			        String dateNowStr = sdf.format(d); 
					messageArea.append(dateNowStr+" Manager: Welcome " + s + ".\n");
					resetMemberboard();
				}
			}
		}.start();
	}
	
	public synchronized boolean notifyApproval(String username) throws RemoteException {
		int n = JOptionPane.showConfirmDialog(null, username+" wants to join the white board.", "User Join",JOptionPane.YES_NO_OPTION);
		if(n==1){
			return false;
		}
		return true;
	}

	public synchronized void resetMemberboard() {
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
		

		editLb.setText("Wow! " + user + " is drawing...");
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
	
	public void SaveDrawTask(Shape s, Graphics2D gr) {

		int x1 = s.getX1();
		int x2 = s.getX2();
		int y1 = s.getY1();
		int y2 = s.getY2();
		int R = s.getR();
		int G = s.getG();
		int B = s.getB();
		String user = s.getUsername();
		
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
			System.out.print("Type wrong " + shapeType + "\n");
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
//			int stroke = Integer.parseInt(strokeType.getSelectedItem().toString());
			Shape shape = new Shape(toolChoice, x1, x2, y1, y2,  R, G, B, username);
			try {
				server.broadcast(shape);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				String word = JOptionPane.showInputDialog(whiteboard, "Plz input your text", "Text",
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
