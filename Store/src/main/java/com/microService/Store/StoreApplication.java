package com.microService.Store;

import com.microService.Store.dao.StoreItemDao;
import com.microService.Store.rabbitMQ.Recv;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StoreApplication {

  private static StoreItemDao storeItemDao;

  @Autowired
  public StoreApplication(StoreItemDao storeItemDao) {
    StoreApplication.storeItemDao = storeItemDao;
  }

  public static void main(String[] args) throws IOException, TimeoutException {
    SpringApplication.run(StoreApplication.class, args);
    Recv receiver = new Recv(storeItemDao);
    Thread thread = new Thread(receiver);
    thread.start();
  }

}
