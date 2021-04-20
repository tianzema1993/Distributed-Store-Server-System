package com.example.Server.kafka;

import com.example.Server.model.PurchaseRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafKaProducerService {

  @Value(value = "${purchase.topic.name}")
  private String purchaseTopicName;

  @Autowired
  private KafkaTemplate<String, PurchaseRecord> purchaseKafkaTemplate;

  public void savePurchase(PurchaseRecord purchaseRecord) {
    ListenableFuture<SendResult<String, PurchaseRecord>> future
        = this.purchaseKafkaTemplate.send(purchaseTopicName, purchaseRecord);

    future.addCallback(new ListenableFutureCallback<>() {
      @Override
      public void onSuccess(SendResult<String, PurchaseRecord> result) {
      }

      @Override
      public void onFailure(Throwable ex) {
      }
    });
  }
}
