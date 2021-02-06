import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Purchase {
  private int storeId;
  private int customerId;
  private List<Item> items = new ArrayList<>();
  private String date;

  public Purchase(int storeId, int numOfCustomersPerStore, int numOfItemsPerPurchase, String date, int maximumItemId) {
    this.storeId = storeId;
    this.customerId = 1000 * storeId + new Random().nextInt(numOfCustomersPerStore);
    this.date = date;
    for (int i = 0; i < numOfItemsPerPurchase; i++) {
      items.add(new Item(maximumItemId));
    }
  }
}
