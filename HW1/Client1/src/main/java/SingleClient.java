import com.google.gson.Gson;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class SingleClient implements Runnable {

  // purchase information
  private String url;
  private int storeId;
  private int numOfPurchases;
  private int numOfCustomersPerStore;
  private int numOfItemsPerPurchase;
  private int maxItemId;
  private String date;
  // signal to open
  private CountDownLatch waitToOpen;
  private CountDownLatch middlePhrase;
  private CountDownLatch westPhrase;
  private CountDownLatch completed;
  private HttpClient client;
  private Counter counter;

  public SingleClient(String url, int storeId, int numOfPurchases, int numOfCustomersPerStore,
      int numOfItemsPerPurchase, int maxItemId, String date,
      CountDownLatch waitToOpen, CountDownLatch middlePhrase,
      CountDownLatch westPhrase, CountDownLatch completed, Counter counter) {
    this.url = url;
    this.storeId = storeId;
    this.numOfPurchases = numOfPurchases;
    this.numOfCustomersPerStore = numOfCustomersPerStore;
    this.numOfItemsPerPurchase = numOfItemsPerPurchase;
    this.maxItemId = maxItemId;
    this.date = date;
    this.waitToOpen = waitToOpen;
    this.middlePhrase = middlePhrase;
    this.westPhrase = westPhrase;
    this.completed = completed;
    this.client = new HttpClient();
    this.counter = counter;
  }

  private void purchaseItems() {
    Purchase purchase = new Purchase(storeId, numOfCustomersPerStore, numOfItemsPerPurchase, date,
        maxItemId);
    url +=
        "/purchase/store/" + storeId + "/customer/" + purchase.getCustomerId() + "/date/" + date;
    PostMethod method = new PostMethod(url);
    // Provide custom retry handler is necessary
    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
        new DefaultHttpMethodRetryHandler(0, false));
    // add the request body
    NameValuePair[] params = new NameValuePair[1];
    Gson gson = new Gson();
    params[0] = new NameValuePair("items", gson.toJson(purchase.getItems()));
    method.addParameters(params);
    try {
      // Execute the method.
      int getStatus = client.executeMethod(method);
      if (getStatus == HttpStatus.SC_OK) {
        this.counter.increasePrimeCount();
      }
    } catch (HttpException e) {
      System.err.println("Fatal protocol violation: " + e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("Fatal transport error: " + e.getMessage());
      e.printStackTrace();
    } finally {
      // Release the connection.
      method.releaseConnection();
    }
  }

  @Override
  public void run() {
    try {
      waitToOpen.await();
      for (int hour = 1; hour <= 9; hour++) {
        for (int i = 0; i < numOfPurchases; i++) {
          purchaseItems();
        }
        if (hour == 3 && middlePhrase != null) {
          middlePhrase.countDown();
        }
        if (hour == 5 && westPhrase != null) {
          westPhrase.countDown();
        }
      }
      completed.countDown();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
