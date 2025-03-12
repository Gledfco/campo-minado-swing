package br.com.gledson.campominado.modelo;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador {

    public int getLinhas() {
        return linhas;
    }

    public int getColunas() {
        return colunas;
    }

    public int getMinas() {
        return minas;
    }

    private final int linhas;
    private final int colunas;
    private final int minas;
    private final List<Campo> campos = new ArrayList<>();
    private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();


    public Tabuleiro(int minas, int colunas, int linhas) {
        this.minas = minas;
        this.colunas = colunas;
        this.linhas = linhas;

        gerarCampos();
        associarOsVizinhos();
        sortearMinas();

    }

    public void paraCada(Consumer<Campo> funcao){
        campos.forEach(funcao);
    }

    public void registrarObservador (Consumer<ResultadoEvento> observador){
        observadores.add(observador);
    }

    private void notificarObservadores(boolean resultado){
        observadores.stream().forEach(observador -> observador.accept(new ResultadoEvento(resultado)));
    }

    public void  abrir( int linha, int coluna){
                campos.parallelStream().filter(c -> c.getLinha()==linha && c.getColuna() ==coluna).
                        findFirst().ifPresent(c -> c.abrir());
    }



    public void  alternarMarcacao( int linha, int coluna){
        campos.parallelStream().filter(c -> c.getLinha()==linha && c
                .getColuna() ==coluna).findFirst().ifPresent(c -> {boolean resultado = c.abrir();
        if (resultado && objetivoAlcancado()) {
            notificarObservadores(true); // Se o objetivo foi alcançado, notifica a vitória
        }
    });
}



    private void gerarCampos() {
        for (int linha = 0; linha < linhas; linha++){
            for (int coluna = 0; coluna < colunas; coluna++) {
                Campo campo = new Campo(linha,coluna);
                campo.registrarObservador(this);
                campos.add(campo);
            }
        }

    }


    private void associarOsVizinhos() {
        for(Campo c1 : campos ){
            for(Campo c2: campos){
                c1.adicionarVizinho(c2);
            }
        }
    }

    private void sortearMinas() {
        long minasArmadas = 0;
        Predicate<Campo> minado = c -> c.isMinado();

        do {
            int aleatorio =(int) (Math.random() * campos.size());
            campos.get(aleatorio).minar();
            minasArmadas = campos.stream().filter(minado).count();
        }while(minasArmadas<minas);

    }

    public boolean objetivoAlcancado(){
       return campos.stream().allMatch(c -> c.objetivoAlcancado());
    }

    public void reiniciarJogo(){
        campos.stream().forEach(c->c.reiniciar());
        sortearMinas();
    }

    @Override
    public void eventoOcorreu(Campo campo, CampoEvento evento) {
        if (evento == CampoEvento.EXPLODIR){
            mostrarMinas();
            System.out.println("Perdeu");
            notificarObservadores(false);
        } else if (objetivoAlcancado()) {
            notificarObservadores(true);
            System.out.println("Ganhou");
        }
    }

    private void mostrarMinas(){
        campos.stream().filter(c -> c.isMinado()).filter(c -> !c.isMarcado()).
                forEach(c -> c.setAberto(true));
    }

}
