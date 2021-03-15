package com.example.Server.service.impl;

import com.example.Server.dao.PurchaseDao;
import com.example.Server.model.PurchaseRecord;
import com.example.Server.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseServiceImpl implements PurchaseService {

  private final PurchaseDao purchaseDao;

  @Autowired
  public PurchaseServiceImpl(PurchaseDao purchaseDao) {
    this.purchaseDao = purchaseDao;
  }

  @Override
  public boolean add(PurchaseRecord purchaseRecord) {
    return purchaseDao.addPurchase(purchaseRecord);
  }
}
