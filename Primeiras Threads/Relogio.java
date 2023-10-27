public class Relogio implements Runnable{
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
