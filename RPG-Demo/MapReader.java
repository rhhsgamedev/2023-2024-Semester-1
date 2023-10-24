import java.util.*;
import java.io.*;

public class MapReader {
    public static Map readMapFile(int mapCode) {
    try {
      ArrayList<String> a = new ArrayList<String>();
      HashMap<Block, Integer> exits = new HashMap<Block, Integer>(); HashMap<Integer,Block> revExits = new HashMap<Integer,Block>();
      File map = new File("map"+mapCode+".txt");
      Scanner readMap = new Scanner(map);
      int scale = Integer.parseInt(readMap.nextLine().split(": ")[1]);
      ArrayList<Integer> e = new ArrayList<Integer>();
      while (readMap.hasNextLine()) {
        String n = readMap.nextLine();
        if (n.equals("-")) 
          break;
        else
          a.add(n);
      }
      while (readMap.hasNextLine())
        e.add(Integer.parseInt(readMap.nextLine().split(": map")[1]));
      Map m = new Map(a.size(), a.get(0).length(), mapCode);
      m.setScale(scale);
      for (int i = 0; i < a.size(); i++) {
        for (int j = 0; j < a.get(0).length(); j++) {
          char b = a.get(i).charAt(j);
          if (b != 'x') {
            Block block = new Block(j, i);
            if (b != '.') {
              int bRef = Integer.parseInt(String.valueOf(b));
              exits.put(block, e.get(bRef-1)); revExits.put(e.get(bRef-1), block);
              block.setExit(e.get(bRef-1));
            }
            m.addBlock(block);
          }
        }
      }
      m.setExits(exits, revExits);
      readMap.close();
      return m;
    } catch (Exception e) {
      System.out.println(e);
    }
    return null;
  }
}