import java.util.Random;

public class Item {
  private int itemId;
  private int amount = 1;

  public Item(int maximumItemId) {
    this.itemId = new Random().nextInt(maximumItemId) + 1;
  }
}
