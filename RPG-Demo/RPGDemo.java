import java.util.*;
import java.io.*;

public class RPGDemo {
  
  static Map m;
  static int mapCode;
  static Display d;
  
  public static void main(String[] args) {
    try {
      File progress = new File("progress.txt");
      Scanner readProgress = new Scanner(progress);
      mapCode = Integer.parseInt(readProgress.nextLine().split(": ")[1]);
    } catch (Exception e) {
      System.out.println(e);
    }
    m = MapReader.readMapFile(mapCode);
    d = new Display();
    d.open(m);
    while (true)
      d.update();
  }
}
