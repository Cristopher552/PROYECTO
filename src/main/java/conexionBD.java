import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase de utilidad para gestionar la conexión a la base de datos.
 * Esta clase proporciona métodos para obtener una conexión utilizando un 
 * pool de conexiones.
 */
public class conexionBD {

    public static Connection getConnection() throws SQLException {
        return conexionPool.getConnection();
    }
}
