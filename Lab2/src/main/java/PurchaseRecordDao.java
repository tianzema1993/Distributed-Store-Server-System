import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class PurchaseRecordDao {

  private static BasicDataSource dataSource;

  public PurchaseRecordDao() {
    dataSource = ConnectionManager.getDataSource();
  }

  public void createPurchase(PurchaseRecord newPurchaseRecord) throws SQLException {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String insertQueryStatement =
        "INSERT INTO PurchaseRecord (ItemId, Amount, StoreId, CustomerId, Date) " +
            "VALUES (?,?,?,?,?)";
    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      preparedStatement.setInt(1, newPurchaseRecord.getItemId());
      preparedStatement.setInt(2, newPurchaseRecord.getAmount());
      preparedStatement.setInt(3, newPurchaseRecord.getStoreId());
      preparedStatement.setInt(4, newPurchaseRecord.getCustomerId());
      preparedStatement.setString(5, newPurchaseRecord.getDate());
      // execute insert SQL statement
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }
}
