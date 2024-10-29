import java.sql.SQLException;
import java.util.List;

/**
 * La interfaz DetallePedidoRepositorioDAO define las operaciones para acceder a los
 * detalles de los pedidos en la base de datos.
 */
public interface DetallePedidoRepositorioDAO {

    /**
     * Recupera una lista de todos los detalles de los pedidos almacenados en la base de datos.
     *
     * @return una lista de objetos DetallePedido que representan cada detalle de pedido.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    List<DetallePedido> getAllDetallesPedido() throws SQLException;
}
