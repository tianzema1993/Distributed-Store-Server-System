import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClient implements Runnable {

  private static String url = "http://localhost:8080/Lab3_war_exploded/hello";
  private static int n = 100;
  private static CountDownLatch completed = new CountDownLatch(n);
  private static int count = 0;

  public static void inc() {
    count++;
  }

  @Override
  public void run() {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpGet request = new HttpGet(url);
      try (CloseableHttpResponse response = httpClient.execute(request)) {
        HttpEntity entity = response.getEntity();
        if (entity != null) {
          // return it as a String
          String result = EntityUtils.toString(entity);
          System.out.println(result);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    inc();
    completed.countDown();
  }

  public static void main(String[] args) throws InterruptedException {
    System.out.println(System.currentTimeMillis());
    for (int i = 0; i < n; i++) {
      Thread thread = new Thread(new HttpClient());
      thread.start();
    }
    completed.await();
    System.out.println(System.currentTimeMillis());
    System.out.println(count);
  }
}
