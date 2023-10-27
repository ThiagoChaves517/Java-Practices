import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class FumanteProcessor implements Runnable{
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();
    private final Object lock3 = new Object();
    private CountDownLatch latch;

//Constructor:
    FumanteProcessor(CountDownLatch latch){
        this.latch = latch;
    }

//Methods:
    void trazerTabaco(){
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        synchronized(lock1){
            System.out.println("     - Fumante " + Thread.currentThread().getName() + " está trazendo o tabaco.");
        }
    }

    void trazerPapel(){
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        synchronized(lock2){
            System.out.println("     - Fumante " + Thread.currentThread().getName() + " está trazendo o papel.");
        }
    }

    void trazerFósforo(){
        try{
            Thread.sleep(1000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        synchronized(lock3){
            System.out.println("     - Fumante " + Thread.currentThread().getName() + " trouxe fósforo, acendeu e fumou um cigarro.");
        }
    }

    public void run(){
        //aumentarTurnos();

        trazerTabaco();
        trazerPapel();
        trazerFósforo();

        latch.countDown();
    }
}

public class Main {
    public static void main(String[] args){
        System.out.println(" --------------------------------------- Simulador de Fumantes 2000 ---------------------------------------");
        System.out.println(" - Iniciando o sistema...");
        CountDownLatch latch = new CountDownLatch(10); //CountDownLatch é incializado para terminar com 10 processos.
        ExecutorService executor = Executors.newFixedThreadPool(3); //Apenas três threads são utilizadas, para que assim só hajam três fumantes.                                       

        for(int i = 0; i < 10; i++){ //Os três fumantes na Thread Pool são submetidos ao processo do simulador umas 10 vezes, assim como pede a documentação do programa.
            executor.submit(new FumanteProcessor(latch));
        }

        executor.shutdown();

        try{
            latch.await(); //Uma barreira é ativada para que o código só passe daqui quando CountDownLatch descer a zero. 
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println(" - 10 cigarros já foram montados, acesos e utilizados.");
        System.out.println(" - Encerrando...");
    }
}










