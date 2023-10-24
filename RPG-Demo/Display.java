import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Display {
  JFrame screenFrame;
  GraphicsPanel screen;
  int scale;
  int panX;
  int panY;
  Map m;
  Player p;
  int pXOnScreen;
  int pYOnScreen;
  int speed;
  
  final Color playerColor = Color.red;
  final Color bgColor = new Color(9, 24, 33);
  final Color blockColor = new Color(224, 214, 211);
  final Color exitColor = new Color(53, 153, 196);
  
  BasicKeyListener keyListener;
  
  Display() {}
  
  public void open(Map m) {
    screenFrame = new JFrame("Display");
    screenFrame.setSize(1200, 800);
    screenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    screen = new GraphicsPanel();
    screenFrame.add(screen);
    screenFrame.setVisible(true);
    
    keyListener = new BasicKeyListener();
    screen.addKeyListener(keyListener);
    
    this.m = m;
    scale = m.getScale(); 
    panX = 0;
    panY = 0;
    p = new Player();
    pYOnScreen = p.getY()*scale-panY+(scale/2); pXOnScreen = p.getX()*scale-panX+(scale/2);
    speed = p.getSpeed();
  }
  public void setScale(int s) {
    scale = s;
  }
  
  //***********************************
  public void update() {
    screen.repaint();
    try { Thread.sleep(20); } catch (Exception e) {}
  }
  class GraphicsPanel extends JPanel {
    public GraphicsPanel() {
      setFocusable(true);
      requestFocusInWindow();
    }
    
    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.setColor(bgColor);
      g.fillRect(0, 0, 1200, 800);
      paintBlocks(g);
      paintPlayer(g);
    }
    public void paintBlocks(Graphics g) {
      for (int i = 0; i <  m.getYSize(); i++) {
        for (int j = 0; j <  m.getXSize(); j++) {
          if (m.hasBlock(j,i)) {
            Block b = m.getBlock(j,i);
            if (b.hasExit())
              g.setColor(exitColor);
            else
              g.setColor(blockColor);
            g.fillRect(b.getX()*scale-panX, b.getY()*scale-panY, scale, scale);
          }
        }
      }
    }
    public void paintPlayer(Graphics g) {
      g.setColor(playerColor);
      g.fillRect(pXOnScreen-scale/4, pYOnScreen-scale/4, scale/2, scale/2);
    }
  }
  
  //***********************************
  
  class BasicKeyListener implements KeyListener {
    public void keyPressed(KeyEvent e){
      try { Thread.sleep(20); } catch (Exception e1) {}
      int key = e.getKeyCode();
      if (key == KeyEvent.VK_UP && canMove(1)) {
        move(1);
      }
      if (key == KeyEvent.VK_DOWN && canMove(2)) {
        move(2);
      }
      if (key == KeyEvent.VK_LEFT && canMove(3)) {
        move(3);
      }
      if (key == KeyEvent.VK_RIGHT && canMove(4)) {
        move(4);
      }
    }
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e){ }
  }
  
  public void move(int dir) { // 1=up, 2=down, 3=left, 4=right
    if (dir == 1) {
      if (pYOnScreen <= 50)
        panY-=speed;
      else
        pYOnScreen-=speed;
    } else if (dir == 2) {
      if (pYOnScreen >= 705)
        panY+=speed;
      else
        pYOnScreen+=speed;      
    } else if (dir == 3) {
      if (pXOnScreen <= 50)
        panX-=speed;
      else
        pXOnScreen-=speed;
    } else {
      if (pXOnScreen >= 1105)
        panX+=speed;
      else
        pXOnScreen+=speed; 
    }
  }
  
  public boolean canMove(int dir) { // 1=up, 2=down, 3=left, 4=right
    try {
    boolean can = false;
    if (dir==1 && ((pYOnScreen)+panY-scale/4)/scale!=(pYOnScreen-scale/4-speed+panY)/scale) {
      if (m.getBlock(((pXOnScreen)+panX-scale/4)/scale, p.getY()).getUp() != null
         && m.getBlock(((pXOnScreen)+panX+scale/4)/scale, p.getY()).getUp() != null) {
        p.setY(p.getY()-1); can = true;
      } else
        return false;
    } else if (dir==2 && ((pYOnScreen+scale/4)+panY)/scale!=(pYOnScreen+scale/4+speed+panY)/scale) {
      if (m.getBlock(((pXOnScreen)+panX-scale/4)/scale, p.getY()).getDown() != null
         && m.getBlock(((pXOnScreen)+panX+scale/4)/scale, p.getY()).getDown() != null) {
        p.setY(p.getY()+1); can = true;
      } else
        return false;
    } else if (dir==3 && ((pXOnScreen-scale/4)+panX)/scale!=(pXOnScreen-scale/4-speed+panX)/scale) {
      if (m.getBlock(p.getX(), ((pYOnScreen)+panY-scale/4)/scale).getLeft() != null
         && m.getBlock(p.getX(), ((pYOnScreen)+panY+scale/4)/scale).getLeft() != null) {
        p.setX(p.getX()-1); can = true;
      } else
        return false;
    } else if (dir==4 && ((pXOnScreen+scale/4)+panX)/scale!=(pXOnScreen+scale/4+speed+panX)/scale) {
      if (m.getBlock(p.getX(), ((pYOnScreen)+panY-scale/4)/scale).getRight() != null
         && m.getBlock(p.getX(), ((pYOnScreen)+panY+scale/4)/scale).getRight() != null) {
        p.setX(p.getX()+1); can = true;
      } else
        return false;
    } else {
      return true;
    }
    
    if (can) {
      if (m.getBlock(p.getX(), p.getY()).hasExit()) {
        int scalePrev = scale;
        m = m.toNextMap(p, m.getExits().get(m.getBlock(p.getX(), p.getY())));
        scale = m.getScale();
        speed = speed * scale/scalePrev;
        panX = (int) -1 * Math.min(p.getX()*scale-50, Math.min(1105-p.getX()*scale, 0));
        panY = (int) -1 * Math.min(p.getY()*scale-50, Math.min(705-p.getY()*scale, 0));
        pYOnScreen = p.getY()*scale-panY+(scale/2); pXOnScreen = p.getX()*scale-panX+(scale/2);
      }
      return true;
    } else
      return false;
  } catch (Exception e) { return false; }
  }
}