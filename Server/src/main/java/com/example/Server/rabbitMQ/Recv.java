package com.example.Server.rabbitMQ;

import com.example.Server.dao.PurchaseDao;
import com.example.Server.model.PurchaseRecord;
import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Recv implements Runnable {

  private final Channel channel;
  private final PurchaseDao purchaseDao;
  private final Gson gson = new Gson();

  public Recv(PurchaseDao purchaseDao) throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("3.229.130.222");
    factory.setPort(5672);
    factory.setUsername("username");
    factory.setPassword("password");
    factory.setConnectionTimeout(10000);
    final Connection connection = factory.newConnection();
    channel = connection.createChannel();
    this.purchaseDao = purchaseDao;
  }

  private void fetchPurchase() throws IOException {
    String EXCHANGE_NAME = "my_exchange";
    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
    String queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, EXCHANGE_NAME, "");
    System.out.println("Waiting for messages...");
    channel.basicConsume(queueName, false, "",
        new DefaultConsumer(channel) {
          @Override
          public void handleDelivery(String consumerTag,
              Envelope envelope,
              AMQP.BasicProperties properties,
              byte[] body)
              throws IOException {
            long deliveryTag = envelope.getDeliveryTag();
            PurchaseRecord purchaseRecord = gson.fromJson(new String(body, StandardCharsets.UTF_8), PurchaseRecord.class);
            purchaseDao.insertPurchase(purchaseRecord);
            channel.basicAck(deliveryTag, false);
          }
        });
  }

  @Override
  public void run() {
    try {
      fetchPurchase();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
