import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Deposito{
    private int qtdArmazenada;
    private int qtdMaxima;
    private boolean processando;

    Deposito(int qtdMaxima){
        this.qtdArmazenada = 1;
        this.qtdMaxima = qtdMaxima;
        this.processando = false;
    }

    public synchronized void armazenar(){ //Armazena uma caixa no depósito.
        while(processando == false){
            try{   
                wait();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        processando = false;

        if(qtdMaxima > qtdArmazenada){
            qtdArmazenada++;
            System.out.println("- " + Thread.currentThread().getName() + " armazenou com sucesso! Quantidade no depósito: " + qtdArmazenada + ".");
        }
        else{
            System.out.println("- " + Thread.currentThread().getName() + " não conseguiu armazenar! Quantidade no depósito: " + qtdArmazenada + ".");
        }

        notifyAll();
    }

    public synchronized void retirar(){ //Retira uma caixa do depósito.
        while(processando == true){
            try{   
                wait();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        processando = true;

        if(qtdArmazenada > 0){
            qtdArmazenada--;
            System.out.println("- " + Thread.currentThread().getName() + " retirou com sucesso! Quantidade no depósito: " + qtdArmazenada + ".");
        }
        else{
            System.out.println("- " + Thread.currentThread().getName() + " não conseguiu retirar! Quantidade no depósito: " + qtdArmazenada + ".");
        } 
        
         notifyAll();
    }

    public int caixasArmazenadas(){ //Retorna o numero dd caixas armazenadas no depósito.
        return qtdArmazenada;
    }
}


class Produtor extends Thread{
    private Deposito deposito;
    private int tempoEntreCaixas; //Milisegundos.

    Produtor(Deposito deposito, int tempoEntreCaixas){
        this.deposito = deposito;
        this.tempoEntreCaixas = tempoEntreCaixas;
    }

    public void run(){
        //System.out.println("- " + Thread.currentThread().getName() + " está armazenando uma caixa.");
        deposito.armazenar();

        try{
            Thread.sleep(tempoEntreCaixas);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}


class Consumidor extends Thread{
    private Deposito deposito;
    private int tempoEntreCaixas; //Milisegundos.

    Consumidor(Deposito deposito, int tempoEntreCaixas){
        this.deposito = deposito;
        this.tempoEntreCaixas = tempoEntreCaixas;
    }

    public void run(){
        //System.out.println("- " + Thread.currentThread().getName() + " está retirando uma caixa.");
        deposito.retirar();

        try{
            Thread.sleep(tempoEntreCaixas);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}


public class Main{
    public static void main (String[] args){
        ExecutorService executor = Executors.newFixedThreadPool(20);
        Deposito deposito = new Deposito(10);

        System.out.println();
        System.out.println("--------------------- Sistema de Armazenamento de Caixas 2000 ---------------------\n");
        System.out.println("Sistema em funcionamento!\n");

        for(int i = 0; i < 10; i++){
            executor.submit(new Produtor(deposito, 500));
            executor.submit(new Consumidor(deposito, 500));
        }

        executor.shutdown();

        try{
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println();
        System.out.println("Quantidade de Caixas = " + deposito.caixasArmazenadas() + ".");
        System.out.println("Trabalho finalizado! Desligando o sistema...\n");
    }
}