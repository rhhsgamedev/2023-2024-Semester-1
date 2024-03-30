import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;

public class Tile {
    private String mapName;
    private int index;
    private ImageElement element;
    public static final int TILE_SIZE = 60;
    private Game sys;
    private Rectangle hitbox;
    private static int[][] NEIGHBOUR_OFFSETS = {{-1,0}, {-1,-1},{0,-1},{1,-1},{1,0},{0,0},{-1,1},{0,1}};

    Tile(Game sys, String mapName, ImageElement element, int index) {
        this.sys = sys;
        this.mapName = mapName;
        this.index = index;
        this.element = element;
        this.hitbox = new Rectangle(element.getElementX(), element.getElementY(), element.getElementWidth(), element.getElementHeight());
    }

    public static HashSet<Tile> tilesAround(int[] pos, Tile[][] tileMap) {
        HashSet<Tile> adjacentTiles = new HashSet<>();
        int[] roundedPos = new int[2];
        roundedPos[0] = pos[0] / Tile.TILE_SIZE;
        roundedPos[1] = pos[1] / Tile.TILE_SIZE;
        for (int[] offset : NEIGHBOUR_OFFSETS) {
            if ((roundedPos[0] + offset[0] > -1) && (roundedPos[0] + offset[0] < tileMap[0].length)) {
                if ((roundedPos[1] + offset[1] > -1) && (roundedPos[1] + offset[1] < tileMap.length)) {
                    Tile t = tileMap[roundedPos[1] + offset[1]][roundedPos[0] + offset[0]];
                    if (t != null) {
                        adjacentTiles.add(t);
                    }
                }
            }
        }
        return adjacentTiles;
    }
    public int getIndex() {
        return index;
    }

    public ImageElement getElement() {
        return element;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
