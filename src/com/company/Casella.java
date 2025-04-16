package com.company;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public abstract class Casella extends StackPane {
    static final BorderWidths B_WIDTHS = new BorderWidths(5);
    static final Border B_SELECTED = new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null, B_WIDTHS));
    static final Border B_UNSELECTED = new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.NONE, null, B_WIDTHS));
    private final double DIM = 75.0;

    Rectangle base = new Rectangle(DIM, DIM);
    char lettera;
    Color colore;
    Text txtLettera = new Text();

    public Casella(char lettera, Color colore) {
        this.lettera = lettera;
        this.colore = colore;
        base.setFill(colore);
        txtLettera.setText(String.valueOf(lettera));
        this.getChildren().addAll(base, txtLettera);
    }
    abstract void changeLetter(char[] parola);
    abstract int getPenalty();

    void selected(){
        this.setBorder(B_SELECTED);
    }
    void unselected(){
        this.setBorder(B_UNSELECTED);
    }
    boolean isAlreadySelected(){
        return this.getBorder() == B_SELECTED;
    }
}
