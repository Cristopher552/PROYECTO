import java.sql.Connection;
import java.sql.SQLException;

public class conexionBD {
    public static Connection getConnection() throws SQLException {
        return conexionPool.getConnection();
    }
}
