import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/actualizarEstadoPedido")
public class ActualizarEstadoPedidoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pedidoId = Integer.parseInt(request.getParameter("pedidoId"));
        String estado = request.getParameter("estado");

        try (Connection connection = conexionBD.getConnection()) {
            String updateSQL = "UPDATE pedidos SET estado = ? WHERE pedido_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
                stmt.setString(1, estado);
                stmt.setInt(2, pedidoId);
                stmt.executeUpdate();
                response.setStatus(HttpServletResponse.SC_OK); // Respuesta exitosa
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al actualizar el estado del pedido");
        }
    }
}

