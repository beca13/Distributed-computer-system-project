package rs.ac.bg.etf.drs.Sockets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class MainConsumer {
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
		String filmovi = "data_filmovi40.txt";
		String host = "127.0.0.1";
		int n = 2;// 5
		int port = 8080;

		Map<String, String> trajanjeFilmovaMapa = ucitajHashMap(filmovi);

		BufferInterface buffer1 = new BufferNet("buff1", host, port);
		BufferInterface buffer2 = new BufferNet("buff2", host, port);

		BarrierInterface barrier = new BarrierNet(host, port);

		long start = System.currentTimeMillis();
		System.out.println("Pocetak: " + start);

		for (int i = 0; i < n; i++) {
			Consumer consumer = new Consumer(buffer1, barrier, buffer2, filmovi, trajanjeFilmovaMapa);
			consumer.setName("Consumer" + i);
			consumer.start();
		}

		long end = System.currentTimeMillis();
		System.out.println("Kraj: " + end);
		long duration = end - start;
		System.out.println("Trajanje: " + (duration / 1000.) + " s");
	}
}
