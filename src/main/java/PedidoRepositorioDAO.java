import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz que define las operaciones para acceder a los pedidos en el sistema.
 * Proporciona métodos para recuperar información sobre los pedidos.
 */
public interface PedidoRepositorioDAO {
    
    /**
     * Obtiene una lista de todos los pedidos en el sistema.
     *
     * @return una lista de objetos {@link Pedido} que representan todos los pedidos.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    List<Pedido> getAllPedidos() throws SQLException;
}

