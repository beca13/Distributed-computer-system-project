package rs.ac.bg.etf.drs.Sockets;

import java.util.concurrent.Semaphore;

public class Barrier implements BarrierInterface {
	int n;
	int counter = 0; // za pracenje broja niti koje su pristigle na barijeru

	Semaphore sem;

	public Barrier(int n) {
		this.n = n;
		sem = new Semaphore(0);

	}

	// opcija sa semaforom
	@Override
	public void sync() {

		counter++;

		if (counter < n) {
			sem.acquireUninterruptibly();
			sem.release();
		} else {
			sem.release();
		}
	}

	// opcija sa wait and notifyAll
	public synchronized void syncWithPrimitives() {
		counter++;

		if (counter < n) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			}
		} else {
			notifyAll();
			counter = 0;
		}
	}

}
