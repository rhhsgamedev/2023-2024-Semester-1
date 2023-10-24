import java.util.HashMap;

public class Map {
  
  Block[][] blocks;
  int xSize; int ySize;
  int scale;
  HashMap<Block, Integer> exits; HashMap<Integer, Block> revExits;
  int id;
  
  Map(int ySize, int xSize, int id) {
    this.xSize = xSize; this.ySize = ySize;
    blocks = new Block[ySize][xSize];
    this.id = id;
  }
  
  public void addBlock(Block b) {
    blocks[b.getY()][b.getX()] = b;
    if (b.getX() != 0) {
      b.setLeft(blocks[b.getY()][b.getX()-1]);
      if (blocks[b.getY()][b.getX()-1] != null)
        blocks[b.getY()][b.getX()-1].setRight(b);
    }
    if (b.getY() != 0) {
      b.setUp(blocks[b.getY()-1][b.getX()]);
      if (blocks[b.getY()-1][b.getX()] != null)
        blocks[b.getY()-1][b.getX()].setDown(b);
    }
  }
  
  public Map toNextMap(Player p, int k) {
    Map nextMap = MapReader.readMapFile(k);
    Block b = nextMap.getRevExits().get(this.id);
    p.setX(b.getX()); p.setY(b.getY());
    if (b.getLeft() != null)
      p.setX(p.getX()-1);
    else if (b.getRight()!= null)
      p.setX(p.getX()+1);
    else if (b.getUp() != null)
      p.setY(p.getY()-1);
    else if (b.getDown() != null)
      p.setY(p.getY()+1);
    return nextMap;
  }
  
  public String toString() {
    String string = "";
    for (Block[] row: blocks) {
      for (Block column: row) {
        if (column != null)
          string = string + " " + column.toString();
        else
          string = string + " x";
      }
      string = string + "\n";
    }
    return string;
  }
  //***********************************
  public int getXSize() {
    return this.xSize;
  }
  public int getYSize() {
    return this.ySize;
  }
  public void setScale(int s) {
    this.scale = s;
  }
  public int getScale() {
    return this.scale;
  }
  public Block getBlock(int x, int y) {
    return blocks[y][x];
  }
  public boolean hasBlock(int x, int y) {
    return blocks[y][x]!=null;
  }
  public void setExits(HashMap<Block, Integer> exits, HashMap<Integer, Block> revExits) {
    this.exits = exits; this.revExits = revExits;
  }
  public HashMap<Block, Integer> getExits() {
    return this.exits;
  }
  public HashMap<Integer, Block> getRevExits() {
    return this.revExits;
  }
  public int getID() {
    return this.id;
  }
}