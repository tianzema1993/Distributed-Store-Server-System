package com.microService.Store.dao;

import com.microService.Store.model.StoreItemRecord;
import java.util.List;

public interface StoreItemDao {
  int insertRecord(StoreItemRecord record);
  List<Integer> getTopTenItemsForStoreN(int storeId);
  List<Integer> getTopTenStoreForItemN(int itemId);
}
