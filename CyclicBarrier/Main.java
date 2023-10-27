import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class RussianMountainThread extends Thread{
    private int numRounds;

    public RussianMountainThread(){
        numRounds = 0;
    }

    public void mountainCarLauch(){
        numRounds += 1;
        System.out.println("\nTurn " + numRounds + ":\n");
        System.out.println(" - A car is full! Beginning another round in the mountain.");

        try{
            Thread.sleep(5000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println(" - A car has just come back! ");
    }

    public void run(){
        mountainCarLauch();
    }
}

class ClientThread extends Thread{
    private CyclicBarrier barrier;
    private int id;


    public ClientThread(CyclicBarrier barrier, int id) {
        this.barrier = barrier; 
        this.id = id;
    }

    public void run(){
        System.out.println(" - " + Thread.currentThread().getName() + ": Client " + id + " just entered the Russian Mountain's queue.");

        try{
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {}

        System.out.println(" - " + Thread.currentThread().getName() + ": Client " + id + " had a lot of fun!");
    }
}

public class Main{
    public static void main(String[] args){
        int numCars = 2;
        int numClient = 10;

        CyclicBarrier barrier = new CyclicBarrier(numCars, new RussianMountainThread());
        ExecutorService executor = Executors.newFixedThreadPool(numCars);

        System.out.println("---------------------------- Russian Mountain 2000 ----------------------------\n");
        for (int i = 0; i < numClient; i++){
            executor.submit(new ClientThread(barrier, i));
        }

        executor.shutdown();
    }
}