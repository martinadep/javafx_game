package com.company;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Scorrimento extends Casella{
    public static final int PENALITA = 50;
    public int position;

    public Scorrimento(char[] parola, int pos) {
        super(parola[pos], Color.LIGHTBLUE);
        position = pos;

    }
    public void changeLetter(char[] parola){
        position++;
        if(position == parola.length) position=0;
        this.lettera = parola[position];
        txtLettera.setText(String.valueOf(lettera));
    }
    @Override
    int getPenalty() {
        return PENALITA;
    }
}
