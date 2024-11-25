import java.sql.SQLException;
import java.util.List;

/**
 * La interfaz DetallePedidoRepositorioDAO define las operaciones para acceder a los
 * detalles de los pedidos en la base de datos.
 */
public interface DetallePedidoRepositorioDAO {

    List<DetallePedido> getAllDetallesPedido() throws SQLException;
}
