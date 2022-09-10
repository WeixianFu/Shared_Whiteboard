// Student Name: Weixian Fu
// Student ID: 1183945

package src;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import src.ServerServiceIMine;

public class StartServer {

	public StartServer() {

	}

	public void start(String port, String address) throws Exception {

		int portnumber = Integer.parseInt(port);
		ServerService server = new ServerServiceIMine();
		LocateRegistry.createRegistry(portnumber);
		Naming.bind(address, server);

	}

}
