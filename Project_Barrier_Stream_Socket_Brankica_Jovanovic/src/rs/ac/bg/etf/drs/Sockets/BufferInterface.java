package rs.ac.bg.etf.drs.Sockets;

public interface BufferInterface {

	/**
	 * Ubacivanje elementa u bafer. Ubacivanjem null elementa signalizira se kraj
	 * ubacivanja.
	 * 
	 * @param line element koji treba ubaciti u bafer ili null
	 */
	void put(String line);

	/**
	 * Dohvata element iz bafera, blokirajuci se dok element ne bude dostupan ili je
	 * kraj ubacivanju elemenata.
	 * 
	 * @return dok god nije signaliziran kraj niza elemenata koji su ubacivani u
	 *         bafer, vraca se ubacen element; u suprotnom, vraca se null
	 */
	String get();

}
