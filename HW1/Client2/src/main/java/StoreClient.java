import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class StoreClient implements Runnable {

  private String url;
  private int storeId;
  private int numPurchases;
  private int numOfCustomersPerStore;
  private int numOfItemsPerPurchase;
  private int maximumItemId;
  private String date;
  private int numOfRequests = 0;
  private static int successfulCount = 0;
  private static int failCount = 0;
  private static CountDownLatch completed;
  private static CountDownLatch latch1;
  private static CountDownLatch latch2;
  private static boolean centralPhase = false;
  private static boolean westPhase = false;
  private static List<ResponseDetail> list = Collections.synchronizedList(new ArrayList<>());

  public StoreClient(String url, int storeId, int numPurchases, int numOfCustomersPerStore,
      int numOfItemsPerPurchase, int maximumItemId, String date) {
    this.url = url;
    this.storeId = storeId;
    this.numPurchases = numPurchases;
    this.numOfCustomersPerStore = numOfCustomersPerStore;
    this.numOfItemsPerPurchase = numOfItemsPerPurchase;
    this.maximumItemId = maximumItemId;
    this.date = date;
  }

  private void purchaseItems() {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost postRequest = new HttpPost(url);
      postRequest.setHeader("Content-type", "application/json; charset=utf-8");
      Purchase purchase = new Purchase(storeId, numOfCustomersPerStore, numOfItemsPerPurchase, date,
          maximumItemId);
      Gson gson = new Gson();
      HttpEntity httpEntity = new StringEntity(gson.toJson(purchase), "utf-8");
      postRequest.setEntity(httpEntity);
      long time1 = System.currentTimeMillis();
      try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
        long time2 = System.currentTimeMillis();
        numOfRequests += 1;
        if (!centralPhase && numOfRequests == numPurchases * 3) {
          openCentral();
        }
        if (!westPhase && numOfRequests == numPurchases * 5) {
          openWest();
        }
        if (response.getStatusLine().getStatusCode() == 200) {
          goodCall();
        } else {
          badCall();
        }
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          // return it as a String
          String result = EntityUtils.toString(entity);
          list.add(new ResponseDetail(time1, time2, "POST", result));
        }
      }
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  synchronized public static void openCentral() {
    centralPhase = true;
    latch1.countDown();
  }

  synchronized public static void openWest() {
    westPhase = true;
    latch2.countDown();
  }

  synchronized public static void goodCall() {
    successfulCount += 1;
  }

  synchronized public static void badCall() {
    failCount += 1;
  }

  @Override
  public void run() {
    for (int i = 0; i < numPurchases * 9; i++) {
      purchaseItems();
    }
    completed.countDown();
  }

  public static void main(String[] args) throws InterruptedException {
    try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
      Properties prop = new Properties();
      // load a properties file
      prop.load(input);
      int maxStores = Integer.parseInt(prop.getProperty("maxStores", String.valueOf(64)));
      int numOfCustomersPerStore = Integer
          .parseInt(prop.getProperty("numOfCustomersPerStore", String.valueOf(1000)));
      int maximumItemId = Integer
          .parseInt(prop.getProperty("maximumItemId", String.valueOf(100000)));
      int numPurchases = Integer.parseInt(prop.getProperty("numPurchases", String.valueOf(60)));
      int numOfItemsPerPurchase = Integer
          .parseInt(prop.getProperty("numOfItemsPerPurchase", String.valueOf(5)));
      String date = prop.getProperty("date", "20200101");
      String url = prop.getProperty("url", "http://localhost:8080/");

      completed = new CountDownLatch(maxStores);
      latch1 = new CountDownLatch(1);
      latch2 = new CountDownLatch(1);

      // get the start time
      long start = System.currentTimeMillis();

      System.out.println("East stores started");

      for (int i = 1; i <= maxStores / 4; i++) {
        Thread thread = new Thread(new StoreClient(url, i, numPurchases, numOfCustomersPerStore, numOfItemsPerPurchase, maximumItemId, date));
        thread.start();
      }
      // the main thread waits till reaching num * 3
      System.out.println("waiting for central stores...");
      latch1.await();
      System.out.println("Central stores started...");
      for (int i = maxStores / 4 + 1; i <= maxStores / 2; i++) {
        Thread thread = new Thread(new StoreClient(url, i, numPurchases, numOfCustomersPerStore, numOfItemsPerPurchase, maximumItemId, date));
        thread.start();
      }

      // the main thread waits till reaching num * 5
      System.out.println("waiting for west stores...");
      latch2.await();
      System.out.println("West stores started...");
      for (int i = maxStores / 2 + 1; i <= maxStores; i++) {
        Thread thread = new Thread(new StoreClient(url, i, numPurchases, numOfCustomersPerStore, numOfItemsPerPurchase, maximumItemId, date));
        thread.start();
      }

      completed.await();

      // get the end time
      long end = System.currentTimeMillis();

      System.out.println("The number of threads is " + maxStores);
      System.out.println("Total number of successful requests sent: " + successfulCount);
      System.out.println("Total number of unsuccessful requests: " + failCount);
      double waitTime = (double) (end - start) / 1000;
      System.out.println("The total run time (in seconds): " + waitTime);
      System.out.println("The throughput is " + (double) (successfulCount + failCount) / waitTime);

      // write to the csv
      BufferedWriter output = new BufferedWriter(new FileWriter("src/main/resources/Blog.csv"));
      output.write("startTime latency requestType responseContent");
      output.write("\n");
      for (ResponseDetail responseDetail: list) {
        output.write(responseDetail.toString());
      }
      output.close();

      // generate the statistic result
      Statistic.calculate();

    } catch (IOException ex) {
      System.err.println(ex);
    }
  }
}
