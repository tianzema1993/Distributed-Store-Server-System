import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Statistic {

  public static void calculate() {
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new FileReader("src/main/resources/Blog.csv"));
      // the first line is header
      reader.readLine();
      List<Integer> list = new ArrayList<>();
      int sum = 0;
      String dataLine;
      while ((dataLine = reader.readLine()) != null) {
        String[] data = dataLine.split(" ");
        list.add(Integer.parseInt(data[1]));
        sum += Integer.parseInt(data[1]);
      }
      Collections.sort(list);
      System.out.println("The mean response time (in ms): " + (double) sum / list.size());
      System.out.println("The median response time (in ms): " + (double) (list.get(list.size() / 2) + list.get(list.size() / 2 - 1)) / 2);
      System.out.println("The maximum response time (in ms): " + list.get(list.size() - 1));
      int index = (int) Math.ceil(99 / 100.0 * list.size());
      System.out.println("The 99th percentile response time (in ms): " + list.get(index - 1));
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
