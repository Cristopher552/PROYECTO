import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.google.gson.Gson;
import java.sql.ResultSet;
import com.google.common.collect.ImmutableList;

/**
 * ConfirmarPagoServlet maneja el procesamiento de pagos y la inserción de datos relacionados en la base de datos.
 * Este servlet recibe los datos del formulario de pago, los guarda en la base de datos y gestiona transacciones.
 */
@WebServlet("/confirmarPago")
public class ConfirmarPagoServlet extends HttpServlet {

    /**
     * Procesa la solicitud POST para confirmar y registrar un pago en la base de datos.
     * Obtiene los datos del cliente, el pedido y los detalles del pedido del formulario enviado, y los inserta en las tablas
     * correspondientes de la base de datos, usando transacciones para asegurar la consistencia de los datos.
     *
     * @param request  El objeto HttpServletRequest que contiene los datos del formulario enviados por el cliente.
     * @param response El objeto HttpServletResponse que se usa para enviar la respuesta al cliente.
     * @throws ServletException si ocurre un error en el procesamiento del servlet.
     * @throws IOException      si ocurre un error de entrada/salida al manejar la solicitud o la respuesta.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection connection = null;

        try {
            // Obtener conexión a la base de datos
            connection = conexionBD.getConnection();
            connection.setAutoCommit(false); // Inicia la transacción

            // Obtener datos del formulario
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String distrito = request.getParameter("distrito");
            String direccion = request.getParameter("direccion");
            String metodoPago = request.getParameter("metodoPago");
            String totalAPagar = request.getParameter("total");

            // Insertar datos del cliente en la base de datos
            String insertClienteSQL = "INSERT INTO clientes (nombre, apellidos, email, telefono, distrito, direccion) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmtCliente = connection.prepareStatement(insertClienteSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtCliente.setString(1, nombre);
                stmtCliente.setString(2, apellidos);
                stmtCliente.setString(3, email);
                stmtCliente.setString(4, telefono);
                stmtCliente.setString(5, distrito);
                stmtCliente.setString(6, direccion);
                stmtCliente.executeUpdate();

                // Obtener el ID del cliente insertado
                try (ResultSet generatedKeys = stmtCliente.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int clienteId = generatedKeys.getInt(1);

                        // Insertar datos del pedido en la base de datos
                        String insertPedidoSQL = "INSERT INTO pedidos (cliente_id, total, metodo_pago) VALUES (?, ?, ?)";
                        try (PreparedStatement stmtPedido = connection.prepareStatement(insertPedidoSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                            stmtPedido.setInt(1, clienteId);
                            stmtPedido.setString(2, totalAPagar);
                            stmtPedido.setString(3, metodoPago);
                            stmtPedido.executeUpdate();

                            // Obtener el ID del pedido insertado
                            try (ResultSet generatedKeysPedido = stmtPedido.getGeneratedKeys()) {
                                if (generatedKeysPedido.next()) {
                                    int pedidoId = generatedKeysPedido.getInt(1);

                                    // Convertir detalles de los productos a listas inmutables usando Gson y Guava
                                    Gson gson = new Gson();
                                    String[] nombresProductosArray = gson.fromJson(request.getParameter("nombresProductos"), String[].class);
                                    int[] cantidades = gson.fromJson(request.getParameter("cantidades"), int[].class);
                                    String[] preciosArray = gson.fromJson(request.getParameter("precios"), String[].class);

                                    ImmutableList<String> nombresProductos = ImmutableList.copyOf(nombresProductosArray);

                                    // Insertar detalles del pedido en la base de datos
                                    String insertDetalleSQL = "INSERT INTO detalle_pedido (pedido_id, nombre_producto, cantidad, precio) VALUES (?, ?, ?, ?)";
                                    try (PreparedStatement stmtDetalle = connection.prepareStatement(insertDetalleSQL)) {
                                        for (int i = 0; i < nombresProductos.size(); i++) {
                                            stmtDetalle.setInt(1, pedidoId);
                                            stmtDetalle.setString(2, nombresProductos.get(i));
                                            stmtDetalle.setInt(3, cantidades[i]);
                                            stmtDetalle.setString(4, preciosArray[i]);
                                            stmtDetalle.addBatch();
                                        }
                                        stmtDetalle.executeBatch();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Confirmar la transacción
            connection.commit();
            response.setStatus(HttpServletResponse.SC_OK); // Respuesta exitosa

            // Redirigir a la página de inicio
            response.sendRedirect("index.html"); // Cambia "index.html" al nombre de tu página de inicio

        } catch (SQLException e) {
            // Manejo de errores y rollback en caso de falla
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la compra");
        } finally {
            // Cerrar la conexión a la base de datos
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
