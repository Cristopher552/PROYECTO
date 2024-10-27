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
import java.math.BigDecimal;

@WebServlet("/listarDetallesProductos")
public class ListarProductosServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<DetallePedido> detalles = new ArrayList<>();
        Connection connection = null;

        try {
            // Obtener conexión
            connection = conexionBD.getConnection();
            String sql = "SELECT pedido_id, nombre_producto, cantidad, precio FROM detalle_pedido";
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setPedidoId(rs.getInt("pedido_id"));
                    detalle.setNombreProducto(rs.getString("nombre_producto"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecio(rs.getBigDecimal("precio")); // Asegúrate de que este tipo sea compatible con tu base de datos
                    detalles.add(detalle);
                }
            }

            // Configurar la respuesta como JSON
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            Gson gson = new Gson();
            String json = gson.toJson(detalles);
            out.print(json);
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al listar los detalles de los productos");
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

    // Clase interna para representar un detalle de pedido
    public class DetallePedido {
        private int pedidoId;
        private String nombreProducto;
        private int cantidad;
        private BigDecimal precio;

        // Getters y setters
        public int getPedidoId() { return pedidoId; }
        public void setPedidoId(int pedidoId) { this.pedidoId = pedidoId; }
        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
        public BigDecimal getPrecio() { return precio; }
        public void setPrecio(BigDecimal precio) { this.precio = precio; }
    }
}
