package rs.ac.bg.etf.drs.Sockets;

public class MainProducerCombinerPrinter {

	public static void main(String[] args) {
		String fileName = "data_film_reziseri_pisci40.txt";
		String host = "127.0.0.1";
		int port = 8080;

		BufferInterface buffer1 = new BufferNet("buff1", host, port);
		BufferInterface buffer2 = new BufferNet("buff2", host, port);
		BufferInterface buffer3 = new BufferNet("buff3", host, port);

		long start = System.currentTimeMillis();
		System.out.println("Pocetak: " + start);

		Producer producer = new Producer(fileName, buffer1);
		producer.setName("Producer");
		producer.start();

		Combiner combiner = new Combiner(buffer2, buffer3);
		combiner.setName("Combiner");
		combiner.start();

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
