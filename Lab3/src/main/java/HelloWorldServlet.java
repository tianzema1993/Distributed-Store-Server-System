import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HelloWorldServlet")
public class HelloWorldServlet extends HttpServlet {
  private String msg;

  public void init() throws ServletException {
    // Initialization
    msg = "Hello World";
  }

  // handle a GET request
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    // Set response content type to text
    response.setContentType("text/html");

    // sleep for 1000ms. You can vary this value for different tests
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Send the response
    PrintWriter out = response.getWriter();
    out.println("<h1>" + msg + "</h1>");
  }

  public void destroy() {
    // nothing to do here
  }
}
