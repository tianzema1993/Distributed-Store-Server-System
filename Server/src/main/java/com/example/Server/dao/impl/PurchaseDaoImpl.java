package com.example.Server.dao.impl;

import com.example.Server.dao.PurchaseDao;
import com.example.Server.model.PurchaseRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PurchaseDaoImpl implements PurchaseDao {

  @Autowired
  @Qualifier("jdbcTemplate0")
  private JdbcTemplate jdbcTemplate0;
  @Autowired
  @Qualifier("jdbcTemplate1")
  private JdbcTemplate jdbcTemplate1;
  @Autowired
  @Qualifier("jdbcTemplate2")
  private JdbcTemplate jdbcTemplate2;
  @Autowired
  @Qualifier("jdbcTemplate3")
  private JdbcTemplate jdbcTemplate3;

  @Override
  public boolean insertPurchase(PurchaseRecord purchaseRecord) {
    String sql = "INSERT INTO PurchaseRecord (ItemId, Amount, StoreId, CustomerId, Date) VALUES (?,?,?,?,?)";
    int storeId = purchaseRecord.getStoreId();
    if (storeId % 4 == 0) {
      return jdbcTemplate0.update(sql, purchaseRecord.getItemId(), purchaseRecord.getAmount(),
          purchaseRecord.getStoreId(), purchaseRecord.getCustomerId(), purchaseRecord.getDate())
          > 0;
    } else if (storeId % 4 == 1) {
      return jdbcTemplate1.update(sql, purchaseRecord.getItemId(), purchaseRecord.getAmount(),
          purchaseRecord.getStoreId(), purchaseRecord.getCustomerId(), purchaseRecord.getDate())
          > 0;
    } else if (storeId % 4 == 2) {
      return jdbcTemplate2.update(sql, purchaseRecord.getItemId(), purchaseRecord.getAmount(),
          purchaseRecord.getStoreId(), purchaseRecord.getCustomerId(), purchaseRecord.getDate())
          > 0;
    } else {
      return jdbcTemplate3.update(sql, purchaseRecord.getItemId(), purchaseRecord.getAmount(),
          purchaseRecord.getStoreId(), purchaseRecord.getCustomerId(), purchaseRecord.getDate())
          > 0;
    }
  }
}

