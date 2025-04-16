package com.company;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Fissa extends Casella{
    public static char lettera = 'X';

    public Fissa() {
        super(lettera, Color.WHEAT);
    }
    public void changeLetter(char[] parola){
        System.out.println("Fissa");
    }

    @Override
    int getPenalty() {
        return 0;
    }
}
