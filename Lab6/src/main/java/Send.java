import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

// In the terminal, type "brew services start rabbitmq" to start the server
// "brew services stop rabbitmq" to stop
//public class Send {
//  private final static String QUEUE_NAME = "hello";
//
//  public static void main(String[] argv) throws Exception {
//    ConnectionFactory factory = new ConnectionFactory();
//    factory.setHost("localhost");
//    try (Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel()) {
//      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//      String message = "Hello World!";
//      channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
//      System.out.println(" [x] Sent '" + message + "'");
//    }
//  }
//}

public class Send {

  private final static String QUEUE_NAME = "threadExQ";
  private final static int NUM_MESSAGES_PER_THREAD = 10;

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");

    final Connection conn = factory.newConnection();
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        try {
          // channel per thread
          Channel channel = conn.createChannel();
          channel.queueDeclare(QUEUE_NAME, true, false, false, null);
          for (int i = 0; i < NUM_MESSAGES_PER_THREAD; i++) {
            String message = "Here's a message " + Integer.toString(i);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
          }
          channel.close();
          System.out.println(" [All Messages  Sent '");
        } catch (IOException | TimeoutException ex) {
          Logger.getLogger(Send.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    };
    // start threads and wait for completion
    Thread t1 = new Thread(runnable);
    Thread t2 = new Thread(runnable);
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    // close connection
    conn.close();

  }
}
