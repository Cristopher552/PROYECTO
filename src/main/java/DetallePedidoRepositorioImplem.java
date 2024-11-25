import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoRepositorioImplem implements DetallePedidoRepositorioDAO {

    @Override
    public List<DetallePedido> getAllDetallesPedido() throws SQLException {
        List<DetallePedido> detalles = new ArrayList<>();
        Connection connection = null;

        try {
            connection = conexionBD.getConnection();
            String sql = "SELECT detalle_id, pedido_id, nombre_producto, cantidad, precio FROM detalle_pedido";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setDetalle_id(rs.getInt("detalle_id"));
                    detalle.setPedido_id(rs.getInt("pedido_id"));
                    detalle.setNombreProducto(rs.getString("nombre_producto"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecio(rs.getBigDecimal("precio"));
                    detalles.add(detalle);
                }
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return detalles;
    }
}

