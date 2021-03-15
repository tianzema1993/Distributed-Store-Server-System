import com.google.gson.Gson;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "StoreServlet")
public class StoreServlet extends HttpServlet {

  protected void doPost(HttpServletRequest req,
      HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType("text/plain");
    String urlPath = req.getPathInfo();
    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing parameters");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("url format incorrect");
    } else {
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      int storeId = Integer.parseInt(urlParts[2]);
      int customerId = Integer.parseInt(urlParts[4]);
      String date = urlParts[6];
      String jsonString = req.getParameter("items");
      Gson gson = new Gson();
      Item[] items = gson.fromJson(jsonString, Item[].class);
      for (int i = 0; i < items.length; i++) {
        Item item = items[i];
        int itemId = item.getItemId();
        int amount = item.getAmount();
        PurchaseRecord purchaseRecord = new PurchaseRecord(itemId, amount, storeId, customerId, date);
        try {
          PurchaseRecordDao purchaseRecordDao = new PurchaseRecordDao();
          purchaseRecordDao.createPurchase(purchaseRecord);
          res.getWriter().write("adding new purchase to db");
          res.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
          e.printStackTrace();
          res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
      }
    }
  }

  protected void doGet(HttpServletRequest req,
      HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType("text/plain");
    String urlPath = req.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing paramterers");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      res.getWriter().write("It works!");
    }
  }

  private boolean isUrlValid(String[] urlPath) {
    // TODO: validate the request url path according to the API spec
    // urlPath  = "store/storeId/customer/customerId/date/xxxxxxxx"
    // urlParts = [, store, storeId, customer, customerId, date, xxxxxxxx]
    if (!urlPath[1].equals("store") || !urlPath[3].equals("customer") || !urlPath[5].equals("date")) return false;
    try {
      int storeId = Integer.parseInt(urlPath[2]);
      int customerId = Integer.parseInt(urlPath[4]);
      DateFormat format = new SimpleDateFormat("yyyyMMdd");
      Date date = format.parse(urlPath[6]);
    } catch (NumberFormatException | ParseException e) {
      return false;
    }
    return true;
  }
}
