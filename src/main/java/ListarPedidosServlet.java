import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import java.sql.Date;

@WebServlet("/listarPedidos")
public class ListarPedidosServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Pedido> pedidos = new ArrayList<>();
        Connection connection = null;

        try {
            // Obtener conexión
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
                    pedido.setFecha(rs.getDate("fecha")); // Cambia según tu tipo de fecha
                    pedidos.add(pedido);
                }
            }

            // Configurar la respuesta como JSON
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            String json = gson.toJson(pedidos);
            out.print(json);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar los pedidos");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Clase interna para representar un pedido
    public class Pedido {
        private int id;
        private int clienteId;
        private String total;
        private String metodoPago;
        private Date fecha;

        // Getters y setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getClienteId() { return clienteId; }
        public void setClienteId(int clienteId) { this.clienteId = clienteId; }
        public String getTotal() { return total; }
        public void setTotal(String total) { this.total = total; }
        public String getMetodoPago() { return metodoPago; }
        public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
        public Date getFecha() { return fecha; }
        public void setFecha(Date fecha) { this.fecha = fecha; }
    }
}
