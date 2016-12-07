/**
 * mainメソッドを含むクラス
 */
public class Main {
	public static void main(String[] args) {
		Dining dining = new Dining();

		Philosopher pls0 = new Philosopher(0, dining);
		Philosopher pls1 = new Philosopher(1, dining);
		Philosopher pls2 = new Philosopher(2, dining);
		Philosopher pls3 = new Philosopher(3, dining);
		Philosopher pls4 = new Philosopher(4, dining);

		pls0.start();
		pls1.start();
		pls2.start();
		pls3.start();
		pls4.start();
	}
}
