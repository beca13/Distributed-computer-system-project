package rs.ac.bg.etf.drs.Barrier;

/**
 * Klasa koja uzima iz bafera rezultat i ispisuje ga.
 */
public class Printer extends Thread {

	BufferInterface buffer;

	public Printer(BufferInterface buffer) {
		this.buffer = buffer;
	}

	@Override
	public void run() {
		String line = null;
		while ((line = buffer.get()) != null) {
			//System.out.println(line);
		}
	}

}
