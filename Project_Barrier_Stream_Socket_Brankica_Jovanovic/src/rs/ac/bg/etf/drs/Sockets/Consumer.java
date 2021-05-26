package rs.ac.bg.etf.drs.Sockets;

import java.util.HashMap;
import java.util.Map;

public class Consumer extends Thread {

	BufferInterface bufferInListaFilmovaSaReziserima, bufferOut;
	BarrierInterface barrier;
	String filmovi;
	Map<String, String> trajanjeFilmovaMapa = new HashMap<String, String>();

	public Consumer(BufferInterface bufferIn, BarrierInterface barrier, BufferInterface bufferOut, String filmovi,
			Map<String, String> trajanjeFilmovaMapa) {
		super();
		this.bufferInListaFilmovaSaReziserima = bufferIn;
		this.barrier = barrier;
		this.bufferOut = bufferOut;
		this.filmovi = filmovi;
		this.trajanjeFilmovaMapa = trajanjeFilmovaMapa;
	}

	@Override
	public void run() {
		System.out.println("Consumer pocetak.");

		// 1. consumer radi u petlji dokle god ima zadataka
		// 2. Uzima jedan po jedan zadatak
		// 3. Obradjuje zadatak
		// 4. Objavljuje lokalni rezultat
		String line = null;
		int brojObradjenih = 0;
		while ((line = bufferInListaFilmovaSaReziserima.get()) != null) {
			brojObradjenih++;
			// Line = jedan film
			// Izgled linije (primer): 12312124<TAB>rez1,rez2,rez3<TAB>pisac1,pisac2
			// tconst directors writers
			// tt0000001 nm0005690 \N
			String[] elementiOdvojeniTabom = line.split("\t");
			if (!("\\N".equals(elementiOdvojeniTabom[1]))) {
				String idFilma = elementiOdvojeniTabom[0];// id mi treba i reziseri
				String[] reziseri = elementiOdvojeniTabom[1].split(",");
				String minuti = trajanjeFilmovaMapa.get(idFilma);

				if (minuti == null) {
					minuti = "0";
				} else {
				}

				int numberOfItems = reziseri.length;

				for (int i = 0; i < numberOfItems; i++) {
					String reziseriMinuti = reziseri[i] + "," + minuti;
					bufferOut.put(reziseriMinuti);
				}

			}

		}

		System.out.println("Consumer run is finished. Waiting on barrier for others.");

		barrier.sync();//

		System.out.println("Consumer barrier wait is done.");

		bufferOut.put(null); // buffer.end == buffer.put(null)

	}

}