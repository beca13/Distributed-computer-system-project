package rs.ac.bg.etf.drs.Barrier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Main {
	public static Map<String, String> ucitajHashMap(String path) {

		Map<String, String> trajanjeFilmova = new HashMap<String, String>();

		try (BufferedReader reader = new BufferedReader(new FileReader(path));) {
			String lineFilmSaMinutima = reader.readLine();
			// int i = 0;
			while ((lineFilmSaMinutima = reader.readLine()) != null) {// && i < 10
				// i++;
				String[] elementiNiza = lineFilmSaMinutima.split("\t");
				if (!("\\N".equals(elementiNiza[7]))) {
					String idFilmaIzTabeleMinuti = elementiNiza[0];
					String minuti = elementiNiza[7];
					// tconst titleType primaryTitle originalTitle isAdult startYear endYear
					// runtimeMinutes genres
					// tt0000001 short Carmencita Carmencita 0 1894 \N 1 Documentary,Short
					// tt0000002 short Le clown et ses chiens Le clown et ses chiens 0 1892 \N 5
					// Animation,Short

					trajanjeFilmova.put(idFilmaIzTabeleMinuti, minuti);

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Kesiranje u memoriji je uradjeno.");

		return trajanjeFilmova;

	}

	public static void main(String[] args) {
		int cap = 500;
		int n = 3;
		String filmovi = "data_filmovi.tsv";
		String reziseriPutanja = "data_film_reziseri_pisci.tsv";

		Buffer buffer1 = new Buffer(cap);
		Buffer buffer2 = new Buffer(cap);
		Buffer buffer3 = new Buffer(cap);

		Barrier barrier = new Barrier(n);

		Map<String, String> trajanjeFilmovaMapa = ucitajHashMap(filmovi);

		long start = System.currentTimeMillis();
		System.out.println("Pocetak: " + start);

		Producer producer = new Producer(reziseriPutanja, buffer1);
		producer.setName("Producer");
		producer.start();

		Combiner combiner = new Combiner(buffer2, buffer3);
		combiner.setName("Combiner");
		combiner.start();

		for (int i = 0; i < n; i++) {
			Consumer consumer = new Consumer(buffer1, barrier, buffer2, filmovi, trajanjeFilmovaMapa);
			consumer.setName("Consumer" + i);
			consumer.start();
		}

		Printer printer = new Printer(buffer3);
		printer.setName("Printer");
		printer.start();

		try {
			printer.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		System.out.println("Kraj: " + end);
		long duration = end - start;
		System.out.println("Trajanje: " + (duration / 1000.) + " s");
	}
}