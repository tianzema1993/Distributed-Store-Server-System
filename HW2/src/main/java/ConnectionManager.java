import org.apache.commons.dbcp2.BasicDataSource;

public class ConnectionManager {
  private static BasicDataSource dataSource;

  // localhost
  private static final String HOST_NAME = "localhost";
  private static final String PORT = "3306";
  private static final String DATABASE = "StoreDatabase";
  private static final String USERNAME = "root2";
  private static final String PASSWORD = "931207";

  // aws rds
//  private static final String HOST_NAME = "store.c9l6ylgoeuub.us-east-1.rds.amazonaws.com";
//  private static final String PORT = "3306";
//  private static final String DATABASE = "StoreDatabase";
//  private static final String USERNAME = "master";
//  private static final String PASSWORD = "931207abc";

  static {
    // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
    dataSource = new BasicDataSource();
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);
    dataSource.setUrl(url);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    dataSource.setInitialSize(10);
    dataSource.setMaxTotal(60);
  }

  public static BasicDataSource getDataSource() {
    return dataSource;
  }
}
