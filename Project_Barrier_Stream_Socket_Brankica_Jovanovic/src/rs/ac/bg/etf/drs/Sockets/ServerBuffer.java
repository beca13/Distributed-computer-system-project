package rs.ac.bg.etf.drs.Sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerBuffer {

	public static void main(String[] args) {
		int cap = 10;
		int n = 2;// 5

		BufferInterface buffer1 = new Buffer(cap);
		BufferInterface buffer2 = new Buffer(cap);
		BufferInterface buffer3 = new Buffer(cap);

		BarrierInterface barrier = new Barrier(n);

		int port = 8080;

		try (ServerSocket server = new ServerSocket(port);) {
			while (true) {
				new WorkingThread(server.accept(), buffer1, buffer2, buffer3, barrier).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static class WorkingThread extends Thread {
		Socket socket;
		BufferInterface buffer1;
		BufferInterface buffer2;
		BufferInterface buffer3;

		BarrierInterface barrier;

		public WorkingThread(Socket socket, BufferInterface buffer1, BufferInterface buffer2, BufferInterface buffer3,
				BarrierInterface barrier) {
			this.socket = socket;
			this.buffer1 = buffer1;
			this.buffer2 = buffer2;
			this.buffer3 = buffer3;
			this.barrier = barrier;
		}

		public void run() {
			try (Socket socket = this.socket;
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);) {
				String command = reader.readLine();
				System.out.println("Command: " + command + ", " + socket.getInetAddress());
				String result;
				switch (command.toLowerCase()) {
				case "put buff1":
					String line1 = reader.readLine();
					if ("null".equals(line1)) {
						line1 = null;
					}
					buffer1.put(line1);
					result = "\n";
					break;
				case "get buff1":
					result = buffer1.get() + "\n";
					break;
				case "put buff2":
					String line2 = reader.readLine();
					buffer2.put(line2);
					result = "\n";
					break;
				case "get buff2":
					result = buffer2.get() + "\n";
					break;
				case "put buff3":
					String line3 = reader.readLine();
					buffer3.put(line3);
					result = "\n";
					break;
				case "get buff3":
					result = buffer3.get() + "\n";
					break;
				case "sync":
					// String line4 = reader.readLine();
					// Integer.parseInt(line4)
					// int val
					System.out.println("Command: sync barrier call");
					barrier.sync();
					// result = val + "\n";
					result = "\n";
					break;
				default:
					result = "bad command\n";
					break;
				}

				writer.write(result);
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
