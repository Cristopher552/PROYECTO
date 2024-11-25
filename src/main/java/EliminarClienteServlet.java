import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/eliminarCliente")
public class EliminarClienteServlet extends HttpServlet {
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int clienteId = Integer.parseInt(request.getParameter("id"));
        Connection connection = null;

        try {
            connection = conexionBD.getConnection();
            connection.setAutoCommit(false);

            
String deleteDetalleSQL = "DELETE FROM detalle_pedido WHERE pedido_id IN (SELECT pedido_id FROM pedidos WHERE cliente_id = ?)";
try (PreparedStatement stmtDetalle = connection.prepareStatement(deleteDetalleSQL)) {
    stmtDetalle.setInt(1, clienteId);
    stmtDetalle.executeUpdate();
}

// Eliminar pedidos
String deletePedidosSQL = "DELETE FROM pedidos WHERE cliente_id = ?";
try (PreparedStatement stmtPedidos = connection.prepareStatement(deletePedidosSQL)) {
    stmtPedidos.setInt(1, clienteId);
    stmtPedidos.executeUpdate();
}

// Eliminar cliente
String deleteClienteSQL = "DELETE FROM clientes WHERE cliente_id = ?";
try (PreparedStatement stmtCliente = connection.prepareStatement(deleteClienteSQL)) {
    stmtCliente.setInt(1, clienteId);
    stmtCliente.executeUpdate();
}


            connection.commit();
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar cliente");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }
}
