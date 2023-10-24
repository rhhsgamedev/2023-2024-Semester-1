public class Block {
  int x;
  int y;
  Block up; Block down; Block left; Block right;
  boolean hasExit; int exitNum;
  
  Block(int x, int y) {
    this.x = x; this.y = y;
  }
  
  public String toString() {
    if (hasExit)
      return "E";
    else
      return ".";
  }
  //***********************************
  public int getX() {
    return this.x;
  }
  public int getY() {
    return this.y;
  }
  
  public void setUp(Block b) {
    this.up = b;
  }
  public Block getUp() {
    return this.up;
  }
  public void setDown(Block b) {
    this.down = b;
  }
  public Block getDown() {
    return this.down;
  }
  public void setRight(Block b) {
    this.right = b;
  }
  public Block getRight() {
    return this.right;
  }
  public void setLeft(Block b) {
    this.left = b;
  }
  public Block getLeft() {
    return this.left;
  }
  
  public boolean hasExit() {
    return hasExit;
  }
  public void setExit(int n) {
    this.hasExit = true;
    this.exitNum = n;
  }
  public int getExitNum() {
    return this.exitNum;
  }
  public String checkSurroundings() {
    String c = "";
    c = c + this.getX() + " " + this.getY() + " left: ";
    if (this.getLeft() == null)
      c = c + "null down: ";
    else
      c = c + this.getLeft().getX() + " " + this.getLeft().getY() + " down: ";
    if (this.getDown() == null)
      c = c + "null";
    else
      c = c + this.getDown().getX() + " " + this.getDown().getY();
    return c;
  }
}