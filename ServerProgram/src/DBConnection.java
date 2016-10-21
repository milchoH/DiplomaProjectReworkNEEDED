/**
 * Created by milcho on 10/12/16.
 */
import java.sql.*;
public class DBConnection {

    public static final String DBURL = "jdbc:h2:tcp://localhost/~/test";
    public static final String DBUSER = "sa";
    public static final String DBPASS = "qwerty";
    Connection con;

    public void connectingToDB() throws ClassNotFoundException, SQLException{
        Class.forName("org.h2.Driver");
        con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
    }

    public ResultSet SelectDB(String data) throws SQLException{
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(data);
        return rs;
    }
    public void insertDB(String data) throws SQLException{
        Statement st = con.createStatement();
        st.executeUpdate(data);

    }
    public void closeConnectionToDB() throws SQLException{
        con.close();
    }

}
