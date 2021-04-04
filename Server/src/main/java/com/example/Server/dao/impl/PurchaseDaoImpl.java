package com.example.Server.dao.impl;

import com.example.Server.dao.PurchaseDao;
import com.example.Server.model.PurchaseRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PurchaseDaoImpl implements PurchaseDao {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public PurchaseDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public boolean insertPurchase(PurchaseRecord purchaseRecord) {
    String sql = "INSERT INTO PurchaseRecord (ItemId, Amount, StoreId, CustomerId, Date) VALUES (?,?,?,?,?)";
    return jdbcTemplate.update(sql, purchaseRecord.getItemId(), purchaseRecord.getAmount(),
        purchaseRecord.getStoreId(), purchaseRecord.getCustomerId(), purchaseRecord.getDate()) > 0;
  }
}
