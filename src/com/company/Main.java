package com.company;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main extends Application {
    public final int DIM_GRID = 3;
    public final double SPACING = 10.0;
    public final double BTNS_HEIGHT = 10;
    public final double BTNS_LENGTH = 90;

    public final int INITIAL_POINTS = 2000;
    public final int TENTATIVI = 4;
    public final int PENALTY_MISCHIA = 100;
    int punteggio = INITIAL_POINTS;
    int tentativi = TENTATIVI;

    List<String> archivioParole = new ArrayList<>();
    List<Casella> caselle;

    Text txtPunteggio = new Text("Punteggio: ");
    Text txtTentativi = new Text("Tentativi: ");
    Text txtTrova = new Text("Trova:");
    Button btnInserisci = new Button("Inserisci");
    Button btnPassa = new Button("Passa");
    Button btnMischia = new Button("Mischia");
    Button btnPulisci = new Button("Pulisci");
    Text txtTrovate = new Text("Trovate: ");

    GridPane griglia = new GridPane();
    static String parolaAttuale;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primarystage) throws Exception {
        initArchivioParole();
        initCaselle();
        updateGriglia();
        update();
        Scene scene = new Scene(root());

        btnPassa.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                archivioParole.remove(0);
                tentativi--;
                unselectAll();
                initCaselle();
                updateGriglia();
                update();

            }
        });
        btnPulisci.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                unselectAll();
            }
        });
        btnMischia.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Collections.shuffle(caselle);
                punteggio -= PENALTY_MISCHIA;
                updateGriglia();
                unselectAll();
                update();
            }
        });
        btnInserisci.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(checkValida()){
                    txtTrovate.setText(txtTrovate.getText() + "\n" + parolaAttuale);
                    tentativi++;
                    btnPassa.fire();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("INSERIMENTO NON VALIDO");
                    alert.setContentText("Parola errata oppure con lettere non contigue");
                    alert.showAndWait();
                    punteggio -= 100;
                    update();
                }
                update();
            }
        });
        scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                for (Casella c : caselle) {
                    if (keyEvent.getCharacter().toUpperCase().equals(String.valueOf(c.lettera)) && !keyEvent.getCharacter().equalsIgnoreCase("X")) {
                        if (hasDoppioni(c.lettera)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("La lettera " + c.lettera + " è presente più di una volta!");
                            alert.setTitle("ERRORE");
                            alert.showAndWait();
                            break;
                        } else {
                            if (!c.isAlreadySelected()) {
                                c.selected();
                            } else {
                                c.unselected();
                            }
                        }
                    }
                }
            }
        });

        primarystage.setTitle("Cerca la parola!");
        primarystage.setScene(scene);
        primarystage.show();
    }

    void update(){
        txtTentativi.setText("Tentativi: " + tentativi);
        txtPunteggio.setText("Punteggio: " + punteggio);

        if(tentativi == 0 || punteggio == 0){
            txtTentativi.setText(txtTentativi.getText() + " - GAMEOVER");
            txtTrova.setFill(Color.GRAY);
            if(tentativi == 0) txtTentativi.setFill(Color.RED);
            else txtPunteggio.setFill(Color.RED);
            btnPassa.setDisable(true);
            btnPulisci.setDisable(true);
            btnMischia.setDisable(true);
            btnInserisci.setDisable(true);
        }

    }

    void initCaselle(){
        caselle = new ArrayList<>();

        Collections.shuffle(archivioParole);
        parolaAttuale = archivioParole.get(0);
        System.out.println("Parola attuale: " + parolaAttuale);

        if(tentativi > 0){
            txtTrova.setText("Trova: " + parolaAttuale);
        }

        caselle.add(new Fissa());
        caselle.add(new Fissa());

        char [] caratteri = parolaAttuale.toCharArray();
        Random rand = new Random();
        int pos = rand.nextInt(caratteri.length);
        caselle.add(new Matta(caratteri, pos));
        pos = rand.nextInt(caratteri.length);
        caselle.add(new Matta(caratteri, pos));

        pos = rand.nextInt(caratteri.length);
        caselle.add(new Scorrimento(caratteri, pos));
        pos = rand.nextInt(caratteri.length);
        caselle.add(new Scorrimento(caratteri, pos));
        pos = rand.nextInt(caratteri.length);
        caselle.add(new Scorrimento(caratteri, pos));
        pos = rand.nextInt(caratteri.length);
        caselle.add(new Scorrimento(caratteri, pos));
        pos = rand.nextInt(caratteri.length);
        caselle.add(new Scorrimento(caratteri, pos));
        Collections.shuffle(caselle);

        for (Casella c : caselle){
            if(! (c instanceof Fissa)){
                c.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        punteggio -= c.getPenalty();
                        c.changeLetter(caratteri);
                        update();
                    }
                });
            }
            c.unselected();
            System.out.print(c.lettera + " ");
        }
        System.out.println();


    }
    void initArchivioParole(){
        archivioParole.add("ARGENTO");
        archivioParole.add("BIOLCHE");
        archivioParole.add("CINTURA");
        archivioParole.add("DEFAULT");
        archivioParole.add("DORMIVA");
        archivioParole.add("FALCONI");
        archivioParole.add("IMPLORA");
        archivioParole.add("PERDANO");
    }
    void updateGriglia(){
        if(tentativi != 0){
            for(Casella c : caselle){
                griglia.getChildren().remove(c);
            }
            int c = 0;
            for(int row = 0; row<DIM_GRID; row++){
                for (int col = 0; col < DIM_GRID; col++){
                    griglia.add(caselle.get(c++), col, row);
                }
            }
        }
    }

    GridPane buttons(){
        btnInserisci.setMinSize(BTNS_LENGTH, BTNS_HEIGHT);
        btnMischia.setMinSize(BTNS_LENGTH, BTNS_HEIGHT);
        btnPassa.setMinSize(BTNS_LENGTH, BTNS_HEIGHT);
        btnPulisci.setMinSize(BTNS_LENGTH, BTNS_HEIGHT);

        GridPane buttons = new GridPane();
        buttons.add(btnInserisci,0,0);
        buttons.add(btnPassa,1,0);
        buttons.add(btnMischia,0,1);
        buttons.add(btnPulisci,1,1);
        buttons.setHgap(SPACING);
        buttons.setVgap(SPACING);
        return buttons;
    }
    HBox root(){
        VBox pers = new VBox(txtPunteggio,txtTentativi);
        VBox rightColumn = new VBox(pers,txtTrova,buttons(),txtTrovate);
        rightColumn.setSpacing(SPACING);
        HBox root = new HBox(griglia,rightColumn);
        root.setSpacing(SPACING);
        root.setPadding(new Insets(SPACING));
        return root;
    }
    boolean hasDoppioni(char c){
        int counter = 0;
        for (Casella casella : caselle) {
            if (casella.lettera == c) {
                counter++;
            }
        }
        return counter > 1;
    }
    void unselectAll(){
        for(Casella c : caselle){
            c.unselected();
        }
    }
    boolean isPresente(char l){
        boolean presente = false;
        for (Casella c : caselle) {
            if (c.lettera == l) {
                presente = true;
            }
        }
        return presente;
    }
    boolean checkValida(){
        char[] parola = parolaAttuale.toCharArray();
        for (int indexLetter = 0; indexLetter < parola.length; indexLetter++){
            char lettera = parola[indexLetter];
            for(int indexCas = 0; indexCas < caselle.size(); indexCas++){
                if(!isPresente(lettera)) return false;
            }
        }
        return true;
    }
}
