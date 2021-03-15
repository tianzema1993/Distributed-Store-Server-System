public class Item {
  private int itemId;
  private int amount;

  public Item(int itemId, int amount) {
    this.itemId = itemId;
    this.amount = amount;
  }

  public int getItemId() {
    return itemId;
  }

  public int getAmount() {
    return amount;
  }

  @Override
  public String toString() {
    return "Item{" +
        "itemId=" + itemId +
        ", amount=" + amount +
        '}';
  }
}
