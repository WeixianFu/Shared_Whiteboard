// Student Name: Weixian Fu
// Student ID: 1183945

package src;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;



public class CreateWhiteBoard {
	protected ServerService server;
	private String boardname;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			int count = args.length;
			if (count != 3) {
				System.out.print("Should have 3 args\n");
				System.exit(0);
			}
			
		} catch (Exception e) {
			System.out.print("Should have 3 args\\n");
			System.exit(0);
		}

		
		String host = args[0];
		String port = args[1];
		String boardname = args[2];
		
//		String host = "localhost";
//		String port = "5555";
//		String boardname = "test";

		
		if (host.equals(null) || port.equals(null) || boardname.equals(null) || host.equals("")
				|| port.equals("") || boardname.equals("")) {
			System.out.print("Input Error\n");
			System.exit(0);
		}
		
		try {
			String address = "//" + host + ":" + port + "/" + boardname;
			StartServer ss = new StartServer();
			ss.start(port, address);
			CreateWhiteBoard cwb = new CreateWhiteBoard();
			cwb.setBoardName(boardname);
			cwb.startServer1(address);
		}catch (Exception e1) {
			System.out.print("Oops, Something went wrong when start Manager, the wrong info is: ");
			System.out.print(e1);
		}

	}
	
	public String getBoardName() {
		return this.boardname;
	}
	
	public void setBoardName(String nameString) {
		this.boardname = nameString;
	}
	
	public void startServer1(String addressString) {
		try {
			server = (ServerService) Naming.lookup(addressString);
			ManagerUI manager = new ManagerUI(server);
			manager.setUsername(this.boardname);
			server.register("Manager", manager);
			
			manager.initialize();
		} catch (MalformedURLException e) {
			System.out.print("Oops, Something went wrong when start Manager, the wrong info is: \n");
			System.out.print(e);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.print("Oops, Something went wrong when start Manager, the wrong info is: \n");
			System.out.print(e);
		} catch (NotBoundException e) {
			System.out.print("Oops, Something went wrong when start Manager, the wrong info is: \n");
			System.out.print(e);
		}
	}

}
