import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase DetallePedidoRepositorioImplem es una implementación de la interfaz 
 * DetallePedidoRepositorioDAO. Proporciona métodos para acceder a los detalles de los pedidos
 * almacenados en la base de datos.
 */
public class DetallePedidoRepositorioImplem implements DetallePedidoRepositorioDAO {

    /**
     * Recupera todos los detalles de los pedidos desde la base de datos.
     *
     * @return una lista de objetos DetallePedido que representan todos los detalles de pedidos.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    @Override
    public List<DetallePedido> getAllDetallesPedido() throws SQLException {
        List<DetallePedido> detalles = new ArrayList<>();
        Connection connection = null;

        try {
            // Obtiene una conexión a la base de datos
            connection = conexionBD.getConnection();
            String sql = "SELECT pedido_id, nombre_producto, cantidad, precio FROM detalle_pedido";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                // Recorre los resultados y los agrega a la lista de detalles
                while (rs.next()) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setPedidoId(rs.getInt("pedido_id"));
                    detalle.setNombreProducto(rs.getString("nombre_producto"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecio(rs.getBigDecimal("precio"));
                    detalles.add(detalle);
                }
            }
        } finally {
            // Asegura que la conexión se cierre
            if (connection != null) {
                connection.close();
            }
        }
        return detalles;
    }
}
