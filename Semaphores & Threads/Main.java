import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.Random;

class BankSystem extends Thread{
    int moneyAmount;
    Semaphore semaphore;
    Random rand = new Random();

    public BankSystem(int moneyAmount, Semaphore semaphore){
        this.moneyAmount = moneyAmount;
        this.semaphore = semaphore;
    }

    public void deposit(int money){
        moneyAmount += money;
        System.out.println(Thread.currentThread().getName() + " deposited " + money + " dolars. Current Amount: " + moneyAmount + ".");
    }

    public void withdraw(int money){
        if(moneyAmount > 0){
            moneyAmount -= money;
            System.out.println(Thread.currentThread().getName() + " withdrew " + money + " dolars. Current Amount: " + moneyAmount + ".");
        }
        else{
            System.out.println("There's nothing to withdraw. No cash...");
        }
    }

    public void run(){
        try{
            semaphore.acquire();
            deposit(100);
            Thread.sleep(rand.nextInt(501));
            semaphore.release();

            semaphore.acquire();
            withdraw(100);
            Thread.sleep(rand.nextInt(501));
            semaphore.release();

        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args){
        Semaphore semaphore = new Semaphore(1, true);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        BankSystem sharedAccount = new BankSystem(100, semaphore);

        for(int i = 0; i < 4; i++){
            executor.submit(sharedAccount);
        }

        executor.shutdown();

        try{
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
