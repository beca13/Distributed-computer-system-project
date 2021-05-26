package rs.ac.bg.etf.drs.Sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BufferNet implements BufferInterface {
	String name;
	String host;
	int port;

	public BufferNet(String name, String host, int port) {
		this.name = name;
		this.host = host;
		this.port = port;
	}

	public void put(String line) {
		try (Socket socket = new Socket(host, port)) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

			String command = "put " + name + "\n" + line + "\n";
			// System.out.println(command);
			writer.write(command);
			writer.flush();

			reader.readLine();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String get() {
		String res = "";
		try (Socket socket = new Socket(host, port)) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

			String command = "get " + name + "\n";

			writer.write(command);
			writer.flush();

			res = reader.readLine();
			if ("null".equals(res)) {
				res = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
