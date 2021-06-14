package sample;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import static sample.Constants.*;

public class OuterTile {
    public Color free_color;
    Tile[] tiles = new Tile[N*N];
    GridPane grid = new GridPane();
    OuterTile() {
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setMinSize((int)(WIDTH/N), (int)(CANVAS_HEIGHT/N));
        grid.setMaxSize((int)(WIDTH/N), (int)(CANVAS_HEIGHT/N));
        for (int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                tiles[i*N+j] = new Tile();
                tiles[i*N+j].setFreeColor(free_color);
                int finalI = i;
                int finalJ = j;
                grid.add(tiles[i*N+j].tile, j, i);
            }
        }
    }
}
