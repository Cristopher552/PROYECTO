import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz {@link PedidoRepositorioDAO} para acceder a los pedidos.
 * Esta clase se encarga de las operaciones de recuperación de datos relacionados con los pedidos desde la base de datos.
 */

public class PedidoRepositorioImplem implements PedidoRepositorioDAO {

    @Override
    public List<Pedido> getAllPedidos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        Connection connection = null;

        try {
            connection = conexionBD.getConnection();
            String sql = "SELECT pedido_id, cliente_id, total, metodo_pago, estado, fecha_pedido FROM pedidos";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setPedido_id(rs.getInt("pedido_id"));  // Se usa 'pedido_id' en lugar de 'id'
                    pedido.setCliente_id(rs.getInt("cliente_id"));  // Se usa 'cliente_id' en lugar de 'clienteId'
                    pedido.setTotal(rs.getBigDecimal("total"));
                    pedido.setMetodoPago(rs.getString("metodo_pago"));
                    pedido.setEstado(rs.getString("estado"));
                    pedido.setFecha(rs.getDate("fecha_pedido"));
                    pedidos.add(pedido);
                }
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return pedidos;
    }
}

