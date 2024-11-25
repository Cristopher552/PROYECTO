import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz que define las operaciones para acceder a los pedidos en el sistema.
 * Proporciona métodos para recuperar información sobre los pedidos.
 */
public interface PedidoRepositorioDAO {

    List<Pedido> getAllPedidos() throws SQLException;
}

