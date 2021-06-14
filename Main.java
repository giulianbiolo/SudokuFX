package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import static sample.Constants.*;

class Constants {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 525;
    public static final int CANVAS_HEIGHT = HEIGHT - 25;
    public static final int N = 3;
    public static final Color uninitialized_color = Color.WHITE;
    public static final Color fixed_color = Color.GRAY;
    public static final Color[] free_colors = new Color[]{
            Color.LIGHTYELLOW,
            Color.LIGHTPINK,
            Color.LIGHTSTEELBLUE,
            Color.LIGHTGREEN,
            Color.LIGHTBLUE,
            Color.LIGHTSALMON,
            Color.LAVENDER,
            Color.BISQUE,
            Color.HONEYDEW
    };
    public static ChoiceBox choiceBox;
}

public class Main extends Application {
    public int fixed_counter = 0;
    Text celle_libere;
    Stage victoryStage;
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setFillWidth(true);
        root.setSpacing(0);
        root.setMinSize(WIDTH, HEIGHT);
        root.setMaxSize(WIDTH, HEIGHT);
        root.setAlignment(Pos.CENTER);

        GridPane canvas = new GridPane();
        canvas.setHgap(2);
        canvas.setVgap(2);
        canvas.setGridLinesVisible(true);
        canvas.setMinSize(WIDTH, CANVAS_HEIGHT);
        canvas.setMaxSize(WIDTH, CANVAS_HEIGHT);
        OuterTile[] outerTiles = new OuterTile[N*N];
        for (int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                outerTiles[i*N+j] = new OuterTile();
                outerTiles[i*N+j].free_color = free_colors[i*N+j];
                canvas.add(outerTiles[i*N+j].grid, j ,i);
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int row = 0; row < N; row++) {
                    for (int col = 0; col < N; col++) {
                        int finalI = i;
                        int finalJ = j;
                        int finalRow = row;
                        int finalCol = col;
                        outerTiles[finalI*N+finalJ].tiles[finalRow*N+finalCol].tile.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                if(fixed_counter < N*N) {
                                    if (!checkRules(finalI, finalJ, finalCol, finalRow, outerTiles)) { return; }
                                    outerTiles[finalI *N+ finalJ].tiles[finalRow *N+ finalCol].rect.setFill(fixed_color);
                                    outerTiles[finalI *N+ finalJ].tiles[finalRow *N+ finalCol].value.setText(choiceBox.getValue().toString());
                                    fixed_counter++;
                                    if(fixed_counter == N*N) { uncoverTiles(outerTiles); }
                                }
                                else {
                                    if(outerTiles[finalI *N+ finalJ].tiles[finalRow *N+ finalCol].rect.getFill() == fixed_color) { return; }
                                    if(!outerTiles[finalI *N+ finalJ].tiles[finalRow *N+ finalCol].value.getText().equals("")) {
                                        outerTiles[finalI *N+ finalJ].tiles[finalRow *N+ finalCol].value.setText("");
                                        updateFreeCells(outerTiles);
                                        return;
                                    }
                                    if (!checkRules(finalI, finalJ, finalCol, finalRow, outerTiles)) { return; }
                                    outerTiles[finalI *N+ finalJ].tiles[finalRow *N+ finalCol].value.setText(choiceBox.getValue().toString());
                                    if (updateFreeCells(outerTiles) == 0) { victoryStage.show(); }
                                }
                            }
                        });
                    }
                }
            }
        }
        root.getChildren().add(canvas);

        HBox bottom = new HBox();
        choiceBox = new ChoiceBox();
        choiceBox.setValue(1);
        for(int i = 1; i <= N*N; i++) { choiceBox.getItems().add(i); }
        Text valore = new Text("Valore: ");
        celle_libere = new Text("Celle Libere: " + (N*N*N*N - N*N));
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(5);
        bottom.getChildren().addAll(valore, choiceBox, celle_libere);
        root.getChildren().add(bottom);

        primaryStage.setTitle("Sudoku - Giulian Biolo");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();

        StackPane victoryPane = new StackPane();
        Text victory = new Text("Complimenti, Hai Vinto!");
        victory.setTextAlignment(TextAlignment.CENTER);
        victoryPane.getChildren().add(victory);
        victoryStage = new Stage();
        victoryStage.setTitle("Vittoria!");
        victoryStage.setScene(new Scene(victoryPane, 150, 80));
        victoryStage.setResizable(false);
    }

    public void uncoverTiles(OuterTile[] outerTiles) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int row = 0; row < N; row++) {
                    for (int col = 0; col < N; col++) {
                        if(!outerTiles[i *N+ j].tiles[row *N+ col].isFixed())
                            outerTiles[i *N+ j].tiles[row *N+ col].rect.setFill(outerTiles[i*N+j].free_color);
                    }
                }
            }
        }
    }
    public boolean checkRules(int i, int j, int c, int r, OuterTile[] outerTiles) {
        for (int idx = 0; idx < N; idx++)
            for (int rows = 0; rows < N; rows++)
                if (outerTiles[idx * N + j].tiles[rows * N + c].value.getText().equals(choiceBox.getValue().toString())) return false;
        for (int idx = 0; idx < N; idx++)
            for (int cols = 0; cols < N; cols++)
                if (outerTiles[i * N + idx].tiles[r * N + cols].value.getText().equals(choiceBox.getValue().toString())) return false;
        for (int rows = 0; rows < N; rows++)
            for (int cols = 0; cols < N; cols++)
                if (outerTiles[i * N + j].tiles[rows * N + cols].value.getText().equals(choiceBox.getValue().toString())) return false;
        return true;
    }
    public int updateFreeCells(OuterTile[] outerTiles) {
        int sum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int row = 0; row < N; row++) {
                    for (int col = 0; col < N; col++) {
                        if (outerTiles[i*N+j].tiles[row*N+col].value.getText().equals("")) { sum++; }
                    }
                }
            }
        }
        celle_libere.setText("Celle Libere: " + sum);
        return sum;
    }
    public static void main(String[] args) { launch(args); }
}
