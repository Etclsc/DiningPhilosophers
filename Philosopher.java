import java.util.concurrent.Semaphore;

public class Philosopher extends Thread {
	/**
	 * 哲学者の状態
	 * THINKING, HUNGRY, EATING のいずれか
	 */
	private static String state[] = new String[5];
	/**
	 * 共有のデータ
	 */
	private final Dining dining;
	/**
	 * 出力を見やすくするための待機時間
	 */
	private int waitTime;
	/**
	 * 自分の番号
	 */
	private int myID;
	/**
	 * 左隣の哲学者の番号
	 */
	private int leftID;
	/**
	 * 右隣の哲学者の番号
	 */
	private int rightID;
	/**
	 * 各哲学者のセマフォ
	 */
	private Semaphore semaphore;
	/**
	 * クリティカルセクション用のセマフォ
	 */
	private Semaphore mutex;

	Philosopher(int ID, Dining dng) {
		dining = dng;
		myID = ID;
		leftID = (ID+4)%5;
		rightID = (ID+1)%5;
		semaphore = new Semaphore(5);
		mutex = new Semaphore(1);
		waitTime = 1000;
	}

	public void run() {
		while(true) {
			try {
				thinking();
				tryEating();
				//tryEating_deadlock();	// デッドロック発生メソッド
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 哲学者の思考にあたるメソッド
	 * @throws InterruptedException
	 */
	public void thinking() throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + "「思考中…」");
		state[myID]  = new String("THINKING");
		sleep(waitTime);
	}
	/**
	 * 哲学者が食事をしようとするメソッド
	 * @throws InterruptedException
	 */
	public void tryEating() throws InterruptedException {
		System.out.println(Thread.currentThread().getName()+"「腹が減りました」");
		take2Forks();
		put2Forks();
	}
	/**
	 * 2本のフォークを同時に取ろうとするメソッド
	 * @throws InterruptedException
	 */
	public void take2Forks() throws InterruptedException {
		/* ------- クリティカルセクション開始 ------- */
		mutex.tryAcquire();
		state[myID] = new String("HUNGRY");
		isTakeable(myID);
		mutex.release();
		/* ------- クリティカルセクション終了 ------- */
		semaphore.tryAcquire(myID);
	}
	/**
	 * 2本のフォークを同時に置こうとするメソッド
	 * @throws InterruptedException
	 */
	public void put2Forks() throws InterruptedException {
		/* ------- クリティカルセクション開始 ------- */
		mutex.tryAcquire();
		state[myID] = new String("THINKING");
		isTakeable(leftID);
		isTakeable(rightID);
		mutex.release();
		/* ------- クリティカルセクション終了 ------- */
	}
	/**
	 * 左右のフォークを取る事が出来るか確認するメソッド
	 * @param ID 対象哲学者の番号
	 * @throws InterruptedException
	 */
	public void isTakeable(int ID) throws InterruptedException {
		int lp = (ID+4)%5;
		int rp = (ID+1)%5;
		if(state[ID].equals("HUNGRY") &&
				!(state[lp].equals("EATING")) && !(state[rp].equals("EATING")) ) {
			state[ID] = new String("EATING");
			System.out.println(Thread.currentThread().getName()+"「おいしい！！」");
			Thread.sleep(waitTime);
			semaphore.release(ID);
		}
	}
	/**
	 * デッドロックが発生する、哲学者の食事メソッド
	 * @throws InterruptedException
	 */
	public void tryEating_deadlock() throws InterruptedException {
		dining.forks[leftID].taking();
		System.out.println(Thread.currentThread().getName() + "「左手:"+leftID + "番フォーク」");

		dining.forks[rightID].taking();
		System.out.println(Thread.currentThread().getName() + "「右手:"+rightID + "番フォーク」");

		System.out.println(Thread.currentThread().getName() + "「おいしい！！」");
		Thread.sleep(waitTime);
		dining.forks[leftID].put();
		dining.forks[rightID].put();;
	}
}
