package rs.ac.bg.etf.drs.Sockets;

import java.io.BufferedReader;
import java.io.FileReader;

public class Producer extends Thread {

	String reziseri;
	BufferInterface buffer;

	public Producer(String reziseri, BufferInterface buffer1) {
		super();
		this.reziseri = reziseri;
		this.buffer = buffer1;
	}

	@Override
	public void run() {

		try (BufferedReader reader = new BufferedReader(new FileReader(reziseri));) {
			String line = reader.readLine();
			int i = 0;
			while ((line = reader.readLine()) != null && i < 100000) {// && i<10
				i++;
				buffer.put(line);

			}
			buffer.put(null); // buffer.end == buffer.put(null)

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}