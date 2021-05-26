
package rs.ac.bg.etf.drs.Stream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.HashMap;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainStreamResenje {

	public static Map<String, Integer> trajanjeFilmova;

	public static class Reziseri {

		String reziseri;
		String idfilma;

		public Reziseri(String s) {

			try {
				String[] data = s.split("\t");

				reziseri = data[1];
				idfilma = data[0];

			} catch (Exception e) {
			}
		}
	}

	public static class ReziseriNiz {

		String idFilma;
		String[] reziseriOdvojeno;

		public ReziseriNiz(Reziseri r) {

			try {
				reziseriOdvojeno = r.reziseri.split(",");
				idFilma = r.idfilma;

			} catch (Exception e) {
			}
		}
	}

	public static class ReziserIdfilmaObj {

		String reziser;
		String idfilma;

	}

	public static ReziserIdfilmaObj[] VratiNizReziserIdfilmaObj(ReziseriNiz r) {
		// kreira niz objekata tipa [reziser,idfilma]
		int brojRezisera = r.reziseriOdvojeno.length;
		ReziserIdfilmaObj[] niz = new ReziserIdfilmaObj[brojRezisera];
		for (int i = 0; i < brojRezisera; i++) {
			niz[i] = new ReziserIdfilmaObj();
			niz[i].idfilma = r.idFilma;
			niz[i].reziser = r.reziseriOdvojeno[i];
		}
		return niz;
	}

	public static String VratiRezisere(Reziseri obj) {
		String niz = obj.reziseri;

		return niz;
	}

	public static class ReziserMinuti {
		String reziser;
		int minuti;

		public ReziserMinuti(String s) {

			try {
				reziser = s;
				minuti = 10;

			} catch (Exception e) {
			}
		}
	}

	public static class FRM {
		String idFilma;
		String reziser;
		int minuti;

		public FRM(ReziserIdfilmaObj obj) {

			try {
				idFilma = obj.idfilma;
				reziser = obj.reziser;
				minuti = trajanjeFilmova.getOrDefault(idFilma, 0);

			} catch (Exception e) {
			}
		}
	}

	public static void UcitajHashMapuTrajanja(String putanja) {
		trajanjeFilmova = new HashMap<String, Integer>();
		// ucitaj drugi fajl i ubaci u memoriju tabelu <IDFILMA, TRAJANJE FILMA>
		// Ostavili smo minuti string posto ne radimo manipulaciju sa vremenom vec samo
		// prebacujemo dalje.
		// 6M * 10BAJTOVA+4BAJTA = 6M * 14 = 80MB tabela u memoriji
		{
			try (BufferedReader reader = new BufferedReader(new FileReader(putanja));) {
				String lineFilmSaMinutima = reader.readLine(); // linija sa zaglavljem
				int i = 0;
				while ((lineFilmSaMinutima = reader.readLine()) != null) {
					i++;
					String[] elementiNiza = lineFilmSaMinutima.split("\t");
					if (!("\\N".equals(elementiNiza[7]))) {
						String idFilmaIzTabeleMinuti = elementiNiza[0];// id mi treba //
						// tconst titleType primaryTitle originalTitle isAdult startYear endYear
						// runtimeMinutes genres
						// tt0000001 short Carmencita Carmencita 0 1894 \N 1 Documentary,Short
						// tt0000002 short Le clown et ses chiens Le clown et ses chiens 0 1892 \N 5
						// Animation,Short
						//
						trajanjeFilmova.put(idFilmaIzTabeleMinuti, new Integer(elementiNiza[7]));

					}
				}
			} catch (Exception e) {
			}
		}

	}

	public static void main(String[] args) {

		String filmovi = "data_filmovi.tsv";
		String reziseriPutanja = "data_film_reziseri_pisci.tsv";

		// ucitaj hashmapu u polje trajanjeFilmova (polje je dostupno iz svih static
		// metoda)
		UcitajHashMapuTrajanja(filmovi);

		long start = System.currentTimeMillis();
		System.out.println("Pocetak: " + start);

		try (Stream<String> streamRedoviFajla = Files.lines(Paths.get(reziseriPutanja), StandardCharsets.ISO_8859_1)) {

			Stream<Reziseri> streamReziseriObj = streamRedoviFajla.map(s -> new Reziseri(s));

			Stream<ReziseriNiz> streamNizStringovaReziseri = streamReziseriObj.map(s -> new ReziseriNiz(s));

			Stream<ReziserIdfilmaObj[]> streamReziserIdfilmaObjNiz = streamNizStringovaReziseri
					.map(s -> VratiNizReziserIdfilmaObj(s));

			Stream<ReziserIdfilmaObj> streamFlattenReziseri = streamReziserIdfilmaObjNiz.flatMap(obj -> Stream.of(obj));
			// streamFlattenReziseri.limit(50).forEach(s-> System.out.println(s.idfilma + "
			// " + s.reziser));

			Stream<FRM> streamFilmReziserMinuti = streamFlattenReziseri.map(s -> new FRM(s)).filter(s -> s.minuti > 0);
			// streamFilmReziserMinuti.limit(50).forEach(s-> System.out.println(s.idFilma +
			// " " + s.reziser + " " + s.minuti));

			streamFilmReziserMinuti
					.collect(Collectors.groupingBy(rm -> rm.reziser, Collectors.summingInt(rm -> rm.minuti))).entrySet()
					.stream().sorted(Map.Entry.comparingByValue()).filter(s->
					{if(s.getValue()>2) //filtriraj sve min vece od 2
					{return true;}
					else{return false;}						
					} )
					.forEach(s -> System.out.println(s.getKey() + "\t" + s.getValue()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();
		System.out.println("Kraj: " + end);
		long duration = end - start;
		System.out.println("Trajanje: " + (duration / 1000.) + " s");
	}
}