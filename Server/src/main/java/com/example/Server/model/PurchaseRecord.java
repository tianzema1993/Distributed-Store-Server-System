package com.example.Server.model;

public class PurchaseRecord {

  private int recordId;
  private int itemId;
  private int amount;
  private int storeId;
  private int customerId;
  private String date;

  public PurchaseRecord() {
  }

  public PurchaseRecord(int itemId, int amount, int storeId, int customerId, String date) {
    this.itemId = itemId;
    this.amount = amount;
    this.storeId = storeId;
    this.customerId = customerId;
    this.date = date;
  }

  public int getRecordId() {
    return recordId;
  }

  public void setRecordId(int recordId) {
    this.recordId = recordId;
  }

  public int getItemId() {
    return itemId;
  }

  public void setItemId(int itemId) {
    this.itemId = itemId;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public int getStoreId() {
    return storeId;
  }

  public void setStoreId(int storeId) {
    this.storeId = storeId;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  @Override
  public String toString() {
    return "PurchaseRecord{" +
        "recordId=" + recordId +
        ", itemId=" + itemId +
        ", amount=" + amount +
        ", storeId=" + storeId +
        ", customerId=" + customerId +
        ", date='" + date + '\'' +
        '}';
  }
}
