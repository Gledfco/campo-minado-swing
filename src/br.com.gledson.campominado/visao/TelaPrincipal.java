package br.com.gledson.campominado.visao;

import br.com.gledson.campominado.modelo.Tabuleiro;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

        public TelaPrincipal(){
            Tabuleiro tabuleiro = new Tabuleiro(50,30,16);
            add(new PainelTabuleiro(tabuleiro));

            setTitle("Campo Minado");
            setSize(690,438);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setVisible(true);
}

    public static void main(String[] args) {
        new TelaPrincipal();
    }


}
