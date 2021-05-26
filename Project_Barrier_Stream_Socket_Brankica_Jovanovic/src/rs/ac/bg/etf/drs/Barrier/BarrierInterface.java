package rs.ac.bg.etf.drs.Barrier;

public interface BarrierInterface {

	/**
	 * Barijera osim sto sinhronizuje niti, tj. blokira prvih (n-1) dok ne dodje i
	 * n-ta, takodje nalazi koja od tih n niti ima film(ove) sa najvecim brojem
	 * rezisera.<br>
	 * To radi tako sto svaka nit javlja koji je njen maksimum, a barijera javlja na
	 * kraju koji je najveci od tih maksimuma (niti imaju svoje "lokalne maksimume",
	 * a barijera nalazi "globalni maksimum").
	 * 
	 * @param currentMax maksimalni broj (rezisera) koji je nit consumer-a nasla
	 * @return stvarno maksimalni broj (rezisera)
	 */
	public void sync();

}