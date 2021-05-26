package rs.ac.bg.etf.drs.Barrier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Combiner extends Thread {

	Buffer bufferIn, bufferOut;

	public Combiner(Buffer bufferIn, Buffer bufferOut) {
		super();
		this.bufferIn = bufferIn;
		this.bufferOut = bufferOut;
	}

	@Override
	public void run() {

		Integer ukupniMinuti = 0;
		String reziser = null;
		String porukaOdConsumera = null;
		Map<String, Integer> map = new HashMap<String, Integer>();
		int primljenihPoruka = 0;

		while ((porukaOdConsumera = bufferIn.get()) != null) { // svi cekaju na barijeri i null se salje kad svi zavrse

			primljenihPoruka++;

			// 1. parse
			String[] elementiOdvojeniZarezom = porukaOdConsumera.split(",");

			// 2. uzmi minute -> integer
			Integer minuti = Integer.parseInt(elementiOdvojeniZarezom[1]);

			// 3. dodaj minute na rezisera u hashmapi
			// myMap.put(key, myMap.get(key) + 1)

			// 3.1. vidi da li postoji kljuc i ako postoji uzmi vrednost
			reziser = elementiOdvojeniZarezom[0];
			Integer lokalniZbirniMinuti = map.get(reziser);
			if (map.get(reziser) == null) {
				lokalniZbirniMinuti = 0;// ako kljuc ne postoji, lokalniZbirniMinuti su null
			}
			// 3.3. dodaj na lokalne zbirne minute, trenutne minute od consumera
			ukupniMinuti = minuti + lokalniZbirniMinuti;

			// 3.4. vrati na mesto elementa u hashmap-i
			map.put(reziser, ukupniMinuti);
			
//			map = map.entrySet().stream()
//		    .sorted(Map.Entry.comparingByValue()) 			
//		    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
//		    (oldValue, newValue) -> oldValue, LinkedHashMap::new));

		}
		bufferOut.put(map.toString());
		// bufferOut.put(gotovo); // buffer.end
		bufferOut.put(null);
	}

}
