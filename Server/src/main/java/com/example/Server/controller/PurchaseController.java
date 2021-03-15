package com.example.Server.controller;

import com.example.Server.exception.PurchaseNotAddException;
import com.example.Server.model.PurchaseRecord;
import com.example.Server.service.PurchaseService;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

  private final PurchaseService purchaseService;

  @Autowired
  public PurchaseController(PurchaseService purchaseService) {
    this.purchaseService = purchaseService;
  }

  @RequestMapping(value = "/store/{storeId}/customer/{customerId}/date/{date}", method = RequestMethod.POST)
  public int createPurchase(HttpServletRequest req, @PathVariable Integer storeId,
      @PathVariable Integer customerId, @PathVariable String date) throws PurchaseNotAddException {
    String jsonItems = req.getParameter("items");
    JSONArray arr = new JSONArray(jsonItems);
    for (int i = 0; i < arr.length(); i++) {
      JSONObject item = arr.getJSONObject(i);
      int itemId = item.getInt("itemId");
      int amount = item.getInt("amount");
      PurchaseRecord newPurchase = new PurchaseRecord(itemId, amount, storeId, customerId, date);
      if (!purchaseService.add(newPurchase)) {
        throw new PurchaseNotAddException("Purchase can't be added to the db");
      }
    }
    return 1;
  }

  @RequestMapping(value = "/store/{storeId}/customer/{customerId}/date/{date}", method = RequestMethod.GET)
  public String get(@PathVariable Integer storeId,
      @PathVariable Integer customerId, @PathVariable String date) {
    return "It works!";
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler({PurchaseNotAddException.class})
  public String handleException(Exception e) {
    return e.getMessage();
  }
}
