package com.microService.Store.rabbitMQ;

import com.microService.Store.dao.StoreItemDao;
import com.microService.Store.model.PurchaseRecord;
import com.microService.Store.model.StoreItemRecord;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import com.google.gson.Gson;

public class Recv implements Runnable {

  private final Channel channel;
  private final StoreItemDao storeItemDao;
  private final Gson gson = new Gson();

  public Recv(StoreItemDao storeItemDao) throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("3.229.130.222");
    factory.setPort(5672);
    factory.setConnectionTimeout(10000);
    factory.setUsername("username");
    factory.setPassword("password");
    final Connection connection = factory.newConnection();
    channel = connection.createChannel();
    this.storeItemDao = storeItemDao;
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
            StoreItemRecord storeItemRecord = new StoreItemRecord();
            storeItemRecord.setStoreId(purchaseRecord.getStoreId());
            storeItemRecord.setItemId(purchaseRecord.getItemId());
            storeItemRecord.setAmount(purchaseRecord.getAmount());
            storeItemDao.insertRecord(storeItemRecord);
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
