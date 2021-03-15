import com.google.gson.Gson;
import java.io.IOException;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class Test implements Runnable {
  public static void main(String[] args) {
    Test test = new Test();
    Thread[] threads = new Thread[1];
    for (int i = 0; i < 1; i++) {
      threads[i] = new Thread(test);
      threads[i].start();
    }
  }

  private void go() {
    HttpClient client = new HttpClient();
    Purchase purchase = new Purchase(1, 1000, 1, "20210101",
        100000);
    // local
//    String url = "http://localhost:8080";
    // hw server
//    String url = "http://ec2-3-92-32-114.compute-1.amazonaws.com:8080/HW2_war";
    // spring server
//    String url = "http://ec2-3-92-237-232.compute-1.amazonaws.com:8080";
    // load balancer
    String url = "http://my-load-balancer-61298793.us-east-1.elb.amazonaws.com:8080";
    url += "/purchase/store/" + 1 + "/customer/" + purchase.getCustomerId() + "/date/" + "20210101";
    PostMethod method = new PostMethod(url);
    // Provide custom retry handler is necessary
    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
        new DefaultHttpMethodRetryHandler(0, false));
    // add the request body
    Gson gson = new Gson();
    method.addParameter("items", gson.toJson(purchase.getItems()));
    try {
      // Execute the method.
      int getStatus = client.executeMethod(method);
      // Read the response body.
      // byte[] responseBody = method.getResponseBody();
      if (getStatus == HttpStatus.SC_OK) {
        System.out.println("ok");
      } else {
        System.out.println(getStatus);
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
    for (int i = 0; i < 1; i++) {
      go();
    }
  }
}
