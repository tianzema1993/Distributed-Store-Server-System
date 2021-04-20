package com.example.Server.kafka;

import com.example.Server.dao.PurchaseDao;
import com.example.Server.model.PurchaseRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

  private final PurchaseDao purchaseDao;

  @Autowired
  public KafkaConsumerService(PurchaseDao purchaseDao) {
    this.purchaseDao = purchaseDao;
  }

  @KafkaListener(topics = "${purchase.topic.name}",
      groupId = "${purchase.topic.group.id}",
      containerFactory = "purchaseKafkaListenerContainerFactory")
  public void consume(PurchaseRecord purchaseRecord) {
    purchaseDao.insertPurchase(purchaseRecord);
  }
}
