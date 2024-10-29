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

    /**
     * Obtiene una lista de todos los pedidos en el sistema.
     *
     * @return una lista de objetos {@link Pedido} que representan todos los pedidos.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    @Override
    public List<Pedido> getAllPedidos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        Connection connection = null;

        try {
            connection = conexionBD.getConnection();
            String sql = "SELECT id, cliente_id, total, metodo_pago, fecha FROM pedidos";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setId(rs.getInt("id"));
                    pedido.setClienteId(rs.getInt("cliente_id"));
                    pedido.setTotal(rs.getString("total"));
                    pedido.setMetodoPago(rs.getString("metodo_pago"));
                    pedido.setFecha(rs.getDate("fecha"));
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
