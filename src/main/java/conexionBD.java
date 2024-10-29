import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase de utilidad para gestionar la conexión a la base de datos.
 * Esta clase proporciona métodos para obtener una conexión utilizando un 
 * pool de conexiones.
 */
public class conexionBD {

    /**
     * Obtiene una conexión a la base de datos.
     *
     * @return una instancia de {@link Connection} para interactuar con la base de datos.
     * @throws SQLException si ocurre un error al obtener la conexión.
     */
    public static Connection getConnection() throws SQLException {
        return conexionPool.getConnection();
    }
}
