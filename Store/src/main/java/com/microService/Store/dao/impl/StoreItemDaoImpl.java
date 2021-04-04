package com.microService.Store.dao.impl;

import com.microService.Store.dao.StoreItemDao;
import com.microService.Store.model.StoreItemRecord;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StoreItemDaoImpl implements StoreItemDao {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public StoreItemDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public int insertRecord(StoreItemRecord record) {
    String sql = "insert into StoreItemRecord (storeId, itemId, amount) values (?,?,?)";
    return jdbcTemplate.update(sql, record.getStoreId(), record.getItemId(), record.getAmount());
  }

  @Override
  public List<Integer> getTopTenItemsForStoreN(int storeId) {
    try {
      String sql = "select Temp.itemId from (select itemId, sum(amount) as Sum from StoreItemRecord where StoreId = ? group by itemId order by Sum desc limit 10) as Temp";
      return jdbcTemplate.queryForList(sql, Integer.TYPE, storeId);
    } catch (EmptyResultDataAccessException e) {
      return new ArrayList<>();
    }
  }

  @Override
  public List<Integer> getTopTenStoreForItemN(int itemId) {
    try {
      String sql = "select Temp.storeId from (select storeId, sum(amount) as Sum from StoreItemRecord where itemId = ? group by storeId order by Sum desc limit 10) as Temp";
      return jdbcTemplate.queryForList(sql, Integer.TYPE, itemId);
    } catch (EmptyResultDataAccessException e) {
      return new ArrayList<>();
    }
  }
}
