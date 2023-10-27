import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Main {
    static Semaphore hashis[] = new Semaphore[5];

    static {
        for(int i = 0; i < 5; i++){
            hashis[i] = new Semaphore(1, true);
        }
    }

    public static void acquire(int n1, int n2) throws InterruptedException {
		while (true) {
			boolean gotFirstLock = false;
			boolean gotSecondLock = false;
			try {
				gotFirstLock = pegar_hashis(n1);
				gotSecondLock = pegar_hashis(n2);
			} 
			finally {
				if (gotFirstLock && gotSecondLock) return;
				else if (gotFirstLock) liberar_hashis(n1);
				else if (gotSecondLock) liberar_hashis(n2);
			}
		}
	}
	
	public static void release(int n1, int n2) {
		liberar_hashis(n1);
        liberar_hashis(n2);
	}

    // Funções Compartilhadas:
    static boolean pegar_hashis(int num){
        System.out.println("Hashi " + num + " foi pego pelo " + Thread.currentThread().getName() + ".");
        return hashis[num].tryAcquire();
    }

    static void liberar_hashis(int num){ 
        System.out.println("Hashi " + num + " foi liberado pelo " + Thread.currentThread().getName() + ".");
        hashis[num].release();
    } 

    // Classe Thread:
    static class Filosofos extends Thread{
        private int id;
        private int counter;
        private Random rand = new Random();

        public Filosofos(int id, int counter){
            this.id = id;
            this.counter = 0;
        }

        public void pense(){
            System.out.println(Thread.currentThread().getName() + " está pensando...");
            try {
                Thread.sleep(rand.nextInt(1001 - 500) + 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void coma(){
            System.out.println(Thread.currentThread().getName() + " começou a comer...");
            try {
                Thread.sleep(rand.nextInt(1001 - 500) + 500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void filosofo(){
            while(this.counter < 5){
                if(id % 2 == 0){
                    pense();
                    try{
                        acquire(id, (id+1)%5);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    coma();
                    release((id+1)%5, id);
                }
                else{
                    pense();
                    try{
                        acquire((id+1)%5, id);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    coma();
                    release((id+1)%5, id);
                }

                this.counter++;
            }
        }

        public void run() {
			filosofo();
		}
    }

    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(5);
        System.out.println("Preparem-se todos! O jantar vai começar!");
        for(int i = 0; i < 5; i++){
            executor.submit(new Filosofos(i, 0));
        }

        executor.shutdown();

        try{
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}

