package com.microService.Store.controller;

import com.microService.Store.dao.StoreItemDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class StoreItemController {

  private final StoreItemDao storeItemDao;

  @Autowired
  public StoreItemController(StoreItemDao storeItemDao) {
    this.storeItemDao = storeItemDao;
  }

  @RequestMapping(value = "/store/{storeId}", method = RequestMethod.GET)
  public List<Integer> getTopTenItemsByStoreN(@PathVariable Integer storeId) {
    return storeItemDao.getTopTenItemsForStoreN(storeId);
  }

  @RequestMapping(value = "/top10/{itemId}", method = RequestMethod.GET)
  public List<Integer> getTopTenStoresByItemN(@PathVariable Integer itemId) {
    return storeItemDao.getTopTenStoreForItemN(itemId);
  }
}
