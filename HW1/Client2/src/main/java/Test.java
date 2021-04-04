import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class Test {

  public static void main(String[] args) throws IOException {
    HttpClient client = new HttpClient();
//    String url = "http://ec2-3-90-248-99.compute-1.amazonaws.com:8080/items/store/1";
    String url = "http://localhost:8888/items/store/1";
    GetMethod method = new GetMethod(url);
    client.executeMethod(method);
    String resp = convertStreamToString(method.getResponseBodyAsStream());
    System.out.println(resp);

    HttpClient client1 = new HttpClient();
//    String url1 = "http://ec2-3-90-248-99.compute-1.amazonaws.com:8080/items/top10/69501";
    String url1 = "http://localhost:8888/items/top10/69501";
    GetMethod method1 = new GetMethod(url1);
    client1.executeMethod(method1);
    String resp1 = convertStreamToString(method1.getResponseBodyAsStream());
    System.out.println(resp1);
  }

  private static String convertStreamToString(InputStream is) {
    Scanner s = new Scanner(is).useDelimiter("\\A");
    return s.hasNext() ? s.next().replace(",", ",\n") : "";
  }
}