import java.lang.Math;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Deposito{
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    //Atributos:
    private int NumeroDeItens;
    private int MaxItens;
    
    //Construtores:
    public Deposito(int maxItens){
        NumeroDeItens = 0;
        MaxItens = maxItens;
    }
    public Deposito(int numItens, int maxItens){
        NumeroDeItens = numItens;
        MaxItens = maxItens;
    }

    //Métodos:
    public void armazenar() throws InterruptedException{
        synchronized(lock1){
            if(NumeroDeItens < MaxItens){
                NumeroDeItens++;
                System.out.println("Caixa armazenada! - Estoque: " + NumeroDeItens + "\n\n");
            }
            else{
                System.out.println("Limite máximo alcancado... estoque cheio." + "\n\n");
            }
        }
        
    }
    public void retirar() throws InterruptedException{
        synchronized(lock2){
            if(NumeroDeItens > 0){
                NumeroDeItens--;
                System.out.println("Caixa retirada! - Estoque: " + NumeroDeItens + "\n\n");
            }
            else{
                System.out.println("Impossivel retirar caixa... estoque vazio." + "\n\n");
            }
        }
    }



}

class Produtor implements Runnable{
    //Atributos:
    private Deposito MeuDeposito;
    private int TempoDeProducao;
    //private final Object lock1 = new Object();

    //Construtores:
    public Produtor(Deposito deposito, int tempoDeProducao){
        MeuDeposito = deposito;
        TempoDeProducao = tempoDeProducao;
    }

    //Métodos:
    public int getTempoDeProducao(){
        return TempoDeProducao;
    }

    public void armazenarCaixa() throws InterruptedException{
        System.out.println(Thread.currentThread().getName() + " -> Tentando armazenar!");
        MeuDeposito.armazenar();
    }

    public void run(){
        int MAX = 1;
        for(int i = 0; i < MAX; i++){
            try{
                armazenarCaixa();
            } catch(InterruptedException e) {

            }
            
            try{
                Thread.sleep(TempoDeProducao);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumidor extends Thread{
    //Atributos:
    private Deposito MeuDeposito;
    private int TempoDeProducao;

    //Construtores:
    public Consumidor(Deposito deposito, int tempoDeProducao){
        MeuDeposito = deposito;
        TempoDeProducao = tempoDeProducao;
    }

    //Métodos:
    public int getTempoDeProducao(){
        return TempoDeProducao;
    }

    public synchronized void retirarCaixa() throws InterruptedException{
        System.out.println(getName() + " -> Tentando retirar!");
        synchronized(this){
            MeuDeposito.retirar();
        }
    }

    public void run() {
        int MAX = 1;
        for(int i = 0; i < MAX; i++){
            try{
                retirarCaixa();
            } catch(InterruptedException e) {

            }

            try{
                Thread.sleep(TempoDeProducao);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Main {
    public static void main(String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(5);
        Deposito dep = new Deposito(5);
        Thread prod1 = new Thread(new Produtor(dep, (int) (Math.random() * 201)));
        Consumidor cons1 = new Consumidor(dep, (int) (Math.random() * 201));

        for(int i = 0; i < 20; i++){
            executor.submit(prod1);
            executor.submit(cons1);
        }

        executor.shutdown();

        try{
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

}
