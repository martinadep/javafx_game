package com.company;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Random;

public class Matta extends Casella{
    public static final int PENALITA = 25;

    public Matta(char[] parola, int pos) {
        super(parola[pos], Color.RED);

    }
    public void changeLetter(char[] parola){
        Random rand = new Random();
        int pos = rand.nextInt(parola.length);
        while (this.lettera == parola[pos]){
            pos = rand.nextInt(parola.length);
        }
        this.lettera = parola[pos];
        txtLettera.setText(String.valueOf(lettera));
    }

    @Override
    int getPenalty() {
        return PENALITA;
    }
}
