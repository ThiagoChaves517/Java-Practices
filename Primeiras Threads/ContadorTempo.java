public class ContadorTempo {
//Private:
    //Atributos da Classe:
    private int Tick;

//Public:
    //Construtor da Classe:
    public ContadorTempo(){
        Tick = 0;
    }
    public ContadorTempo(int t){
        Tick = t;
    }
    
    //Métodos da Classe:
    public void setTick(int tick){
        Tick = tick;
    }
    public int getTick(){
        return Tick;
    }

    public void nextTick(){
        Tick++;
    }
}
