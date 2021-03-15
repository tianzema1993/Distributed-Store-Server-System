package com.example.Server.dao;

import com.example.Server.model.PurchaseRecord;

public interface PurchaseDao {
  boolean addPurchase(PurchaseRecord purchaseRecord);
}
