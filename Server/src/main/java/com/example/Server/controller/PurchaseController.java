package com.example.Server.controller;

import com.example.Server.exception.PurchaseNotAddException;
import com.example.Server.model.PurchaseRecord;
import com.example.Server.pools.ChannelFactory;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

  private final ObjectPool<Channel> pool;
  private final Gson gson = new Gson();

  @Autowired
  public PurchaseController() throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("3.229.130.222");
    factory.setPort(5672);
    factory.setConnectionTimeout(10000);
    factory.setUsername("username");
    factory.setPassword("password");
    Connection conn = factory.newConnection();
    pool = new GenericObjectPool<>(new ChannelFactory(conn));
  }

  @RequestMapping(value = "/store/{storeId}/customer/{customerId}/date/{date}", method = RequestMethod.POST)
  public String createPurchase(HttpServletRequest req, @PathVariable Integer storeId,
      @PathVariable Integer customerId, @PathVariable String date) throws Exception {
    Channel channel = null;
    try {
      channel = pool.borrowObject();
      String EXCHANGE_NAME = "my_exchange";
      channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
      String jsonItems = req.getParameter("items");
      JSONArray arr = new JSONArray(jsonItems);
      for (int i = 0; i < arr.length(); i++) {
        JSONObject item = arr.getJSONObject(i);
        int itemId = item.getInt("itemId");
        int amount = item.getInt("amount");
        PurchaseRecord purchaseRecord = new PurchaseRecord(itemId, amount, storeId, customerId, date);
        channel.basicPublish(EXCHANGE_NAME, "", null, gson.toJson(purchaseRecord).getBytes(StandardCharsets.UTF_8));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (channel != null) {
        pool.returnObject(channel);
      }
    }
    return "Purchase added to the queue";
  }

  @RequestMapping(value = "/store/{storeId}/customer/{customerId}/date/{date}", method = RequestMethod.GET)
  public String get(@PathVariable Integer storeId,
      @PathVariable Integer customerId, @PathVariable String date) {
    return "It works!";
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler({PurchaseNotAddException.class})
  public String handleException(Exception e) {
    return e.getMessage();
  }
}
