import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase que gestiona un pool de conexiones a la base de datos utilizando Apache Commons DBCP.
 * Proporciona un método para obtener conexiones de forma eficiente y segura.
 */
public class conexionPool {
    private static BasicDataSource dataSource;

    static {
        // Configuración del pool de conexiones
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test-prueba");
        dataSource.setUsername("root");
        dataSource.setPassword(""); 

        // Configuración del tamaño del pool
        dataSource.setInitialSize(5); // Número inicial de conexiones en el pool
        dataSource.setMaxTotal(10); // Número máximo de conexiones en el pool
        dataSource.setMaxIdle(5); // Número máximo de conexiones inactivas en el pool
        dataSource.setMinIdle(2); // Número mínimo de conexiones inactivas en el pool
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
