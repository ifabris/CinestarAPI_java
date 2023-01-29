/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.SQL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import javax.sql.DataSource;

/**
 *
 * @author Ivan
 */
public class DataSourceSingleton {

   
    private DataSourceSingleton(){
}
    
 private static DataSource instance;
 
 public static DataSource getInstance(){
     if (instance ==null) {
         instance = createInstance();
     }
     return instance;
 }
 
     private static DataSource createInstance() {
      
         SQLServerDataSource dataSource = new SQLServerDataSource();
         dataSource.setServerName(SERVER_NAME);
         dataSource.setDatabaseName(DATABASE_NAME);
         dataSource.setUser(UID);
         dataSource.setPassword(PWD);
         
         return dataSource;
    }
    private static final String PWD = "SQL";
    private static final String UID = "sas";
    private static final String DATABASE_NAME = "JAVA";
    private static final String SERVER_NAME = "DESKTOP-UELQJN8";
    
    
}

