package br.com.gledson.campominado.modelo;

@FunctionalInterface
public interface CampoObservador {

    public void eventoOcorreu(Campo campo,CampoEvento evento );
    //qual foi o campo e o tipo de evento que aconteceu


}
