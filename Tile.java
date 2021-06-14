package sample;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static sample.Constants.*;

public class Tile {
    StackPane tile = new StackPane();
    Rectangle rect = new Rectangle();
    Text value = new Text("");
    Color free_tile_color = free_colors[0];

    Tile() {
        rect.setWidth((int)(WIDTH/(N*N)));
        rect.setHeight((int)(CANVAS_HEIGHT/(N*N)));
        rect.setFill(Constants.uninitialized_color);
        tile.getChildren().addAll(rect, value);
    }
    public boolean isFixed() { if (rect.getFill() == fixed_color) { return true; } return false; }
    public void setFreeColor(Color free_tile_color) { this.free_tile_color = free_tile_color; }
}
