import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class MultiClient {

  public static void main(String[] args) {
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    try (InputStream input = classloader.getResourceAsStream("config.properties")) {
      // read from the config.properties
      Properties prop = new Properties();
      prop.load(input);
      int maxStores = Integer.parseInt(prop.getProperty("maxStores", String.valueOf(32)));
      int numOfCustomersPerStore = Integer
          .parseInt(prop.getProperty("numOfCustomersPerStore", String.valueOf(1000)));
      int maxItemId = Integer
          .parseInt(prop.getProperty("maxItemId", String.valueOf(100000)));
      int numOfPurchases = Integer.parseInt(prop.getProperty("numOfPurchases", String.valueOf(60)));
      int numOfItemsPerPurchase = Integer
          .parseInt(prop.getProperty("numOfItemsPerPurchase", String.valueOf(5)));
      String date = prop.getProperty("date", "20200101");
      String url = prop.getProperty("url", "http://localhost:8080");

      CountDownLatch east = new CountDownLatch(0);
      CountDownLatch middle = new CountDownLatch(1);
      CountDownLatch west = new CountDownLatch(1);
      CountDownLatch completed = new CountDownLatch(maxStores);

      // Counter array
      Counter[] counters = new Counter[maxStores];
      for (int j = 0; j < maxStores; j++) {
        counters[j] = new Counter();
      }

      // Create Threads
      Thread[] threads = new Thread[maxStores];
      for (int k = 1; k <= maxStores / 4; k++) {
        // instantiate a new Client in the east
        SingleClient newClient = new SingleClient(url, k, numOfPurchases, numOfCustomersPerStore, numOfItemsPerPurchase, maxItemId, date, east, middle, west, completed, counters[k-1]);
        Thread newThread = new Thread(newClient);
        threads[k-1] = newThread;
      }
      for (int k = maxStores / 4 + 1; k <= maxStores / 2; k++) {
        // instantiate a new Client in the middle
        SingleClient newClient = new SingleClient(url, k, numOfPurchases, numOfCustomersPerStore, numOfItemsPerPurchase, maxItemId, date, middle, middle, west, completed, counters[k-1]);
        Thread newThread = new Thread(newClient);
        threads[k-1] = newThread;
      }
      for (int k = maxStores / 2 + 1; k <= maxStores; k++) {
        // instantiate a new Client in the west
        SingleClient newClient = new SingleClient(url, k, numOfPurchases, numOfCustomersPerStore, numOfItemsPerPurchase, maxItemId, date, west, middle, west, completed, counters[k-1]);
        Thread newThread = new Thread(newClient);
        threads[k-1] = newThread;
      }

      // get the start time
      long start = System.currentTimeMillis();
      for (int i = 0; i < maxStores; i++) {
        // start the thread
        threads[i].start();
      }
      completed.await();
      // get the end time
      long end = System.currentTimeMillis();

      int total = maxStores * 9 * numOfPurchases;
      int success = 0;
      for (int j = 0; j < maxStores; j++) {
        success += counters[j].getPrimes();
      }

      System.out.println("The number of threads is " + maxStores);
      System.out.println("Total number of successful requests sent: " + success);
      System.out.println("Total number of unsuccessful requests: " + (total - success));
      double waitTime = (double) (end - start) / 1000;
      System.out.println("The total run time (in seconds): " + waitTime);
      System.out.println("The throughput is " + (double) success / waitTime);
    } catch (IOException | InterruptedException ex) {
      ex.printStackTrace();
    }
  }
}
