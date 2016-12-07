/**
 * フォークが置かれているテーブルに相当するクラス
 */
public class Dining {
	private final int nForks = 5;
	Fork forks[];
	Dining() {
		forks = new Fork[nForks];
		for(int i = 0; i < nForks; i++) {
			forks[i] = new Fork();
		}
	}

	/**
	 * フォーク1本に相当するクラス
	 */
	public class Fork {
		/**
		 * フォークが哲学者に取られている状態かどうか
		 * @see tryEating_deadlock
		 */
		private boolean holded;
		Fork() {
			holded = false;
		}

		/**
		 * フォークが哲学者に取られるメソッド
		 * @throws InterruptedException
		 */
		public synchronized void taking() throws InterruptedException {
			while(holded) {
				wait();
			}
			holded = true;
		}
		/**
		 * フォークが哲学者に置かれるメソッド
		 * @throws InterruptedException
		 */
		public synchronized void put() throws InterruptedException {
			holded = false;
			notifyAll();
		}
	}
}
