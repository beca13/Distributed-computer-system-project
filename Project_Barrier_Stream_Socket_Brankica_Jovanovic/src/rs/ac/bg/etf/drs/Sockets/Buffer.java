package rs.ac.bg.etf.drs.Sockets;

import java.util.ArrayList;
import java.util.List;

public class Buffer implements BufferInterface {

	private List<String> buffer;
	private boolean end = false;
	private int cap;

	public Buffer(int cap) {
		buffer = new ArrayList<String>(cap);
		this.cap = cap;
	}

	/**
	 * Ubacivanje elementa u bafer. Ubacivanjem null elementa signalizira se kraj
	 * ubacivanja.
	 * 
	 * @param line element koji treba ubaciti u bafer ili null
	 */
	@Override
	public synchronized void put(String line) {
		if (line == null) {
			// ako pristigne null, to nam oznacava da nece vise elemenata dolaziti i da je
			// kraj
			end = true;
		} else {
			while (buffer.size() == cap) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			buffer.add(line);
		}

		notifyAll();
		// Ako je dosao null, a bafer je bio prazan, neophodno je probuditi consumere
		// koji su eventualno cekali na element, kako bi videli da je kraj.
		// Najjednostavnija implementacija nam je sa notifyAll, koja svakako probudi sve
		// na kraju.

	}

	/**
	 * Dohvata element iz bafera, blokirajuci se dok element ne bude dostupan ili je
	 * kraj ubacivanju elemenata.
	 * 
	 * @return dok god nije signaliziran kraj niza elemenata koji su ubacivani u
	 *         bafer, vraca se ubacen element; u suprotnom, vraca se null
	 */
	@Override
	public synchronized String get() {

		String res = null;
		while (!end && buffer.size() == 0) {
			// ako nismo dosli do kraja, tj. nije (jos uvek) stigao null, a bafer je prazan,
			// cekamo
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (buffer.size() > 0) {
			// Uzmi prvu liniju iz bafera
			res = buffer.remove(0);
		}

		notifyAll();
		return res;
	}

}
