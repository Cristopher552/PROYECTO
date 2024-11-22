import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetallePedidoDaoTest {

    private Connection connection;

    @BeforeEach
    public void setUp() throws Exception {
        connection = conexionBD.getConnection();
        connection.setAutoCommit(false);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.rollback();
            connection.close();
        }
    }
@Test
public void testAgregarDetallePedido() throws Exception {
    try {
        String insertClienteSQL = "INSERT INTO clientes (nombre, apellidos, email, telefono, distrito, direccion, fecha_creacion, dni) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
        int clienteId = 1;  
        String nombreCliente = "Juan";
        String apellidosCliente = "Pérez";
        String emailCliente = "juan.perez@example.com";
        String telefonoCliente = "123456789";
        String distritoCliente = "Centro";
        String direccionCliente = "Calle Falsa 123";
        String dniCliente = "12345678";

        try (PreparedStatement stmtCliente = connection.prepareStatement(insertClienteSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtCliente.setString(1, nombreCliente);
            stmtCliente.setString(2, apellidosCliente);
            stmtCliente.setString(3, emailCliente);
            stmtCliente.setString(4, telefonoCliente);
            stmtCliente.setString(5, distritoCliente);
            stmtCliente.setString(6, direccionCliente);
            stmtCliente.setString(7, dniCliente);
            stmtCliente.executeUpdate();
            try (ResultSet rs = stmtCliente.getGeneratedKeys()) {
                assertTrue(rs.next());
                clienteId = rs.getInt(1);  // Obtener el ID generado
            }
        }

        double totalPedido = 150.00;
        String metodoPago = "tarjeta";
        String estado = "Espera";

        String insertPedidoSQL = "INSERT INTO pedidos (cliente_id, total, metodo_pago, estado, fecha_pedido) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        int pedidoId;
        try (PreparedStatement stmtPedido = connection.prepareStatement(insertPedidoSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtPedido.setInt(1, clienteId);
            stmtPedido.setDouble(2, totalPedido);
            stmtPedido.setString(3, metodoPago);
            stmtPedido.setString(4, estado);
            stmtPedido.executeUpdate();
            try (ResultSet rs = stmtPedido.getGeneratedKeys()) {
                assertTrue(rs.next());
                pedidoId = rs.getInt(1);
            }
        }

        String insertDetalleSQL = "INSERT INTO detalle_pedido (pedido_id, nombre_producto, cantidad, precio) VALUES (?, ?, ?, ?)";
        String nombreProducto = "Producto Test";
        int cantidad = 2;
        double precio = 50.00;

        try (PreparedStatement stmtDetalle = connection.prepareStatement(insertDetalleSQL)) {
            stmtDetalle.setInt(1, pedidoId);
            stmtDetalle.setString(2, nombreProducto);
            stmtDetalle.setInt(3, cantidad);
            stmtDetalle.setDouble(4, precio);
            int rowsInserted = stmtDetalle.executeUpdate();
            assertEquals(1, rowsInserted);

            String querySQL = "SELECT * FROM detalle_pedido WHERE pedido_id = ? AND nombre_producto = ?";
            try (PreparedStatement queryStmt = connection.prepareStatement(querySQL)) {
                queryStmt.setInt(1, pedidoId);
                queryStmt.setString(2, nombreProducto);
                try (ResultSet rs = queryStmt.executeQuery()) {
                    assertTrue(rs.next());
                    assertEquals(pedidoId, rs.getInt("pedido_id"));
                    assertEquals(nombreProducto, rs.getString("nombre_producto"));
                    assertEquals(cantidad, rs.getInt("cantidad"));
                    assertEquals(precio, rs.getDouble("precio"));
                }
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
        fail("Error al agregar detalle al pedido: " + e.getMessage());
    }
}
@Test
public void testEvitarDuplicidadProducto() throws Exception {
    try {
        String insertClienteSQL = "INSERT INTO clientes (nombre, apellidos, email, telefono, distrito, direccion, fecha_creacion, dni) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
        int clienteId = 1;
        String nombreCliente = "Juan";
        String apellidosCliente = "Pérez";
        String emailCliente = "juan.perez@example.com";
        String telefonoCliente = "123456789";
        String distritoCliente = "Centro";
        String direccionCliente = "Calle Falsa 123";
        String dniCliente = "12345678";

        try (PreparedStatement stmtCliente = connection.prepareStatement(insertClienteSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtCliente.setString(1, nombreCliente);
            stmtCliente.setString(2, apellidosCliente);
            stmtCliente.setString(3, emailCliente);
            stmtCliente.setString(4, telefonoCliente);
            stmtCliente.setString(5, distritoCliente);
            stmtCliente.setString(6, direccionCliente);
            stmtCliente.setString(7, dniCliente);
            stmtCliente.executeUpdate();
            try (ResultSet rs = stmtCliente.getGeneratedKeys()) {
                assertTrue(rs.next());
                clienteId = rs.getInt(1);
            }
        }

        double totalPedido = 150.00;
        String metodoPago = "tarjeta";
        String estado = "Espera";

        String insertPedidoSQL = "INSERT INTO pedidos (cliente_id, total, metodo_pago, estado, fecha_pedido) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        int pedidoId;
        try (PreparedStatement stmtPedido = connection.prepareStatement(insertPedidoSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtPedido.setInt(1, clienteId);
            stmtPedido.setDouble(2, totalPedido);
            stmtPedido.setString(3, metodoPago);
            stmtPedido.setString(4, estado);
            stmtPedido.executeUpdate();
            try (ResultSet rs = stmtPedido.getGeneratedKeys()) {
                assertTrue(rs.next());
                pedidoId = rs.getInt(1);
            }
        }

        String insertDetalleSQL = "INSERT INTO detalle_pedido (pedido_id, nombre_producto, cantidad, precio) VALUES (?, ?, ?, ?)";
        String nombreProducto = "Producto Test";
        int cantidad = 1;
        double precio = 50.00;

        try (PreparedStatement stmt = connection.prepareStatement(insertDetalleSQL)) {
            stmt.setInt(1, pedidoId);
            stmt.setString(2, nombreProducto);
            stmt.setInt(3, cantidad);
            stmt.setDouble(4, precio);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = connection.prepareStatement(insertDetalleSQL)) {
            stmt.setInt(1, pedidoId);
            stmt.setString(2, nombreProducto);
            stmt.setInt(3, cantidad);
            stmt.setDouble(4, precio);
            int rowsInserted = stmt.executeUpdate();
            assertEquals(1, rowsInserted);  
        }

    } catch (SQLException e) {
        e.printStackTrace();
        fail("Error al evitar duplicidad de producto: " + e.getMessage());
    }
}
@Test
public void testEvitarCantidadesNegativas() throws Exception {
    try {
        String insertClienteSQL = "INSERT INTO clientes (nombre, apellidos, email, telefono, distrito, direccion, fecha_creacion, dni) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";
        int clienteId = 1;
        String nombreCliente = "Juan";
        String apellidosCliente = "Pérez";
        String emailCliente = "juan.perez@example.com";
        String telefonoCliente = "123456789";
        String distritoCliente = "Centro";
        String direccionCliente = "Calle Falsa 123";
        String dniCliente = "12345678";

        try (PreparedStatement stmtCliente = connection.prepareStatement(insertClienteSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtCliente.setString(1, nombreCliente);
            stmtCliente.setString(2, apellidosCliente);
            stmtCliente.setString(3, emailCliente);
            stmtCliente.setString(4, telefonoCliente);
            stmtCliente.setString(5, distritoCliente);
            stmtCliente.setString(6, direccionCliente);
            stmtCliente.setString(7, dniCliente);
            stmtCliente.executeUpdate();
            try (ResultSet rs = stmtCliente.getGeneratedKeys()) {
                assertTrue(rs.next());
                clienteId = rs.getInt(1);
            }
        }
        double totalPedido = 100.00;
        String metodoPago = "efectivo";
        String estado = "Espera";

        String insertPedidoSQL = "INSERT INTO pedidos (cliente_id, total, metodo_pago, estado, fecha_pedido) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        int pedidoId;
        try (PreparedStatement stmtPedido = connection.prepareStatement(insertPedidoSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtPedido.setInt(1, clienteId);
            stmtPedido.setDouble(2, totalPedido);
            stmtPedido.setString(3, metodoPago);
            stmtPedido.setString(4, estado);
            stmtPedido.executeUpdate();
            try (ResultSet rs = stmtPedido.getGeneratedKeys()) {
                assertTrue(rs.next());
                pedidoId = rs.getInt(1);
            }
        }

        String insertDetalleSQL = "INSERT INTO detalle_pedido (pedido_id, nombre_producto, cantidad, precio) VALUES (?, ?, ?, ?)";
        String nombreProducto = "Producto Negativo";
        int cantidadNegativa = -1;
        double precio = 20.00;

        if (cantidadNegativa < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }

        try (PreparedStatement stmtDetalle = connection.prepareStatement(insertDetalleSQL)) {
            stmtDetalle.setInt(1, pedidoId);
            stmtDetalle.setString(2, nombreProducto);
            stmtDetalle.setInt(3, cantidadNegativa);
            stmtDetalle.setDouble(4, precio);
            stmtDetalle.executeUpdate();
        }
        fail("No se lanzó una excepción para una cantidad negativa");

    } catch (IllegalArgumentException e) {
        assertEquals("La cantidad no puede ser negativa", e.getMessage());
    } catch (SQLException e) {
        e.printStackTrace();
        fail("Error durante la prueba de evitar cantidades negativas: " + e.getMessage());
    }
}

}