package rs.ac.bg.etf.drs.Sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BarrierNet implements BarrierInterface {
	String host;
	int port;

	public BarrierNet(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void sync() {
		String res = "";
		try (Socket socket = new Socket(host, port)) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

			String command = "sync\n";

			writer.write(command);
			writer.flush();

			reader.readLine();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
