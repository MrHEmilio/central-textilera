package mx.com.ctx.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ConexionDB {
    private final static Logger log = Logger.getLogger(ConexionDB.class.getName());
    private static Connection conn = null;
    private String url;
    private String user;
    private String pwd;
    public ConexionDB(String url, String user, String pwd) {
        this.url = url;
        this.user = user;
        this.pwd = pwd;
    }

    public void ConectarDB(){
        try{
            log.info("Connecting to database");
            conn = DriverManager.getConnection(url, user, pwd);
            log.info("Connected to database");
        }catch(SQLException sqle){
            log.warning("Error connecting to database: "+sqle.getMessage());
        }
    }

    public void DesconectarDB(){
        if(conn != null){
            try{
                conn.close();
                log.info("Disconnecting from database");
            }catch(SQLException sqle){
                log.warning("Error closing connection: "+sqle.getMessage());
            }
        }else
            log.warning("No active database connection");
    }

    public Connection getConnection(){
        return conn;
    }

}
