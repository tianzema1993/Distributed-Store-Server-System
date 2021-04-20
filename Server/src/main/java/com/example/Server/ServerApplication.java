package com.example.Server;

import com.example.Server.dao.PurchaseDao;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ServerApplication extends SpringBootServletInitializer {

  private static PurchaseDao purchaseDao;

  @Autowired
  public ServerApplication(PurchaseDao purchaseDao) {
    ServerApplication.purchaseDao = purchaseDao;
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(ServerApplication.class);
  }

  public static void main(String[] args) throws IOException, TimeoutException {
    SpringApplication.run(ServerApplication.class, args);
    // for RabbitMQ
//    Recv receiver = new Recv(purchaseDao);
//    Thread thread = new Thread(receiver);
//    thread.start();
  }
}
