//import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import java.sql.Driver;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.Enumeration;
//
//public class MyServletContextListener implements ServletContextListener {
//
//  @Override
//  public void contextInitialized(ServletContextEvent servletContextEvent) {
//
//  }
//
//  @Override
//  public void contextDestroyed(ServletContextEvent servletContextEvent) {
//    Enumeration<Driver> drivers = DriverManager.getDrivers();
//    Driver driver = null;
//    while (drivers.hasMoreElements()) {
//      try {
//        driver = drivers.nextElement();
//        DriverManager.deregisterDriver(driver);
//      } catch (SQLException ex) {
//        ex.printStackTrace();
//      }
//    }
//    AbandonedConnectionCleanupThread.checkedShutdown();
//  }
//}

