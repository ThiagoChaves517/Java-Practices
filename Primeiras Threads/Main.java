import java.util.Scanner;

class Relogio implements Runnable{
//Private:
    //Atributos da Classe:
    private ContadorTempo Contador;
    private int TickCases;
    private int InicialTick;

    //Construtor da Classe:
    Relogio(ContadorTempo c, int t, int i){
        Contador = c;
        TickCases = t;
        InicialTick = i;
    }

//Public:
    //Métodos da Classe:
    static Relogio criarRelogio(ContadorTempo cont, int tickCases, int inicialTick){
        cont.setTick(inicialTick);
        return new Relogio(cont, tickCases, inicialTick);
    }

    public void run(){
        for(int i = InicialTick; i < TickCases; i++){
            System.out.println(Thread.currentThread().getName() + " - Tick: " + Contador.getTick());
            Contador.nextTick();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Main{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        int initialTick = 0;
        int tickCases = 0;

        System.out.println("----------------------- Bem-vindo ao Relogio Multithreading! -----------------------");
        System.out.println("Deseja escolher o tick inicial?");
        System.out.println(" -> 1: Sim / 2: Nao");
        int op = input.nextInt();
        if(op == 1 || op == 1){
            System.out.println("Digite o tick inicial: ");
            initialTick = input.nextInt();
        }
        System.out.println("Digite o número de ticks que quer ver: ");
         tickCases = input.nextInt();

        Thread r1 = new Thread(Relogio.criarRelogio(new ContadorTempo(), tickCases, initialTick));
        Thread r2 = new Thread(Relogio.criarRelogio(new ContadorTempo(), tickCases, initialTick));

        r1.start();
        r2.start();
    }
}








