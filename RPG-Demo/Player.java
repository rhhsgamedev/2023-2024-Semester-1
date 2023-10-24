public class Player {
  
  int x;
  int y;
  int speed;
  
  Player() {
    this.x = 12;
    this.y = 1;
    this.speed = 8;
  }
  
  //***********************************
  public void setX(int x) {
    this.x = x;
  }
  public int getX() {
    return this.x;
  }
  public void setY(int y) {
    this.y = y;
  }
  public int getY() {
    return this.y;
  }
  public int getSpeed() {
    return this.speed;
  }
}