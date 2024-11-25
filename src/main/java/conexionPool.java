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
        dataSource.setUrl("jdbc:mysql://localhost:3306/prueba2");
        dataSource.setUsername("root");
        dataSource.setPassword(""); 

        dataSource.setInitialSize(5); 
        dataSource.setMaxTotal(10); 
        dataSource.setMaxIdle(5); 
        dataSource.setMinIdle(2); 
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
