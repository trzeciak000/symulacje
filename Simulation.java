public class Simulation {
	private static Object lock1 = new Object();
	private static Object lock2 = new Object();
	private static int x = 0;
	private static int res = 0;
	private static int res2 = 0;
	
	public static void main(String args[]) throws InterruptedException {
		if (args.length != 1) {
			usage();
		}
		try {
			int option = Integer.parseInt(args[0]);
			if(option == 1) {
				Locker1 Locker1 = new Locker1();
				Locker2 Locker2 = new Locker2();
				int i =0;
				Locker1.start();
				Locker2.start();
				while(x == 0) {
					Thread.sleep(3000);
					i++;
					if(res != 0 || res2 !=0) {
						System.out.println("Watki wykonaly swoje zadanie.");
						System.exit(0);
					} else {
						System.out.println("Watki nie moga dostac sie do zasobow.");
					}
					if ( i == 10) x = 1;
				}
				if(res != 0 || res2 != 0) {
					System.out.println("Koniec czasu. Watki wykonaly swoje zadanie");
				} else {
					System.out.println("Koniec czasu. Watki nie wykonaly swego zadania.");
				}
				System.exit(0);
			} else if(option == 2) {
				Starve1 Starve1 = new Starve1();
				Starve2 Starve2 = new Starve2();
				Starve1.start();
				Starve2.start();
				int i = 0;
				while (i <7) {
					Thread.sleep(2000);
					i++;
				}
				if(res == 0) {
					System.out.println("Koniec czasu. Watek Starve2 nie uzyskal dostepu do zasobow.");
				} else {
					System.out.println("Koniec czasu. Watek starve2 uzyskal dostep do zasobow.");
				}
				System.exit(0);
			} else if (option == 3) {
				Sender sender = new Sender();
				Receiver receiver = new Receiver();
				
				Thread t1 = new Thread(new Runnable() {
					public void run() {
						try {
							sender.send(receiver);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
				Thread t2 = new Thread(new Runnable() {
					public void run() {
						try {
							receiver.receive(sender);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
				t1.start();
				t2.start();
				int i = 0;
				while(i < 8) {
					i++;
					Thread.sleep(2000);
				}
				System.out.println("Koniec czasu");
				System.exit(0);
			} else {
				usage();
			}
		} catch (NumberFormatException e) {
			usage();
		}
		
	}
	
	
	public static class Locker1 extends Thread{
		public void run() {
			System.out.println("watek 1 rozpoczal dzialanie: " + res);
			synchronized(lock1) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized(lock2) {
					res = 1;
					System.out.println("watek 1 zakonczyl dzialanie: " + res);
				}
			}
		}
	}
	
	public static class Locker2 extends Thread{
		public void run() {
			System.out.println("watek 2 rozpoczal dzialanie: " + res);
			synchronized(lock2) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized(lock1) {
					res2 = 1;
					System.out.println("watek 2 zakonczyl dzialanie: " + res);
				}
			}
		}
	}
	
	public static class Starve1 extends Thread {
		public void run() {
			System.out.println("Watek Starve1 rozpoczal prace.");
			synchronized(lock1) {
				while(true) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Watek Starve1 pracuje.");
				}
			}
		}
	}
	
	public static class Starve2 extends Thread {
		public void run() {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Watek Starve2 rozpoczal prace.");
			synchronized(lock1) {
				res = 1;
				System.out.println("Watek Starve2 zakonczyl prace");
			}
		}
	}
	
	public static class Sender {
		private boolean sended = false;
		
		public void send(Receiver receiver) throws InterruptedException {
			while(!receiver.isReceived()) {
				System.out.println("Sender: Czekam z wysylka przesylki a¿ Receiver zaplaci pieniadze.");
				Thread.sleep(1000);
			}
			System.out.println("Sender: Wyslano.");
			sended = true;
		}
		
		public boolean isSend() {
			return sended;
		}
	}
	
	public static class Receiver{
		private boolean received = false;
		
		public void receive(Sender sender) throws InterruptedException {
			while(!sender.isSend()) {
				System.out.println("Receiver: Czekam z zaplata az Sender wysle przesylke.");
				Thread.sleep(1000);
			}
			System.out.println("Receiver: Zaplacono.");
			received = true;
		}
		
		public boolean isReceived() {
			return received;
		}
	}
	
	public static void usage() {
		System.out.println("U¿ycie: java Simulation <number>\n\n1  -  deadlock\n2  -  starvation\n3  -  livelock");
		System.exit(0);
	}
}